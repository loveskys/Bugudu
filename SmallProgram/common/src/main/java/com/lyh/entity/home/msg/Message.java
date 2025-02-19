package com.lyh.entity.home.msg;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    //发送者user_id
    @TableField("send_user_id")
    private String sendUserId;

    //发送人头像
    @TableField("send_head_url")
    private String sendHeadUrl;

    //接收者user_id
    @TableField("receive_user_id")
    private String receiveUserId;

    //消息主体
    @TableField("msg_theme")
    private String msgTheme;

    //消息内容
    @TableField("msg_text")
    private String msgText;

    //消息类型：1官方消息，2官方给我单独发送的消息，3留言，4/5收藏，678活动
    @TableField("msg_type")
    private String msgType;

    //活动id
    @TableField("activity_id")
    private String activityId;

    //是否已推送：0未推送成功，1已推送成功
    @TableField("is_push")
    private String isPush;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

}
