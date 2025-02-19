package com.lyh.entity.comment;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动留言表
 */
@Data
@TableName("activity_leave")
public class ActivityLeave implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    //父id
    @TableField("parent_id")
    private String parentId;

    @TableField("activity_id")
    private String activityId;

    @TableField("send_user_id")
    private String sendUserId;

    @TableField("send_head_url")
    private String sendHeadUrl;

    @TableField("send_sex")
    private String sendSex;

    @TableField("send_nickname")
    private String sendNickname;

    //留言内容
    @TableField("message_cont")
    private String messageCont;

    //0没删除，1删除了
    @TableField("is_delete")
    private String isDelete;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
}
