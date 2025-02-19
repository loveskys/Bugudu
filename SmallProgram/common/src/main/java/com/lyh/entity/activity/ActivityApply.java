package com.lyh.entity.activity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

//活动报名表
@Data
@TableName("activity_apply")
public class ActivityApply implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    //活动ID
    @TableField("activity_id")
    private String activityId;

    //报名用户ID
    @TableField("user_id")
    private String userId;

    //报名状态：0申请中，1成功，2不通过，3取消报名
    @TableField("apply_status")
    private String applyStatus;

    //报名时间
    @TableField("apply_time")
    private String applyTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

}
