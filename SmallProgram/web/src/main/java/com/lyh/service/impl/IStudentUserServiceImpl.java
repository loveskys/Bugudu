package com.lyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.dao.AuthenMapper;
import com.lyh.dao.StudentUserMapper;
import com.lyh.dao.UserLogMapper;
import com.lyh.entity.BizException;
import com.lyh.entity.ComResponse;
import com.lyh.entity.authen.Authen;
import com.lyh.entity.constant.ActivityConstant;
import com.lyh.entity.constant.MinioConstant;
import com.lyh.entity.user.*;
import com.lyh.entity.wxresponse.WxCheckTextResp;
import com.lyh.entity.wxresponse.WxCommonResp;
import com.lyh.service.IStudentUserService;
import com.lyh.service.MinioService;
import com.lyh.service.WxRequest;
import com.lyh.util.ComUtil;
import com.lyh.util.ThreadLocalUtil;
import com.lyh.util.TokenUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyh
 * @since 2023-07-05
 */
@Slf4j
@Service
public class IStudentUserServiceImpl extends ServiceImpl<StudentUserMapper, StudentUser> implements IStudentUserService {

    @Resource
    private UserLogMapper userLogMapper;
    @Resource
    private TokenUtil tokenUtil;
    @Resource
    private MinioService minioService;
    @Resource
    private AuthenMapper authenMapper;
    @Resource
    private WxRequest wxRequest;

    @Override
    @Transactional
    public StudentUser getByUserName(String userName, String pwd) {
        if (StringUtils.hasText(pwd)) {
            QueryWrapper<StudentUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(StudentUser::getUserName, userName).eq(StudentUser::getPassWord, pwd);
            List<StudentUser> list = list(queryWrapper);
            if (list != null && !list.isEmpty()) {
                StudentUser user = list.getFirst();
                if (pwd.equals(user.getPassWord())) {
                    user.setToken(tokenUtil.signToken(user.getId(), TokenUtil.STUDENT_TOKEN));
                    return user;
                }
            }
        }
        return null;
    }


    @Override
    @Transactional
    public void saveUserLog(String userId, String userName, String sysTag, String remark) {
        UserLog userLog = new UserLog(userId, userName, sysTag, remark, LocalDateTime.now(), userId, LocalDateTime.now(), userId);
        userLogMapper.insert(userLog);
    }


    @Override
    @Transactional
    public StudentUser wxLogin(WxUserLoginDto dto) {
        WxCommonResp wxCommonResp = wxRequest.wxGetOpenId(dto.getCode());
        if(wxCommonResp!=null) {
            QueryWrapper<StudentUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(StudentUser::getWxOpenId, wxCommonResp.getOpenId());
            List<StudentUser> list = list(queryWrapper);
            if (list != null && !list.isEmpty()) {
                StudentUser user = list.getFirst();
                user.setToken(tokenUtil.signToken(user.getId(), TokenUtil.STUDENT_TOKEN));
                user.setPassWord("");
                return user;
            } else {
                StudentUser user = new StudentUser();
                user.setWxOpenId(wxCommonResp.getOpenId());
                user.setWxSessionKey(wxCommonResp.getSessionKey());
                user.setSex(ActivityConstant.unknownSex);
                user.setNickName("不咕嘟"+ ComUtil.gAlphanumeric());
                user.setUserName(wxCommonResp.getOpenId());
                user.setCreateBy("system");
                user.setUpdateBy("system");
                user.setHeadUrl(wxRequest.decryptWxInfo(dto, wxCommonResp.getSessionKey()));
                int i = getBaseMapper().insert(user);
                if (i == 1) {
                    user.setToken(tokenUtil.signToken(user.getId(), TokenUtil.STUDENT_TOKEN));
                    user.setPassWord("");
                    return user;
                }
            }
        }
        return null;
    }


    @Override
    @Transactional
    public StudentUser register(StudentRegistDto dto) {
        try {
            QueryWrapper<StudentUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(StudentUser::getPhone, dto.getPhone()).or().eq(StudentUser::getUserName, dto.getUserName());
            List<StudentUser> list1 = list(queryWrapper);
            if (list1 != null && !list1.isEmpty()) {
                throw new BizException(ComResponse.ERROR, "用户名或手机号重复");
            }

            WxCommonResp wxCommonResp = wxRequest.wxGetOpenId(dto.getCode());
            if(wxCommonResp==null) {
                return null;
            }
            queryWrapper.clear();
            queryWrapper.lambda().eq(StudentUser::getWxOpenId, wxCommonResp.getOpenId());
            List<StudentUser> list2 = list(queryWrapper);
            if (list2 != null && !list2.isEmpty()) {
                throw new BizException(ComResponse.ERROR, "您已使用过微信登录，无法注册");
            }

            String head_oos_url = minioService.putBase64Image(MinioConstant.headImg, dto.getHeadUrl(), dto.getUserName() + ".png");
            StudentUser user = new StudentUser();
            if (StringUtils.hasText(head_oos_url)) {
                user.setHeadUrl(head_oos_url);
            }

            user.setWxOpenId(wxCommonResp.getOpenId());
            user.setWxSessionKey(wxCommonResp.getSessionKey());
            user.setUserName(dto.getUserName());
            user.setNickName(dto.getNickName());
            user.setBirthdate(dto.getBirthdate());
            user.setPassWord(dto.getPassWord());
            user.setPhone(dto.getPhone());

            String joinedString = String.join(",", dto.getRegion());
            user.setPccText(joinedString);

            user.setProv(dto.getRegion_code()[0]);
            if (dto.getRegion_code().length > 1) {
                user.setCity(dto.getRegion_code()[1]);
            }
            if (dto.getRegion_code().length == 3) {
                user.setCounty(dto.getRegion_code()[2]);
            }
            user.setAddress(dto.getAddress());
            user.setCreateBy("system");
            user.setUpdateBy("system");

            int i = getBaseMapper().insert(user);
            if (i == 1) {
                user.setToken(tokenUtil.signToken(user.getId(), TokenUtil.STUDENT_TOKEN));
                user.setPassWord("");
                return user;
            }
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("处理注册时异常：{}", e.getMessage());
            throw new BizException(ComResponse.ERROR, "处理注册时异常：" + e.getMessage());
        }
        return null;
    }


    @Override
    public StudentInfoVo getInfoDetail(StudentDetailDto dto) {
            QueryWrapper<StudentUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(StudentUser::getId, dto.getUserId());
            StudentUser user = getOne(queryWrapper);

            if(user!=null) {
            StudentInfoVo vo = new StudentInfoVo();
            vo.setId(user.getId());
            vo.setUserName(user.getUserName());
            vo.setHeadUrl(user.getHeadUrl());
            vo.setNickName(user.getNickName());
            vo.setMbti(user.getMbti());
            vo.setIntro(user.getIntro());
            vo.setAge(user.getAge());
            vo.setConstellation(user.getConstellation());
            vo.setWxOpenId(user.getWxOpenId());
            vo.setCategory(baseMapper.getUserCategory(user.getId()));

            if(StringUtils.hasText(user.getHobby())) {
                String[] arr = user.getHobby().split(",");
                vo.setHobby(user.getHobby());
                vo.setHobbyArray(arr);
            }

            //资料完善状态
            if(StringUtils.hasText(user.getBirthdate()) && StringUtils.hasText(user.getSex())) {
                vo.setBirthdate(user.getBirthdate());
                vo.setSex(user.getSex());
                vo.setInfoPerfect("1"); //已完善
            } else {
                vo.setInfoPerfect("0"); //未完善
            }

            List<String> auths = new ArrayList<>();
            List<String> auths2 = new ArrayList<>();
            vo.setAuthStatus(ActivityConstant.student_authen_no);
            if(StringUtils.hasText(user.getAuthId())) {
              Authen authen = authenMapper.selectById(user.getAuthId());
              if(authen!=null) {
                  vo.setAuthStatus(authen.getStatus());
                  vo.setAuthId(authen.getId());
                  vo.setAuthCont(authen.getCont());
                  //认证通过
                  if(ActivityConstant.student_authen_pass.equals(authen.getStatus()) && ActivityConstant.wx_pic_check_pass.equals(authen.getWxImgCheck())) {
                      if(StringUtils.hasText(authen.getSchoolName())) {
                          auths.add(authen.getSchoolName());
                          auths2.add(authen.getSchoolName());
                      }if(StringUtils.hasText(authen.getCont())) {
                          auths.add(authen.getCont());
                          auths2.add(authen.getCont());
                      }if(StringUtils.hasText(user.getMbti())) {
                          auths.add(user.getMbti());
                          auths2.add(user.getMbti());
                      }if(StringUtils.hasText(user.getConstellation())) {
                          auths.add(user.getConstellation());
                      }
                  }
              }
            }
            vo.setMyHomeAuthenArray(auths2.toArray(new String[0]));
            vo.setAuthenArray(auths.toArray(new String[0]));

            StudentUser mys = ThreadLocalUtil.getStudentUser();
            if(mys!=null) {
                vo.setMySex(mys.getSex());
                vo.setMyAuthenCont(mys.getAuthCont());
                vo.setMyAuthenStatus(mys.getAuthStatus());
            }
            return vo;

        }
        return null;
    }




    @Override
    @Transactional
    public StudentUser saveOrUpdate(StudentSaveDto dto) {
        try {
            StudentUser user = ThreadLocalUtil.getStudentUser();
            if (user != null) {
                if(baseMapper.checkNickName(dto.getNickName(), ThreadLocalUtil.getStudentUserId())>0) {
                    throw new BizException(ComResponse.ERROR, "该昵称已被占用");
                }
                if(!dto.getHeadUrl().equals(user.getHeadUrl())) {
                    String head_oos_url = minioService.putBase64Image(MinioConstant.headImg, dto.getHeadUrl(), user.getUserName() + ".png");
                    if (StringUtils.hasText(head_oos_url)) {
                        user.setHeadUrl(head_oos_url);
                    }
                }

                if(StringUtils.hasText(dto.getNickName()) && !dto.getNickName().equals(user.getNickName())) {
                    WxCheckTextResp check_1 = wxRequest.wxCheckTextSecurity(user.getWxOpenId(), dto.getNickName(), "1");
                    if(check_1.getCode()!=100) {
                        throw new BizException(ComResponse.ERROR, "昵称违规："+check_1.getMsg());
                    }
                }
                if(StringUtils.hasText(dto.getIntro()) && !dto.getIntro().equals(user.getIntro())) {
                    WxCheckTextResp check_2 = wxRequest.wxCheckTextSecurity(user.getWxOpenId(), dto.getIntro(), "1");
                    if(check_2.getCode()!=100) {
                        throw new BizException(ComResponse.ERROR, "个人简介内容违规："+check_2.getMsg());
                    }
                }

                user.setNickName(dto.getNickName());
                user.setIntro(dto.getIntro());
                user.setBirthdate(dto.getBirthdate());
                user.setConstellation(dto.getConstellation());
                user.setSex(dto.getSex());
                if(StringUtils.hasText(dto.getMbti())) {
                    user.setMbti(dto.getMbti());
                }
                if(dto.getHobby()!=null && dto.getHobby().length > 0) {
                    String hobby = String.join(",", dto.getHobby());
                    user.setHobby(hobby);
                }
                baseMapper.updateById(user);
                return user;
            }
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("保存用户信息异常：{}", e.getMessage());
            throw new BizException(ComResponse.ERROR, "保存用户信息异常：" + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateUserAuthenById(String authId, String authCont, String authStatus, String userId) {
        baseMapper.updateUserAuthenById(authId, authCont, authStatus, userId);
    }

    @Override
    public boolean updateCategory(String id,String category){
        baseMapper.updateUserCategory(id,category);
        return true;
    }

    @Override
    public List<StudentUserVo> getByNameList(String name) {
        List<StudentUserVo> studentUserVoList = new ArrayList<>();
        List<StudentUser> userList = baseMapper.getByNameList(name);
        if(!userList.isEmpty()) {
            List<String> userIds = userList.stream().map(StudentUser::getId).toList();
            List<Authen> authenList = authenMapper.findAuthenByUserIds(userIds);

            for (StudentUser user : userList) {
                StudentUserVo vo = new StudentUserVo();
                user.setPassWord("");
                user.setWxSessionKey("");
                user.setWxOpenId("");
                vo.setStudentUser(user);

                for(Authen authen : authenList) {
                    if(user.getId().equals(authen.getUserId())) {
                        vo.setAuthen(authen);
                        if(ActivityConstant.student_authen_pass.equals(authen.getStatus())) {
                            vo.setAuthenArray(new String[]{authen.getSchoolName(), authen.getCont()});
                        } else {
                            vo.setAuthenArray(new String[]{});
                        }
                    }
                }
                studentUserVoList.add(vo);
            }
            return studentUserVoList;
        }
        return null;
    }

}
