package com.lyh.entity.home.msg;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

@Data
public class MessageVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    //发送者user_id
    private String sendUserId;

    //发送人头像
    private String sendHeadUrl;

    //接收者user_id
    private String receiveUserId;

    //消息主体
    private String msgTheme;

    //消息内容
    private String msgText;

    //消息类型：1官方消息，2官方给我单独发送的消息，3留言，4/5收藏，678活动
    private String msgType;

    //活动id
    private String activityId;

    //是否已推送：0未推送成功，1已推送成功
    private String isPush;

    //是否已读：0未读 1已读
    private String isRead;


    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;
}
