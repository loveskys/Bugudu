package com.lyh.entity.home.msg;

import com.lyh.entity.ComResponse;
import com.lyh.entity.constant.MsgConstant;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author lyh
 * @since 2024-08-26
 */
@Data
public class MessageSendDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //消息类型：1官方消息，2官方给我单独发送的消息，3留言，4/5收藏，678活动
    private String msgType;

    //消息主题
    private String msgTheme;

    //消息内容不超过1000字
    private String msgText;

    //发送者userid
    private String sendUserId;
    //发送者头像url
    private String sendHeadUrl;
    //接收者userid
    private String receiveUserId;

    //活动id
    private String activityId;



    public ComResponse<?> validate() {
        if (!StringUtils.hasText(msgType)) {
            msgType = MsgConstant.msg_type_official; //默认官方消息
        }
        if (!StringUtils.hasText(msgTheme)) {
            return new ComResponse<>().error("消息主体不能为空");
        }
        if (!StringUtils.hasText(msgText)) {
            return new ComResponse<>().error("消息内容不能为空");
        }
        return null;
    }

}
