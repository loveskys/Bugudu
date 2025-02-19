package com.lyh.entity.home.msg;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class MyMessageVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //1官方消息，2官方给我单独发送的消息
    private List<MessageVo> officialList;


    //3留言，4收藏
    private List<MessageVo> commentsAndCollectList;
    //留言和收藏消息是否全部已读：0存在未读，1全部已读
    private boolean commentsAndCollectAllRead;


    //5活动消息
    private List<MessageVo> activityMsgList;
    //活动消息是否全部已读：0存在未读，1全部已读
    private boolean activityMsgAllRead;
}
