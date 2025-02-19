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
public class MessageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    //消息类型：1官方消息，2官方给我单独发送的消息，3留言，4/5收藏，678活动
    private String msgType;

    //发送者ID
    private String sendUserId;

    //接收者ID
    private String receiveUserId;

    //活动id
    private String activityId;

    private Integer pageCount;

    private Integer pageSize;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(msgType)) {
            msgType = MsgConstant.msg_type_official; //默认官方消息
        }
        return null;
    }

}
