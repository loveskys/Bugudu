package com.lyh.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.dao.MessageMapper;
import com.lyh.entity.constant.MsgConstant;
import com.lyh.entity.home.msg.*;
import com.lyh.service.IMessageService;
import com.lyh.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lyh
 * @since 2023-08-26
 */
@Slf4j
@Service
public class IMessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Override
    public MyMessageVo getMyMsgList(MessageDto dto) {
        List<MessageVo> originalList = baseMapper.getMyMsgList(dto.getMsgType(), ThreadLocalUtil.getStudentUserId());
        if(!originalList.isEmpty()) {
            MyMessageVo myMessageVo = new MyMessageVo();

            // 官方消息，官方给我单独发送的消息
            myMessageVo.setOfficialList(originalList.stream()
                    .filter(messageVo -> MsgConstant.msg_type_official.equals(messageVo.getMsgType())
                            || MsgConstant.msg_type_official_tome.equals(messageVo.getMsgType()))
                    .collect(Collectors.toList()));

            // 留言和收藏
            myMessageVo.setCommentsAndCollectList(originalList.stream().filter(messageVo ->
                            MsgConstant.msg_type_leave.equals(messageVo.getMsgType())
                            || MsgConstant.msg_type_collect.equals(messageVo.getMsgType())
                            || MsgConstant.msg_type_collect_cancel.equals(messageVo.getMsgType()))
                    .collect(Collectors.toList()));

            // 活动消息
            myMessageVo.setActivityMsgList(originalList.stream()
                    .filter(messageVo ->
                            MsgConstant.msg_type_activity_create.equals(messageVo.getMsgType())
                            || MsgConstant.msg_type_activity_apply.equals(messageVo.getMsgType())
                            || MsgConstant.msg_type_activity_apply_cancel.equals(messageVo.getMsgType())
                    )
                    .collect(Collectors.toList()));

            // 设置留言和收藏是否全部已读
            boolean commentsAndCollectAllRead = true;
            for (MessageVo messageVo : myMessageVo.getCommentsAndCollectList()) {
                if (MsgConstant.unread.equals(messageVo.getIsRead())) { // 如果有任何一条未读，则设置为非全部已读
                    commentsAndCollectAllRead = false;
                    break;
                }
            }
            myMessageVo.setCommentsAndCollectAllRead(commentsAndCollectAllRead);

            // 设置活动消息是否全部已读
            boolean activityMsgAllRead = true;
            for (MessageVo messageVo : myMessageVo.getActivityMsgList()) {
                if (MsgConstant.unread.equals(messageVo.getIsRead())) { // 如果有任何一条未读，则设置为非全部已读
                    activityMsgAllRead = false;
                    break;
                }
            }
            myMessageVo.setActivityMsgAllRead(activityMsgAllRead);
            return myMessageVo;
        }
        return null;
    }

    @Override
    public IPage<MessageVo> getPageList(MessageDto dto) {
        return baseMapper.getPageList(new Page<>(dto.getPageCount(), dto.getPageSize()), dto.getMsgType());
    }


    @Override
    @Transactional
    public void deleteMsg(MessageUpdateDto dto) {
            baseMapper.deleteById(dto.getId());
    }



    @Override
    @Transactional
    public void read(MessageUpdateDto dto) {
        String id = baseMapper.getRead(dto.getId(), ThreadLocalUtil.getStudentUserId());
        if(id==null) {
            baseMapper.insertRead(dto.getId(), ThreadLocalUtil.getStudentUserId());
        }
    }

    @Override
    @Transactional
    public void readAll(MessageDto dto) {
//        List<MessageVo> originalList =
//                baseMapper.getMyMsgList(dto.getMsgType(), ThreadLocalUtil.getStudentUserId());
//        for (MessageVo messageVo : originalList) {
//            String msgId = messageVo.getId();
//            String id = baseMapper.getRead(msgId, ThreadLocalUtil.getStudentUserId());
//            if(id==null) {
//                baseMapper.insertRead(msgId, ThreadLocalUtil.getStudentUserId());
//            }
//        }
        baseMapper.insertAllRead(ThreadLocalUtil.getStudentUserId());
    }

    @Override
    public void readAllComments(MessageDto dto) {
        List<MessageVo> originalList =
                baseMapper.getMyMsgList(dto.getMsgType(), ThreadLocalUtil.getStudentUserId());
        for (MessageVo messageVo : originalList) {
            if(MsgConstant.msg_type_leave.equals(messageVo.getMsgType())
                    || MsgConstant.msg_type_collect.equals(messageVo.getMsgType())
                    || MsgConstant.msg_type_collect_cancel.equals(messageVo.getMsgType())) {
                String msgId = messageVo.getId();
                String id = baseMapper.getRead(msgId, ThreadLocalUtil.getStudentUserId());
                if (id == null) {
                    baseMapper.insertRead(msgId, ThreadLocalUtil.getStudentUserId());
                }
            }
        }
    }

    @Override
    public void readAllActivity(MessageDto dto) {
        List<MessageVo> originalList =
                baseMapper.getMyMsgList(dto.getMsgType(), ThreadLocalUtil.getStudentUserId());
        for (MessageVo messageVo : originalList) {
            if(MsgConstant.msg_type_activity_create.equals(messageVo.getMsgType())
                    || MsgConstant.msg_type_activity_apply.equals(messageVo.getMsgType())
                    || MsgConstant.msg_type_activity_apply_cancel.equals(messageVo.getMsgType())) {
                String msgId = messageVo.getId();
                String id = baseMapper.getRead(msgId, ThreadLocalUtil.getStudentUserId());
                if (id == null) {
                    baseMapper.insertRead(msgId, ThreadLocalUtil.getStudentUserId());
                }
            }
        }
        System.out.println("activitysuccess");
    }


    @Override
    @Transactional
    public void sendOfficial(MessageSendDto dto) {
        Message message = new Message();
        message.setSendUserId(ThreadLocalUtil.getAdminUserId());
        message.setSendHeadUrl("");
        message.setMsgType(dto.getMsgType());
        message.setMsgTheme(dto.getMsgTheme());
        message.setMsgText(dto.getMsgText());
        message.setIsPush("1");
        save(message);
    }



    @Override
    @Transactional
    public void taskSend(MessageSendDto dto) {
        Message message = new Message();
        message.setSendUserId(dto.getSendUserId());
        message.setReceiveUserId(dto.getReceiveUserId());
        message.setSendHeadUrl(dto.getSendHeadUrl());
        message.setActivityId(dto.getActivityId());
        message.setMsgType(dto.getMsgType());
        message.setMsgTheme(dto.getMsgTheme());
        message.setMsgText(dto.getMsgText());
        message.setIsPush("1");
        save(message);
    }


}
