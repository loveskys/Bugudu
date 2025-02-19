package com.lyh.entity.comment;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

@Data
public class ActivityLeaveVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String parentId;
    private String activityId;
    private String sendUserId;
    private String sendHeadUrl;
    private String sendSex;
    private String sendNickname;
    private String messageCont;
    private String isDelete;

    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;

}
