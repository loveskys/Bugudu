package com.lyh.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyh.dao.*;
import com.lyh.entity.BizException;
import com.lyh.entity.ComResponse;
import com.lyh.entity.activity.*;
import com.lyh.entity.activity.dto.*;
import com.lyh.entity.comment.ActivityLeave;
import com.lyh.entity.comment.ActivityLeaveVo;
import com.lyh.entity.constant.ActivityConstant;
import com.lyh.entity.constant.MinioConstant;
import com.lyh.entity.constant.MsgConstant;
import com.lyh.entity.home.msg.MessageSendDto;
import com.lyh.entity.user.StudentUser;
import com.lyh.entity.wxresponse.WxCheckTextResp;
import com.lyh.service.*;
import com.lyh.util.ThreadLocalUtil;
import com.lyh.util.TimeUtil;
import com.lyh.util.TokenUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lyh
 * @since 2023-08-17
 */
@Slf4j
@Service
public class IActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Resource
    private MinioService minioService;
    @Resource
    private IStudentUserService studentUserService;
    @Resource
    private IActivityApplyPersonService applyPersonService;
    @Resource
    private ActivityCollectMapper activityCollectMapper;
    @Resource
    private ActivityApplyPersonMapper applyPersonMapper;
    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private ActivityLeaveMapper activityLeaveMapper;
    @Resource
    private ActivityCheckimgMapper activityCheckimgMapper;
    @Resource
    private IMessageService iMessageService;
    @Resource
    private WxRequest wxRequest;

    private String activityIdTemp;

    @Override
    @Transactional
    public boolean originate(MultipartFile file,String activityInfo) {
        ActivityDto dto;
        StudentUser studentUser = studentUserService.getById(ThreadLocalUtil.getStudentUserId());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            dto = objectMapper.readValue(activityInfo, ActivityDto.class);
        } catch (JsonProcessingException e) {
            throw new BizException(ComResponse.ERROR,"ActivityDto-json解析失败！");
        }
        if(dto.validate()!=null) {
            return false;
        }
        Activity activity = new Activity();
        activity.setTheme(dto.getTheme());

        WxCheckTextResp check_1 = wxRequest.wxCheckTextSecurity(studentUser.getWxOpenId(), dto.getTheme(), "2");
        if(check_1.getCode()!=100) {
            throw new BizException(ComResponse.ERROR, "活动主题违规："+check_1.getMsg());
        }

        activity.setIntro(dto.getIntro());
        WxCheckTextResp check_2 = wxRequest.wxCheckTextSecurity(studentUser.getWxOpenId(), dto.getIntro(), "2");
        if(check_2.getCode()!=100) {
            throw new BizException(ComResponse.ERROR, "活动描述内容违规："+check_2.getMsg());
        }

        activity.setCategory(dto.getCategory());
        activity.setContactType(dto.getContactType());
        activity.setContact(dto.getContact());
        if(!(dto.getContact() == null || dto.getContact().isEmpty()))
        {
            WxCheckTextResp check_3 = wxRequest.wxCheckTextSecurity(studentUser.getWxOpenId(), dto.getContact(), "1");
            if(check_3.getCode()!=100) {
                throw new BizException(ComResponse.ERROR, "联系方式填写违规："+check_3.getMsg());
            }
        }

        if(StringUtils.hasText(dto.getLocation().getAddress())) {
            WxCheckTextResp check_4 = wxRequest.wxCheckTextSecurity(studentUser.getWxOpenId(), dto.getLocation().getAddress(), "1");
            if(check_4.getCode()!=100) {
                throw new BizException(ComResponse.ERROR, "地址填写违规："+check_4.getMsg());
            }
        }

        activity.setActivityStart(LocalDateTime.parse(dto.getStartDate()+" "+dto.getStartTime(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        activity.setActivityEnd(LocalDateTime.parse(dto.getEndDate()+" "+dto.getEndTime(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        activity.setIssue(ThreadLocalUtil.getStudentUserId());
        activity.setIsDelete(ActivityConstant.unDelete);
        activity.setAuditStatus(ActivityConstant.activity_originate_status_pass);
        activity.setAuditCont("等待微信图片审核通过");
        activity.setRecommend(ActivityConstant.unRecommend);
        activity.setAuditTime(LocalDateTime.now());
//        activity.setImageType(dto.getImageType());
        String image = dto.getImage();
        activity.setImage(image);
        if (file != null) {
            image = minioService.putObject(MinioConstant.activityImg, file,
                    ThreadLocalUtil.getStudentUserId() + TokenUtil.getuid32() + ".png");
            if (StringUtils.hasText(image)) {
                activity.setImage(image);
            }
        }
        if (!StringUtils.hasText(activity.getImage())) {
            throw new BizException(ComResponse.ERROR, "请上传活动图片！");
        }
//        if(dto.getImageType().equals(ActivityConstant.image_type_url)) {
            activity.setWxUrl(dto.getWxUrl());
//            if(!StringUtils.hasText(activity.getWxUrl())) {
//                throw new BizException(ComResponse.ERROR, "请上传活动公众号链接！");
//            }
//        }

        if(StringUtils.hasText(dto.getId())) { //更新
            activity.setId(dto.getId());
            baseMapper.updateById(activity);
            baseMapper.updateActCondit(activity.getId(), dto.getCondit().getPeopleNum(),
                    dto.getCondit().getSex(), dto.getCondit().getGrade(), dto.getCondit().getOther());
            baseMapper.updateActLocation(activity.getId(), dto.getLocation());
            baseMapper.updateActImg(activity.getId(), image, "student-"+ThreadLocalUtil.getStudentUserId(), LocalDateTime.now());
            wxRequest.wxCheckPicSecurity("2", dto.getId(), studentUser.getWxOpenId(), activity.getImage());
            return true;
        } else {//新发布
            if(save(activity)) {
                if(studentUser != null &&
                        (ActivityConstant.student_authen_ing.equals(studentUser.getAuthStatus()) ||
                                ActivityConstant.student_authen_pass.equals(studentUser.getAuthStatus()))) {
                    ActivityApplyPerson activityApplyPerson = new ActivityApplyPerson();
                    activityApplyPerson.setUserId(ThreadLocalUtil.getStudentUserId());
                    activityApplyPerson.setActivityId(activity.getId());
                    activityApplyPerson.setIsIssue(ActivityConstant.IsIssue);
                    activityApplyPerson.setNickName(studentUser.getNickName());
                    activityApplyPerson.setUserName(studentUser.getUserName());
                    activityApplyPerson.setUserHeadUrl(studentUser.getHeadUrl());
                    activityApplyPerson.setContact(activity.getContactType() + "："+ activity.getContact());
                    activityApplyPerson.setSex(studentUser.getSex());
                    activityApplyPerson.setGrade(studentUser.getAuthCont());
                    applyPersonService.save(activityApplyPerson);
                } else {
                    throw new BizException(ComResponse.ERROR, "您的学生认证未通过");
                }

                activityIdTemp=activity.getId();

                baseMapper.saveActCondit(activity.getId(), dto.getCondit().getPeopleNum(),
                        dto.getCondit().getSex(), dto.getCondit().getGrade(), dto.getCondit().getOther());
                dto.getLocation().setActivityId(activity.getId());
                baseMapper.saveActLocation(dto.getLocation());
                baseMapper.saveActImg(activity.getId(), image,
                            "student-" + ThreadLocalUtil.getStudentUserId(), LocalDateTime.now());
                wxRequest.wxCheckPicSecurity("2", activity.getId(), studentUser.getWxOpenId(), activity.getImage());
                return true;
            }
        }
        return false;
    }


    @Override
    @Transactional
    public boolean originateContact(MultipartFile file,String activityInfo) {
        System.out.println("进入联系方式上传方法");
        ActivityDto dto;
        StudentUser studentUser = studentUserService.getById(ThreadLocalUtil.getStudentUserId());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            dto = objectMapper.readValue(activityInfo, ActivityDto.class);
        } catch (JsonProcessingException e) {
            throw new BizException(ComResponse.ERROR,"ActivityDto-json解析失败！");
        }
        if(dto.validate()!=null) {
            return false;
        }
        System.out.println("ActivityDto-json解析成功");
        Activity activity = new Activity();
        String contact_image = dto.getContact_image();
        System.out.println("contact_image成功获取");
        activity.setContact_image(contact_image);
        if(file!=null) {
            contact_image = minioService.putObject(MinioConstant.contactImg, file,
                    ThreadLocalUtil.getStudentUserId()+ TokenUtil.getuid32()+".png");
            if (StringUtils.hasText(contact_image)) {
                activity.setContact_image(contact_image);
            }
        }
        System.out.println("contact_image成功存入minIO");
        System.out.println(dto);

        if(StringUtils.hasText(activityIdTemp)) { //更新
            baseMapper.updateContactImg(activityIdTemp, contact_image);
            wxRequest.wxCheckPicSecurity("3", activityIdTemp, studentUser.getWxOpenId(), activity.getContact_image());
            System.out.println("成功通过微信图片审核");
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean reOriginate(ActivityDto dto) {
        Activity activity = new Activity();
        StudentUser studentUser = studentUserService.getById(ThreadLocalUtil.getStudentUserId());
        activity.setTheme(dto.getTheme());

        WxCheckTextResp check_1 = wxRequest.wxCheckTextSecurity(studentUser.getWxOpenId(), dto.getTheme(), "2");
        if(check_1.getCode()!=100) {
            throw new BizException(ComResponse.ERROR, "活动主题违规："+check_1.getMsg());
        }

        activity.setIntro(dto.getIntro());
        WxCheckTextResp check_2 = wxRequest.wxCheckTextSecurity(studentUser.getWxOpenId(), dto.getIntro(), "2");
        if(check_2.getCode()!=100) {
            throw new BizException(ComResponse.ERROR, "活动描述内容违规："+check_2.getMsg());
        }


        activity.setCategory(dto.getCategory());
        activity.setContactType(dto.getContactType());
        activity.setContact(dto.getContact());
        WxCheckTextResp check_3 = wxRequest.wxCheckTextSecurity(studentUser.getWxOpenId(), dto.getContact(), "1");
        if(check_3.getCode()!=100) {
            throw new BizException(ComResponse.ERROR, "联系方式填写违规："+check_3.getMsg());
        }

        if(StringUtils.hasText(dto.getLocation().getAddress())) {
            WxCheckTextResp check_4 = wxRequest.wxCheckTextSecurity(studentUser.getWxOpenId(), dto.getLocation().getAddress(), "1");
            if(check_4.getCode()!=100) {
                throw new BizException(ComResponse.ERROR, "地址填写违规："+check_4.getMsg());
            }
        }
//        activity.setImageType(dto.getImageType());
//        if (dto.getImageType().equals(ActivityConstant.image_type_url))
            activity.setWxUrl(dto.getWxUrl());
        activity.setImage(dto.getImage());
        activity.setActivityStart(LocalDateTime.parse(dto.getStartDate()+" "+dto.getStartTime(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        activity.setActivityEnd(LocalDateTime.parse(dto.getEndDate()+" "+dto.getEndTime(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        activity.setIssue(ThreadLocalUtil.getStudentUserId());
        activity.setIsDelete(ActivityConstant.unDelete);
        activity.setAuditStatus(ActivityConstant.activity_originate_status_pass);
        activity.setAuditCont("重发布自动通过");
        activity.setRecommend(ActivityConstant.unRecommend);
        activity.setAuditTime(LocalDateTime.now());

        if(StringUtils.hasText(dto.getId())) { //修改活动
            activity.setId(dto.getId());
            baseMapper.updateById(activity);
            baseMapper.updateActCondit(activity.getId(), dto.getCondit().getPeopleNum(),
                    dto.getCondit().getSex(), dto.getCondit().getGrade(), dto.getCondit().getOther());
            baseMapper.updateActLocation(activity.getId(), dto.getLocation());
            baseMapper.updateActImg(activity.getId(), dto.getImage(),
                        "student-" + ThreadLocalUtil.getStudentUserId(), LocalDateTime.now());
            return true;
        } else {  //新发布
//            if(save(activity)) {
//                if(studentUser != null && (ActivityConstant.student_authen_ing.equals(studentUser.getAuthStatus()) || ActivityConstant.student_authen_pass.equals(studentUser.getAuthStatus()))) {
//                    ActivityApplyPerson activityApplyPerson = new ActivityApplyPerson();
//                    activityApplyPerson.setUserId(ThreadLocalUtil.getStudentUserId());
//                    activityApplyPerson.setActivityId(activity.getId());
//                    activityApplyPerson.setIsIssue(ActivityConstant.IsIssue);
//                    activityApplyPerson.setNickName(studentUser.getNickName());
//                    activityApplyPerson.setUserName(studentUser.getUserName());
//                    activityApplyPerson.setUserHeadUrl(studentUser.getHeadUrl());
//                    activityApplyPerson.setContact(activity.getContactType() + "："+ activity.getContact());
//                    activityApplyPerson.setSex(studentUser.getSex());
//                    activityApplyPerson.setGrade(studentUser.getAuthCont());
//                    applyPersonService.save(activityApplyPerson);
//                } else {
//                    throw new BizException(ComResponse.ERROR, "您的学生认证未通过");
//                }
//                baseMapper.saveActCondit(activity.getId(), dto.getCondit().getPeopleNum(),  dto.getCondit().getSex(), dto.getCondit().getGrade(), dto.getCondit().getOther());
//                dto.getLocation().setActivityId(activity.getId());
//                baseMapper.saveActLocation(dto.getLocation());
//                baseMapper.saveActImg(activity.getId(), dto.getImage(), "student-"+ThreadLocalUtil.getStudentUserId(), LocalDateTime.now());
//
//                //活动发布成功-发送通知
//                MessageSendDto sendDto = new MessageSendDto();
//                sendDto.setReceiveUserId(ThreadLocalUtil.getStudentUserId());
//                sendDto.setMsgTheme(MsgConstant.official_theme);
//                sendDto.setMsgText("您的活动["+activity.getTheme()+"]发布成功，赶快召集小伙伴参加吧，点击查看详情~");
//                sendDto.setMsgType(MsgConstant.msg_type_activity_create);
//                sendDto.setActivityId(activity.getId());
//                iMessageService.taskSend(sendDto);
//                return true;
//            }
        }
        return false;
    }


    @Override
    public IPage<ActivityVo> pageList(ActivityPageListDto dto) {
        IPage<ActivityVo> pageVo = baseMapper.getPageList(new Page<>(dto.getPageCount(), dto.getPageSize()), dto.getTheme(), dto.getTag(), dto.getWxCheck());
        if(pageVo.getTotal()>0) {
            List<ActivityVo> voList = pageVo.getRecords();
            List<ActivityVo> list = new ArrayList<>();
            for (ActivityVo activityVo : voList) {
                List<ActivityApplyPerson> applyPersonList = applyPersonMapper.getApplyPersonList(activityVo.getId());
                activityVo.setApplyPeople(applyPersonList);
                list.add(setLandC(activityVo));
            }
            pageVo.setRecords(list);
            return pageVo;
        }
        return null;
    }



    @Override
    public List<ActivityVo> getByThemeList(String theme) {
        List<ActivityVo> voList = baseMapper.getByThemeList(theme);
        List<ActivityVo> list = new ArrayList<>();
        if(!voList.isEmpty()) {
            for (ActivityVo activityVo : voList) {
                List<ActivityApplyPerson> applyPersonList = applyPersonMapper.getApplyPersonList(activityVo.getId());
                activityVo.setApplyPeople(applyPersonList);
                list.add(setLandC(activityVo));
            }
            return list;
        }
        return null;
    }


    @Override
    @Transactional
    public void updateActivityAudit(ActivityAuditDto dto) {
        baseMapper.updateActivityAudit(dto.getId(), dto.getStatus(), dto.getCont());
    }

    @Override
    @Transactional
    public void delete(ActivityDeleteDto dto) {
       baseMapper.updateActivityValid(dto.getId());
    }


    @Override
    public ActivityVo pc_detail(ActivityDetailDto dto) {
        ActivityVo activityVo = baseMapper.pc_detail(dto.getId());
        if(activityVo!=null) {
            List<ActivityApplyPerson> applyPersonList = applyPersonMapper.getApplyPersonList(dto.getId());
            activityVo.setApplyPeople(applyPersonList);
            return setLandC(activityVo);
        }
        return null;
    }


    @Override
    public ActivityVo detail(ActivityMyListDto dto) {
        ActivityVo activityVo = baseMapper.detail(dto.getId(), ThreadLocalUtil.getStudentUserId());
        if(activityVo!=null) {
            if(ActivityConstant.wx_pic_check_nopass.equals(activityVo.getWxImgCheck())) {
                activityVo.setImage("");
            }
            List<ActivityApplyPerson> applyPersonList = applyPersonMapper.getApplyPersonList(dto.getId());
            activityVo.setApplyPeople(applyPersonList);
            return setLandC(activityVo);
        }
        return null;
    }


    @Override
    @Transactional
    public void collect(ActivityCollectDto dto) {
        ActivityCollect collect = activityCollectMapper.getCollect(ThreadLocalUtil.getStudentUserId(), dto.getActivityId());
        if(collect!=null) {
            //取消收藏
            activityCollectMapper.deleteById(collect.getId());
            //取消收藏活动-发送通知
            toMsg(dto, MsgConstant.msg_type_collect_cancel);
        } else {
            //收藏
            ActivityCollect activityCollect = new ActivityCollect();
            activityCollect.setUserId(ThreadLocalUtil.getStudentUserId());
            activityCollect.setActivityId(dto.getActivityId());
            activityCollectMapper.insert(activityCollect);
            //收藏活动-发送通知
            toMsg(dto, MsgConstant.msg_type_collect);
        }
    }


    private void toMsg(ActivityCollectDto dto, String msgType) {
        Activity activity = baseMapper.selectById(dto.getActivityId());
        if(activity!=null) {
            StudentUser studentUser = studentUserService.getById(ThreadLocalUtil.getStudentUserId());
            //收藏/取消收藏活动-发送通知
            MessageSendDto sendDto = new MessageSendDto();
            sendDto.setSendUserId(ThreadLocalUtil.getStudentUserId());
            sendDto.setSendHeadUrl(studentUser.getHeadUrl());
            sendDto.setReceiveUserId(activity.getIssue());
            sendDto.setMsgType(msgType);
            sendDto.setMsgTheme(studentUser.getNickName());
            sendDto.setMsgText(activity.getTheme());
            sendDto.setActivityId(dto.getActivityId());
            iMessageService.taskSend(sendDto);
        }
    }


    @Override
    @Transactional
    public void recommend(ActivityRSDto dto) {
        Activity activity = baseMapper.selectById(dto.getId());
        if(activity != null && ActivityConstant.IsRecommend.equals(activity.getRecommend())) {
            //取消推荐
            baseMapper.updateByIdRecommend(dto.getId(), ActivityConstant.unRecommend);
            //发送通知
//            toMsg(dto, MsgConstant.msg_type_collect_cancel);
        } else {
            baseMapper.updateByIdRecommend(dto.getId(), ActivityConstant.IsRecommend);
            //推荐活动-发送通知
//            toMsg(dto, MsgConstant.msg_type_collect);
        }
    }


    @Override
    public List<ActivityVo> getUserActivityList(ActivityMyListDto dto) {
        //1我报名参与的，2我发布的，3我的收藏
        if("1".equals(dto.getTag())) {
            List<ActivityVo> voList = baseMapper.getUserActivityList(ThreadLocalUtil.getStudentUserId(), null, null);
            for (ActivityVo activityVo : voList) {
                List<ActivityApplyPerson> applyPersonList = applyPersonMapper.getApplyPersonList(activityVo.getId());
                activityVo.setApplyPeople(applyPersonList);
                setLandC(activityVo);
            }
            return voList;
        }
        if("2".equals(dto.getTag())) {
            List<ActivityVo> voList = baseMapper.getUserActivityList(null,ThreadLocalUtil.getStudentUserId(), null);
            for (ActivityVo activityVo : voList) {
                List<ActivityApplyPerson> applyPersonList = applyPersonMapper.getApplyPersonList(activityVo.getId());
                activityVo.setApplyPeople(applyPersonList);
                setLandC(activityVo);
            }
            return voList;
        }
        if("3".equals(dto.getTag())) {
            List<ActivityVo> voList = baseMapper.getUserActivityList(null,null, ThreadLocalUtil.getStudentUserId());
            for (ActivityVo activityVo : voList) {
                List<ActivityApplyPerson> applyPersonList = applyPersonMapper.getApplyPersonList(activityVo.getId());
                activityVo.setApplyPeople(applyPersonList);
                setLandC(activityVo);
            }
            return voList;
        }

        //查看他人主页时
        if("1".equals(dto.getOther()) && StringUtils.hasText(dto.getOtherUserId())) {
            List<ActivityVo> voList = baseMapper.getUserActivityList(dto.getOtherUserId(), null, null);
            for (ActivityVo activityVo : voList) {
                List<ActivityApplyPerson> applyPersonList = applyPersonMapper.getApplyPersonList(activityVo.getId());
                activityVo.setApplyPeople(applyPersonList);
                setLandC(activityVo);
            }
            return voList;
        }
        return null;
    }


    //todo
    //首页活动类型需要添加
    @Override
    public List<ActivityVo> getHomeActivityList(String tag) {
        String category = null, recommend = null;
        if(tag.equals("0")) {
            recommend = ActivityConstant.IsRecommend;
        } if(tag.equals("1")) {
            category = "娱乐";
        } if(tag.equals("2")) {
            category = "公益";
        } if(tag.equals("3")) {
            category = "运动";
        } if(tag.equals("4")) {
            category = "学习";
        }
        List<ActivityVo> voList = baseMapper.getHomeActivityList(category, recommend);
        for (ActivityVo activityVo : voList) {
            List<ActivityApplyPerson> applyPersonList = applyPersonMapper.getApplyPersonList(activityVo.getId());
            activityVo.setApplyPeople(applyPersonList);
            setLandC(activityVo);
        }
        return voList;
    }


//    @Override
//    public List<Swiper> getSwiperActivityList(Integer num) {
//        List<Swiper> list = baseMapper.getSwiperList(num);
//        list.addAll(loopImageMapper.getSwiperList(num));
//        return list;
//    }


    //处理list数据
    private ActivityVo setLandC(ActivityVo activityVo) {
        if (activityVo.getActivityStart().length() >= 17 && activityVo.getActivityEnd().length() >= 17){
            if(TimeUtil.isNowPastDate(activityVo.getActivityEnd())) {
                activityVo.setIsPast("1");
            }
            activityVo.setActivityStart(activityVo.getActivityStart().substring(0, 16));
            activityVo.setActivityEnd(activityVo.getActivityEnd().substring(0, 16));
        }

        ActivityLocation location = new ActivityLocation();
        if (StringUtils.hasText(activityVo.getLocationName())) {
            location.setLocationName(activityVo.getLocationName());
        } if (StringUtils.hasText(activityVo.getLocationAddress())) {
            location.setLocationAddress(activityVo.getLocationAddress());
        } if (StringUtils.hasText(activityVo.getLongitude())) {
            location.setLongitude(activityVo.getLongitude());
        } if (StringUtils.hasText(activityVo.getLatitude())) {
            location.setLatitude(activityVo.getLatitude());
        } if (StringUtils.hasText(activityVo.getAddress())) {
            location.setAddress(activityVo.getAddress());
        }
        activityVo.setLocation(location);

        ActivityCondit condit = new ActivityCondit();
        if (StringUtils.hasText(activityVo.getPeopleNum())) {
            condit.setPeopleNum(activityVo.getPeopleNum());
        } if (StringUtils.hasText(activityVo.getConditSex())) {
            condit.setSex(activityVo.getConditSex());
        } if (StringUtils.hasText(activityVo.getConditGrade())) {
            condit.setGrade(activityVo.getConditGrade());
        } if (StringUtils.hasText(activityVo.getConditOther())) {
            condit.setOther(activityVo.getConditOther());
        }
        activityVo.setCondit(condit);

        List<String> conditArray = new ArrayList<>();
        if (StringUtils.hasText(activityVo.getPeopleNum())) {
            conditArray.add(activityVo.getPeopleNum() + "人");
        } if (StringUtils.hasText(activityVo.getConditSex())) {
            conditArray.add(activityVo.getConditSex());
        } if (StringUtils.hasText(activityVo.getConditGrade())) {
            conditArray.add(activityVo.getConditGrade());
        } if (StringUtils.hasText(activityVo.getConditOther())) {
            conditArray.add(activityVo.getConditOther());
        }
        activityVo.setConditArray(conditArray.toArray(new String[0]));

        //默认不是发布人，可报名，不可取消
        activityVo.setItSme("0");
        activityVo.setIsApply("1");
        activityVo.setIsCancelApply("0");
        //如果我是发布人，设置为1，不可报名，不可取消
        if(Objects.equals(ThreadLocalUtil.getStudentUserId(), activityVo.getIssue())) {
            activityVo.setItSme("1");
            activityVo.setIsApply("0");
            activityVo.setIsCancelApply("0");
        }

        // 处理已报名人员的头像和用户ID
        String nullHeadUrl = "/images/my/nullhead.png";
        List<ActivityApplyPerson> applyPeople = activityVo.getApplyPeople();
        List<DetailApplyPersonVO> enrolledDetails = new ArrayList<>();
        // 获取活动总人数
        int peopleNum = Integer.parseInt(activityVo.getCondit().getPeopleNum());
        int enrolledCount = 0;
        // 遍历已报名的人员列表
        if (applyPeople != null) {
            for (ActivityApplyPerson person : applyPeople) {
                if (person != null) {
                    if(person.getIsIssue().equals("0") && person.getUserId().equals(ThreadLocalUtil.getStudentUserId())) {
                        activityVo.setIsApply("0"); //不是发布人，已经报名了，设置为不可报名；同时可以取消报名
                        activityVo.setIsCancelApply("1");
                    }
                    DetailApplyPersonVO detail = new DetailApplyPersonVO();
                    detail.setUserId(person.getUserId());
                    if(person.getUserHeadUrl()!=null){
                        detail.setUserHeadUrl(person.getUserHeadUrl());
                    } else {
                        detail.setUserHeadUrl(nullHeadUrl);
                    }
                    detail.setSex(person.getSex());
                    detail.setIsIssue(person.getIsIssue());
                    if(person.getIsIssue().equals(ThreadLocalUtil.getStudentUserId())) {
                        detail.setItSme("1");
                    } else {
                        detail.setItSme("0");
                    }
                    enrolledDetails.add(detail);
                    enrolledCount++;
                }
            }
        }
        // 计算还需填充多少个位置
        int remainingSlots = peopleNum - enrolledCount;
        while (remainingSlots > 0 && enrolledDetails.size() < 30) {
            DetailApplyPersonVO detail = new DetailApplyPersonVO();
            detail.setUserHeadUrl(nullHeadUrl);
            enrolledDetails.add(detail);
            remainingSlots--;
            if (enrolledDetails.size() >= 30) {
                break;
            }
        }
        // 设置已报名人数与总人数的比例
        activityVo.setEnrolledRatio(enrolledCount + "/" + peopleNum);
        activityVo.setEnrolledDetails(enrolledDetails);
        activityVo.setAtFull("0");
        if(enrolledCount==peopleNum) {
            activityVo.setAtFull("1");
        }
        return activityVo;
    }


    @Override
    public List<ActivityLeaveVo> get_leaves(ActivityLeaveDto dto){
        return activityLeaveMapper.get_leaves(dto.getActivityId());
    }

    @Override
    @Transactional
    public void send_leaves(ActivityLeaveDto dto) {
        StudentUser studentUser = studentUserService.getById(ThreadLocalUtil.getStudentUserId());
        if(ActivityConstant.student_authen_pass.equals(studentUser.getAuthStatus())) {
            ActivityLeave leave = new ActivityLeave();
            leave.setIsDelete("0");
            leave.setSendNickname(studentUser.getNickName());
            leave.setSendUserId(ThreadLocalUtil.getStudentUserId());
            leave.setSendSex(studentUser.getSex());
            leave.setSendHeadUrl(studentUser.getHeadUrl());
            leave.setActivityId(dto.getActivityId());
            leave.setMessageCont(dto.getLeaveCont());

            if(StringUtils.hasText(dto.getLeaveCont())) {
                WxCheckTextResp check_1 = wxRequest.wxCheckTextSecurity(studentUser.getWxOpenId(), dto.getLeaveCont(), "2");
                if(check_1.getCode()!=100) {
                    throw new BizException(ComResponse.ERROR, "留言内容违规："+check_1.getMsg());
                }
            }
            activityLeaveMapper.insert(leave);

            //留言成功发送通知消息
            Activity activity = getById(dto.getActivityId());
            if(activity != null && !Objects.equals(ThreadLocalUtil.getStudentUserId(), activity.getIssue())) {
                MessageSendDto sendDto = new MessageSendDto();
                sendDto.setReceiveUserId(activity.getIssue());
                sendDto.setMsgTheme(studentUser.getNickName());
                sendDto.setMsgText(activity.getTheme());
                sendDto.setMsgType(MsgConstant.msg_type_leave);
                sendDto.setActivityId(dto.getActivityId());
                iMessageService.taskSend(sendDto);
            }
        } else {
            throw new BizException(ComResponse.ERROR, "您还未认证");
        }
    }

    @Override
    @Transactional
    public void delete_leaves(ActivityLeaveDto dto) {
        ActivityLeave activityLeave = activityLeaveMapper.selectById(dto.getId());
        Activity activity = activityMapper.selectById(activityLeave.getActivityId());
        //当前用户为留言发布者或活动发布者可删除留言
        if(Objects.equals(ThreadLocalUtil.getStudentUserId(), activityLeave.getSendUserId())
            ||Objects.equals(ThreadLocalUtil.getStudentUserId(), activity.getIssue())) {
            activityLeaveMapper.deleteById(dto.getId());
        }
        else throw new BizException(ComResponse.ERROR, "没有权限删除");
    }



    @Override
    public List<ActivityApplyPerson> getActivityApplicantsList(String dto) {
        List<ActivityApplyPerson> applyPersonList = applyPersonMapper.getApplyPersonList(dto);

        return applyPersonList;
    }

    //上传审核表
    @Override
    public void updateActivityCheckImg(ActivityCheckimg dto){
        ActivityCheckimg activityCheckimg = activityCheckimgMapper.getCheckimg(dto.getActivityId());
        if (activityCheckimg != null) {
            activityCheckimgMapper.updateCheckimgurl(dto.getActivityId(),dto.getCheckimgUrl());
        }else {
            activityCheckimgMapper.insert(dto);
        }
    }

    //获取审核表
    @Override
    public String getActivityCheckImg(String activity_id){
        ActivityCheckimg activityCheckimg = activityCheckimgMapper.getCheckimg(activity_id);
        if (activityCheckimg!= null) {

            return activityCheckimg.getCheckimgUrl();
        }
        return null;

    }

}
