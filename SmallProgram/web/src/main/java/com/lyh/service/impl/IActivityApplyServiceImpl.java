package com.lyh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.dao.ActivityApplyMapper;
import com.lyh.dao.ActivityMapper;
import com.lyh.entity.BizException;
import com.lyh.entity.ComResponse;
import com.lyh.entity.activity.*;
import com.lyh.entity.activity.dto.*;
import com.lyh.entity.constant.ActivityConstant;
import com.lyh.entity.constant.MsgConstant;
import com.lyh.entity.contact.WxMaSubscribeMessage;
import com.lyh.entity.home.msg.MessageSendDto;
import com.lyh.entity.user.StudentUser;
import com.lyh.service.IActivityApplyPersonService;
import com.lyh.service.IActivityApplyService;
import com.lyh.service.IActivityService;
import com.lyh.service.IMessageService;
import com.lyh.service.IStudentUserService;
import com.lyh.util.ThreadLocalUtil;
import com.lyh.util.TimeUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;


/**
 * @author lyh
 * @since 2023-08-20
 */
@Slf4j
@Service
public class IActivityApplyServiceImpl extends ServiceImpl<ActivityApplyMapper, ActivityApply> implements IActivityApplyService {

    @Resource
    private IStudentUserService studentUserService;
    @Resource
    private IActivityApplyPersonService applyPersonService;
    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private IMessageService iMessageService;
    @Resource
    private IActivityService activityService;
    /**
     * 报名
     */
    @Override
    @Transactional
    public int apply(ActivityApplyDto dto) {


        StudentUser studentUser111 = studentUserService.getById(dto.getUserId());
        Activity activity111=activityService.getById(dto.getActivityId());
        System.out.println("wwwwwwwwwwwwwwwww");
        System.out.println(activity111.getTheme());
        System.out.println(TimeUtil.now());
        System.out.println(studentUser111.getNickName());
        System.out.println(studentUser111.getUserName());

        ActivityApply apply = baseMapper.getStatus(dto.getUserId(), dto.getActivityId());
        if(apply != null) {
            if(ActivityConstant.activity_apply_status_applying.equals(apply.getApplyStatus())) {
                throw new BizException(ComResponse.ERROR, "您已报名，请耐心等待");
            }
            if(ActivityConstant.activity_apply_status_pass.equals(apply.getApplyStatus())) {
                throw new BizException(ComResponse.ERROR, "您已通过报名");
            }
        }

        ActivityApply activityApply = new ActivityApply();
        activityApply.setUserId(ThreadLocalUtil.getStudentUserId());
        activityApply.setActivityId(dto.getActivityId());
        activityApply.setApplyStatus(ActivityConstant.activity_apply_status_pass); //直接通过报名
        activityApply.setApplyTime(TimeUtil.now());
        if(save(activityApply)) {
            StudentUser studentUser = studentUserService.getById(dto.getUserId());
            if(studentUser != null) {
                ActivityApplyPerson activityApplyPerson = new ActivityApplyPerson();
                activityApplyPerson.setUserId(dto.getUserId());
                activityApplyPerson.setUserHeadUrl(studentUser.getHeadUrl());
                activityApplyPerson.setNickName(studentUser.getUserName());
                activityApplyPerson.setActivityId(dto.getActivityId());
                activityApplyPerson.setIsIssue(ActivityConstant.unIssue);
                activityApplyPerson.setNickName(dto.getNickName());
                activityApplyPerson.setContact(dto.getContact());
                activityApplyPerson.setSex(studentUser.getSex());
                activityApplyPerson.setGrade(studentUser.getAuthCont());

                if(applyPersonService.save(activityApplyPerson)) {
                    if(ActivityConstant.student_authen_pass.equals(studentUser.getAuthStatus())) { //报名成功
                        //订阅消息发送


                        ArrayList<WxMaSubscribeMessage.MsgData> list=new ArrayList<>();

                        WxMaSubscribeMessage wxMaSubscribeMessage=new WxMaSubscribeMessage();
                        WxMaSubscribeMessage.MsgData msgData1 = wxMaSubscribeMessage.new MsgData();

                        //获取活动信息
                        Activity activity=activityService.getById(dto.getActivityId());

                        msgData1.setName("thing1");
                        msgData1.setValue(activity.getTheme());
                        WxMaSubscribeMessage.MsgData msgData2 = wxMaSubscribeMessage.new MsgData();
                        msgData2.setName("time3");
                        msgData2.setValue(TimeUtil.now());
                        WxMaSubscribeMessage.MsgData msgData3 = wxMaSubscribeMessage.new MsgData();
                        msgData3.setName("thing2");
                        msgData3.setValue(studentUser.getNickName());

                        list.add(msgData1);
                        list.add(msgData2);
                        list.add(msgData3);
                        wxMaSubscribeMessage.setData(list);
                        wxMaSubscribeMessage.setTemplateID("tD-yfkb6-1oAal7f451_KgMdjwG93SsseiVPoUCtFT8");
                        //获取发起者Openid
                        StudentUser studentUser1=studentUserService.getById(activity.getIssue());
                        wxMaSubscribeMessage.setToUser(studentUser1.getUserName());

                        //活动报名成功-给发布人-发送通知
                        toMsg(dto, MsgConstant.msg_type_activity_apply, dto.getContact());
                        return 2;
                    } else {
                        //还没通过学生认证
                        return 1;
                    }
                }
            }
        }
        return 0;
    }


    /**
     * 取消报名
     */
    @Override
    @Transactional
    public void applyCancel(ActivityApplyDto dto) {
        baseMapper.deleteApplyInfo(dto.getUserId(), dto.getActivityId());
        baseMapper.deleteApplyPersonInfo(dto.getUserId(), dto.getActivityId());

        //活动取消报名成功-给发布人-发送通知
        toMsg(dto, MsgConstant.msg_type_activity_apply_cancel, dto.getContact());
    }


    //报名后发通知
    private void toMsg(ActivityApplyDto dto, String msgType, String contact) {
        Activity activity = activityMapper.selectById(dto.getActivityId());
        if(activity!=null) {
            StudentUser studentUser = studentUserService.getById(dto.getUserId());
            MessageSendDto sendDto = new MessageSendDto();
            sendDto.setReceiveUserId(activity.getIssue());
            sendDto.setMsgTheme(studentUser.getNickName());
            sendDto.setMsgText(activity.getTheme() + (contact!=null?"，联系方式："+contact:""));
            sendDto.setMsgType(msgType);
            sendDto.setActivityId(dto.getActivityId());
            iMessageService.taskSend(sendDto);
        }

    }


}


