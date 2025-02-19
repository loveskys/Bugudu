package com.lyh.entity.activity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动参与人员
 */
@Data
@TableName("activity_apply_person")
public class ActivityApplyPerson implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    //活动ID
    @TableField("activity_id")
    private String activityId;

    //是否发布人：0否，1是
    @TableField("is_issue")
    private String isIssue;

    //用户ID
    @TableField("user_id")
    private String userId;

    //用户头像URL
    @TableField("user_head_url")
    private String userHeadUrl;

    //用户名
    @TableField("username")
    private String userName;

    //称呼
    @TableField("nickname")
    private String nickName;

    //性别
    @TableField("sex")
    private String sex;

    //性别
    @TableField("grade")
    private String grade;

    //联系方式
    @TableField("contact")
    private String contact;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

}
