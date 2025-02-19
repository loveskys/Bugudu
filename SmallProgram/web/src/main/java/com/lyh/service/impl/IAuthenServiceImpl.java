package com.lyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.dao.AuthenMapper;
import com.lyh.entity.BizException;
import com.lyh.entity.ComResponse;
import com.lyh.entity.authen.*;
import com.lyh.entity.constant.ActivityConstant;
import com.lyh.entity.constant.MinioConstant;
import com.lyh.entity.constant.MsgConstant;
import com.lyh.entity.home.msg.MessageSendDto;
import com.lyh.entity.user.StudentUser;
import com.lyh.entity.wxresponse.WxCheckTextResp;
import com.lyh.service.*;
import com.lyh.util.ThreadLocalUtil;
import com.lyh.util.TokenUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * @author lyh
 * @since 2023-07-31
 */
@Slf4j
@Service
public class IAuthenServiceImpl extends ServiceImpl<AuthenMapper, Authen> implements IAuthenService {

    @Resource
    private MinioService minioService;
    @Resource
    private IStudentUserService iStudentUserService;
    @Resource
    private IMessageService iMessageService;
    @Resource
    private WxRequest wxRequest;


    @Override
    @Transactional
    public Authen submit(MultipartFile file, Map<String, String> allParams) {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
        String schoolName = allParams.get("schoolName");
        String studentNum = allParams.get("studentNum");
        String studentNum2 = allParams.get("studentNum2");
        String authenType = allParams.get("authenType");

        QueryWrapper<Authen> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Authen::getUserId, ThreadLocalUtil.getStudentUserId()).ne(Authen::getStatus, ActivityConstant.student_authen_nopass);
        Authen authen_t = getOne(queryWrapper);
        if(authen_t != null) {
            if(!ActivityConstant.wx_pic_check_nopass.equals(authen_t.getWxImgCheck())) {
                throw new BizException(ComResponse.ERROR, "您已提交过，请等待审核通过");
            }
        }
        if(baseMapper.checkStudentNum(schoolName, ThreadLocalUtil.getStudentUserId())>0) {
            throw new BizException(ComResponse.ERROR, "学号已被认证");
        }

        StudentUser user = iStudentUserService.getById(ThreadLocalUtil.getStudentUserId());
        if(StringUtils.hasText(studentNum)) {
            WxCheckTextResp check_1 = wxRequest.wxCheckTextSecurity(user.getWxOpenId(), studentNum, "1");
            if(check_1.getCode()!=100) {
                throw new BizException(ComResponse.ERROR, "学号内容违规："+check_1.getMsg());
            }
        }

        if(!studentNum.equals(studentNum2)) {
            throw new BizException(ComResponse.ERROR, "两次学号不相等请重新输入");
        }

        Authen authen = new Authen();
        if(file!=null) {
            String authen_oos_url = minioService.putObject(MinioConstant.authenImg, file,studentNum+TokenUtil.getuid32()+".png");
            if (StringUtils.hasText(authen_oos_url)) {
                authen.setAuthenImg(authen_oos_url);
            }
        }

        authen.setUserId(ThreadLocalUtil.getStudentUserId());
        authen.setSchoolName(schoolName);
        authen.setStudentNum(studentNum);
        authen.setAuthenType(authenType);
        authen.setStatus(ActivityConstant.student_authen_ing);
        authen.setWxImgCheck(ActivityConstant.wx_pic_check_ing);
        authen.setCont("等待微信图片检测结果");
        if (save(authen)) {
            iStudentUserService.updateUserAuthenById(authen.getId(), "", ActivityConstant.student_authen_ing, ThreadLocalUtil.getStudentUserId());
            wxRequest.wxCheckPicSecurity("1", authen.getId(), user.getWxOpenId(), authen.getAuthenImg());
            return authen;
        }
        return null;
    }


    @Override
    @Transactional
    public Authen resubmit(Authen authen) {
        QueryWrapper<Authen> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Authen::getUserId, ThreadLocalUtil.getStudentUserId()).ne(Authen::getStatus, "3");
        Authen authen_t = getOne(queryWrapper);
        if(authen_t != null) {
            if(!ActivityConstant.wx_pic_check_nopass.equals(authen_t.getWxImgCheck())) {
                throw new BizException(ComResponse.ERROR, "您已提交过，请等待审核通过");
            }
        }
        if(baseMapper.checkStudentNum(authen.getStudentNum(), ThreadLocalUtil.getStudentUserId())>0) {
            throw new BizException(ComResponse.ERROR, "该学号已经被认证");
        }
        if(StringUtils.hasText(authen.getStudentNum())) {
            StudentUser user = iStudentUserService.getById(ThreadLocalUtil.getStudentUserId());
            WxCheckTextResp check_1 = wxRequest.wxCheckTextSecurity(user.getWxOpenId(), authen.getStudentNum(), "1");
            if(check_1.getCode()!=100) {
                throw new BizException(ComResponse.ERROR, "学号内容违规："+check_1.getMsg());
            }
        }
        //TODO can't be null
        authen.setId(null);
        authen.setUserId(ThreadLocalUtil.getStudentUserId());
        authen.setStatus(ActivityConstant.student_authen_ing);
        if (save(authen)) {
            iStudentUserService.updateUserAuthenById(authen.getId(), "", ActivityConstant.student_authen_ing, ThreadLocalUtil.getStudentUserId());
            return authen;
        }
        return null;
    }


    @Override
    public Authen detail(AuthenDetailDto dto) {
        QueryWrapper<Authen> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Authen::getUserId, dto.getUserId()).orderByDesc(Authen::getCreateTime).last("LIMIT 1");
        Authen authen = getOne(queryWrapper);
        authen = clAuthen(authen);
        return authen;
    }


    @Override
    public IPage<AuthenVo> list(AuthenSearchDto dto) {
        IPage<AuthenVo> pageVo = baseMapper.getList(new Page<>(dto.getPageCount(), dto.getPageSize()), dto.getSchoolName(), dto.getStudentName(), dto.getStatus());
        if(pageVo!=null && pageVo.getTotal()>0) {
            return pageVo;
        }
        return null;
    }


    private Authen clAuthen(Authen authen) {
        if(authen!=null) {
            if(ActivityConstant.wx_pic_check_nopass.equals(authen.getWxImgCheck())) {
                authen.setStatus(ActivityConstant.student_authen_nopass);
                authen.setAuthenImg("");
            }
        }
        return authen;
    }


    @Override
    @Transactional
    public void updateStatus(AuthenUpdateDto dto) {
        Authen authen = baseMapper.selectById(dto.getId());
        if(authen!=null) {
            System.out.println("666666");
            if(!ActivityConstant.wx_pic_check_pass.equals(authen.getWxImgCheck())) {
                throw new BizException(ComResponse.ERROR, "微信未通过图片审核无法提交");
            }
        }

        //更新authen表
        baseMapper.updateStatus(dto.getId(), dto.getCont(), dto.getStatus());
        //更新student_user表的认证状态
        iStudentUserService.updateUserAuthenById(dto.getId(), dto.getCont(), dto.getStatus(), dto.getUserId());


        if(dto.getStatus().equals(ActivityConstant.student_authen_pass)) {
            //认证成功-发送通知
            MessageSendDto sendDto = new MessageSendDto();
            sendDto.setReceiveUserId(dto.getUserId());
            sendDto.setMsgType(MsgConstant.msg_type_official_tome);
            sendDto.setMsgTheme(MsgConstant.official_theme);
            sendDto.setMsgText("你的身份认证已成功，你现在可以参加或者发布活动啦！");
            iMessageService.taskSend(sendDto);
        }
        if(dto.getStatus().equals(ActivityConstant.student_authen_nopass)) {
            //认证失败-发送通知
            MessageSendDto sendDto = new MessageSendDto();
            sendDto.setReceiveUserId(dto.getUserId());
            sendDto.setMsgType(MsgConstant.msg_type_official_tome);
            sendDto.setMsgTheme(MsgConstant.official_theme);
            sendDto.setMsgText("你的身份认证不通过，原因："+dto.getCont());
            iMessageService.taskSend(sendDto);
        }

    }

}
