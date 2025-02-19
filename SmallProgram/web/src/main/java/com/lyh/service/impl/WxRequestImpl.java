package com.lyh.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lyh.dao.ActivityMapper;
import com.lyh.dao.AuthenMapper;
import com.lyh.dao.WxcheckbizrecoMapper;
import com.lyh.entity.BizException;
import com.lyh.entity.ComResponse;
import com.lyh.entity.activity.Activity;
import com.lyh.entity.authen.Authen;
import com.lyh.entity.constant.ActivityConstant;
import com.lyh.entity.constant.MsgConstant;
import com.lyh.entity.home.msg.MessageSendDto;
import com.lyh.entity.user.WxUserLoginDto;
import com.lyh.entity.wxresponse.*;
import com.lyh.service.IMessageService;
import com.lyh.service.WxRequest;
import com.lyh.util.DistributedLock;
import com.lyh.util.TokenUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.*;

@Slf4j
@Service
public class WxRequestImpl implements WxRequest {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private DistributedLock distributedLock;
    @Resource
    private WxcheckbizrecoMapper wxcheckbizrecoMapper;
    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private AuthenMapper authenMapper;
    @Resource
    private IMessageService iMessageService;



    @Value(value = "${wx.getOpenIdUrl}")
    private String getOpenIdUrl;
    @Value(value = "${wx.getAccessTokenUrl}")
    private String getAccessTokenUrl;
    @Value(value = "${wx.appId}")
    private String appId;
    @Value(value = "${wx.appSecret}")
    private String appSecret;
    @Value("${environment.env}")
    private String env;

    /**
     * 调用wx（getAccessToken）获取调用凭据
     */
    public String getAccessToken() {
        try {
            //todo 这两个key名字要区分一下，比如当前正式环境，叫 prod_wx_access_token 和 prod_wx_access_token_lock
            // 测s环境，叫 dev_wx_access_token 和 dev_wx_access_token_lock
            String accessTokenKey = env + "_wx_access_token";
            String lockKey = env + "_wx_access_token_lock";
                if (distributedLock.acquireLock(lockKey, TokenUtil.getuid32(), 20)) {
                try {
                    String accessToken = stringRedisTemplate.opsForValue().get(accessTokenKey);
                    if (accessToken == null || accessToken.isEmpty()) {
                        String requestUrl = getAccessTokenUrl + "?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
                        ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
                        if (response.getStatusCode().is2xxSuccessful()) {
                            String result = response.getBody();
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode jsonNode = mapper.readTree(result);
                            if (jsonNode.has("access_token") && jsonNode.has("expires_in")) {
                                accessToken = jsonNode.get("access_token").asText();
                                int expiresIn = jsonNode.get("expires_in").asInt();
                                stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, expiresIn - 600, TimeUnit.SECONDS);
                                return accessToken;
                            } else {
                                throw new BizException(ComResponse.ERROR, "getAccessToken响应中没有access_token或expires_in");
                            }
                        }
                    }
                    return accessToken;
                } finally {
                    distributedLock.releaseLock(lockKey);
                }
            } else {
                log.warn("无法获取锁，可能有其他线程正在获取AccessToken");
                return null;
            }
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取微信AccessToken时异常：{}", e.getMessage());
            throw new BizException(ComResponse.ERROR, "获取微信AccessToken时异常：" + e.getMessage());
        }
    }


    /**
     * 微信获取openid方法
     */
    public WxCommonResp wxGetOpenId(String code) {
        try {
            String url = getOpenIdUrl + "?grant_type=authorization_code&appid=" + appId + "&secret=" + appSecret + "&js_code=" + code;
            System.out.println("开始获取 result");
            String result = restTemplate.getForObject(url, String.class);
            System.out.println("成功获取到 result 如下：");
            System.out.println(result);
            if (StringUtils.hasText(result)) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(result);
                if (jsonNode.has("openid") && jsonNode.has("session_key")) {
                    WxCommonResp wxCommonResp = new WxCommonResp();
                    wxCommonResp.setOpenId(jsonNode.get("openid").asText());
                    wxCommonResp.setSessionKey(jsonNode.get("session_key").asText());
                    return wxCommonResp;
                } else {
                    throw new BizException(ComResponse.ERROR, "wxLoginFn响应中没有session_key或openid");
                }
            } else {
                throw new BizException(ComResponse.ERROR, "wxLoginFn响应result为空");
            }
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("处理微信code校验时异常：{}", e.getMessage());
            throw new BizException(ComResponse.ERROR, "处理微信code校验时出现异常：" + e.getMessage());
        }
    }


    /**
     * 微信文本安全校验
     */
    @Override
    public WxCheckTextResp wxCheckTextSecurity(String openId, String content, String scene) {
        try {
            String accessToken = getAccessToken();
            System.out.println("accessToken获取成功：");
            if (accessToken != null) {
                String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode postData = objectMapper.createObjectNode();
                postData.put("version", "2");
                postData.put("openid", openId);
                postData.put("content", content);       //内容
                postData.put("scene", scene);           //场景枚举值(1资料；2评论；3论坛；4社交日志)
                postData.put("title", new String(content.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)); //文本标题，需使用UTF-8编码
                postData.put("nickname", new String(content.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)); //用户昵称，需使用UTF-8编码
                postData.put("signature", new String(content.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)); //个性签名，该参数仅在资料类场景有效(scene=1)，需使用UTF-8编码
                HttpEntity<byte[]> entity = new HttpEntity<>(postData.toString().getBytes(StandardCharsets.UTF_8), headers);
                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    String result = response.getBody();
                    JsonNode resultJsonNode = objectMapper.readTree(result);
                    if ("0".equals(resultJsonNode.get("errcode").asText())) {
                        JsonNode detailNode = resultJsonNode.get("result");
                        int label = detailNode.get("label").asInt();
                        return switch (label) {
                            case 100 -> new WxCheckTextResp(100, "正常");
                            case 10001 -> new WxCheckTextResp(10001, "广告");
                            case 20001 -> new WxCheckTextResp(20001, "时政");
                            case 20002 -> new WxCheckTextResp(20002, "色情");
                            case 20003 -> new WxCheckTextResp(20003, "辱骂");
                            case 20006 -> new WxCheckTextResp(20006, "违法犯罪");
                            case 20008 -> new WxCheckTextResp(20008, "欺诈");
                            case 20012 -> new WxCheckTextResp(20012, "低俗");
                            case 20013 -> new WxCheckTextResp(20013, "版权");
                            case 21000 -> new WxCheckTextResp(21000, "其他");
                            default -> throw new BizException(ComResponse.ERROR, "未知错误");
                        };
                    } else {
                        throw new BizException(ComResponse.ERROR, "请重新提交");
                    }
                } else {
                    throw new BizException(ComResponse.ERROR, "微信文本安全校验请求失败");
                }
            }
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信文本安全校验时异常：{}", e.getMessage());
            throw new BizException(ComResponse.ERROR, "微信文本安全校验时异常：" + e.getMessage());
        }
        return null;
    }


    /**
     * 微信图片安全校验
     */
    @Override
    public String wxCheckPicSecurity(String bizType, String bizId, String openId, String mediaUrl) {
        try {
            System.out.println("开始校验");
            String accessToken = getAccessToken();
            System.out.println("已经成功获取到token：" + accessToken);
            if (accessToken != null) {
                String url = "https://api.weixin.qq.com/wxa/media_check_async?access_token=" + accessToken;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode postData = objectMapper.createObjectNode();
                postData.put("version", 2);
                postData.put("media_type", 2);
                postData.put("openid", new String(openId.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
                postData.put("media_url", mediaUrl);
                postData.put("scene", 3);  //场景枚举值(1资料；2评论；3论坛；4社交日志)
                HttpEntity<byte[]> entity = new HttpEntity<>(postData.toString().getBytes(StandardCharsets.UTF_8), headers);
                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    String result = response.getBody();
                    System.out.println("图片检测发送结果：" + result);
                    //{"errcode":0,"errmsg":"ok","trace_id":"66d55a77-79cc983d-43ec4c95"}
                    JsonNode resultJsonNode = objectMapper.readTree(result);
                    if ("0".equals(resultJsonNode.get("errcode").asText())) {
                        String trace_id = resultJsonNode.get("trace_id").asText();
                        if (StringUtils.hasText(trace_id)) {
                            Wxcheckbizreco wcbr = new Wxcheckbizreco();
                            wcbr.setBizId(bizId);
                            wcbr.setBizType(bizType);
                            wcbr.setTraceId(trace_id);
                            wcbr.setCheckStatus(ActivityConstant.pci_checking);
                            if (wxcheckbizrecoMapper.insert(wcbr) == 1) {
                                return trace_id;
                            }
                        }
                    } else {
                        throw new BizException(ComResponse.ERROR, "请重新提交");
                    }
                } else {
                    throw new BizException(ComResponse.ERROR, "微信图片安全请求校验失败");
                }
            }
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信图片安全请求校验时异常：{}", e.getMessage());
            throw new BizException(ComResponse.ERROR, "微信图片安全请求校验时异常：" + e.getMessage());
        }
        return null;
    }


    //解密wx的数据获取头像
    public String decryptWxInfo(WxUserLoginDto dto, String sessionKey) {
        if (StringUtils.hasText(dto.getIv()) && StringUtils.hasText(dto.getEncryptedData())) {
            try {
                byte[] dataByte = Base64.getDecoder().decode(dto.getEncryptedData());
                byte[] keyByte = Base64.getDecoder().decode(sessionKey);
                byte[] ivByte = Base64.getDecoder().decode(dto.getIv());
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
                AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
                parameters.init(new IvParameterSpec(ivByte));
                cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
                byte[] resultByte = cipher.doFinal(dataByte);
                if (null != resultByte && resultByte.length > 0) {
                    String ss = new String(resultByte, StandardCharsets.UTF_8);
                    if (StringUtils.hasText(ss)) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        var userInfoMap = objectMapper.readValue(ss, Map.class);
                        return (String) userInfoMap.get("avatarUrl");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("解密失败："+e.getMessage());
                log.error("解密失败", e);
            }
        }
        return null;
    }



    @Override
    @Transactional
    public String clWxBackCell(WxPicCheckDto dto) {
        if(appId.equals(dto.getAppid()) && dto.getTrace_id()!=null) {
            if(dto.getResult()!=null) {
                WxPciCheckResult result = dto.getResult();
                if(result.getLabel() != null) {
                    //命中标签枚举值label，100正常；20001时政；20002色情；20006违法犯罪；21000其他
                    String trace_id = dto.getTrace_id();
                    Wxcheckbizreco wcbr = wxcheckbizrecoMapper.getByTraceId(trace_id);
                    if(wcbr!=null) {
                        if(result.getLabel().equals(100)) {
                            wcbr.setCheckStatus(ActivityConstant.pci_checkok);
                            wcbr.setCheckCont("图片正常");
                        }
                        if(result.getLabel().equals(20001)) {
                            wcbr.setCheckStatus(ActivityConstant.pci_checkno);
                            wcbr.setCheckCont("图片涉及时政");
                        }
                        if(result.getLabel().equals(20002)) {
                            wcbr.setCheckStatus(ActivityConstant.pci_checkno);
                            wcbr.setCheckCont("图片涉及色情");
                        }
                        if(result.getLabel().equals(20006)) {
                            wcbr.setCheckStatus(ActivityConstant.pci_checkno);
                            wcbr.setCheckCont("图片涉及违法犯罪");
                        }
                        if(result.getLabel().equals(21000)) {
                            wcbr.setCheckStatus(ActivityConstant.pci_checkno);
                            wcbr.setCheckCont("图片其他违规");
                        }
                    }

                    int i = wxcheckbizrecoMapper.updateById(wcbr);
                    if(i == 1) {
                        //更新学生认证主表
                        if(wcbr.getBizType().equals("1")) {
                            Authen authen = authenMapper.selectById(wcbr.getBizId());
                            if(authen!=null) {
                                if(wcbr.getCheckStatus().equals(ActivityConstant.pci_checkok)) {
                                    authen.setWxImgCheck(ActivityConstant.wx_pic_check_pass);
                                    authen.setCont("微信检测正常");
                                    authenMapper.updateById(authen);
                                } else {
                                    authen.setWxImgCheck(ActivityConstant.wx_pic_check_nopass);
                                    authen.setStatus(ActivityConstant.student_authen_nopass);
                                    authen.setCont(wcbr.getCheckCont());

                                    if(authenMapper.updateById(authen)==1) {
                                        //认证成功-发送通知
                                        MessageSendDto sendDto = new MessageSendDto();
                                        sendDto.setReceiveUserId(authen.getUserId());
                                        sendDto.setMsgType(MsgConstant.msg_type_official_tome);
                                        sendDto.setMsgTheme(MsgConstant.official_theme);
                                        sendDto.setMsgText("你的身份认证失败，图片违规："+wcbr.getCheckCont());
                                        iMessageService.taskSend(sendDto);
                                    }
                                }
                            }
                        }

                        //更新活动主表
                        if(wcbr.getBizType().equals("2")) {
                            Activity activity = activityMapper.selectById(wcbr.getBizId());
                            if(activity!=null) {
                                if(wcbr.getCheckStatus().equals(ActivityConstant.pci_checkok)) {
                                    activity.setWxImgCheck(ActivityConstant.wx_pic_check_pass);
                                    activity.setAuditCont("微信检测正常");
                                    //活动发布成功
                                    if(activityMapper.updateById(activity)==1) {
                                        //活动发布成功-发送通知
                                        MessageSendDto sendDto = new MessageSendDto();
                                        sendDto.setReceiveUserId(activity.getIssue());
                                        sendDto.setMsgTheme("活动发布成功");
                                        sendDto.setMsgText("您的活动（"+activity.getTheme()+"）发布成功，赶快召集小伙伴参加吧，点击查看详情~");
                                        sendDto.setMsgType(MsgConstant.msg_type_activity_create);
                                        sendDto.setActivityId(activity.getId());
                                        iMessageService.taskSend(sendDto);
                                    }
                                } else {
                                    activity.setWxImgCheck(ActivityConstant.wx_pic_check_nopass);
                                    activity.setAuditCont(wcbr.getCheckCont());
                                    //活动发布违规
                                    if(activityMapper.updateById(activity)==1) {
                                        //活动发布违规-发送通知
                                        MessageSendDto sendDto = new MessageSendDto();
                                        sendDto.setReceiveUserId(activity.getIssue());
                                        sendDto.setMsgTheme("活动发布失败");
                                        sendDto.setMsgText("您的活动（"+activity.getTheme()+"）图片违规："+wcbr.getCheckCont()+"，请重新发布哦~");
                                        sendDto.setMsgType(MsgConstant.msg_type_activity_create);
                                        sendDto.setActivityId("");
                                        iMessageService.taskSend(sendDto);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return "success";
    }





}
