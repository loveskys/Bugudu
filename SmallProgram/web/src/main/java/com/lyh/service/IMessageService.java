package com.lyh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.entity.home.msg.*;

/**
 * @author lyh
 * @since 2024-08-26
 */
public interface IMessageService extends IService<Message> {

    MyMessageVo getMyMsgList(MessageDto dto);

    IPage<MessageVo> getPageList(MessageDto dto);

    void deleteMsg(MessageUpdateDto dto);

    void read(MessageUpdateDto dto);

    //pc端发送官方消息
    void sendOfficial(MessageSendDto dto);


    //事件触发发送通知消息
    void taskSend(MessageSendDto dto);

    void readAll(MessageDto dto);

    void readAllComments(MessageDto dto);

    void readAllActivity(MessageDto dto);
}
