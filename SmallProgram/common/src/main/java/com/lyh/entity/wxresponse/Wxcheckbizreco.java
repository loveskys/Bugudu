package com.lyh.entity.wxresponse;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 微信安全校验结果表
 */
@Data
@TableName("wxcheckbizreco")
public class Wxcheckbizreco implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    //业务类型：1用户认证，2活动提交，3联系方式，4轮播图
    @TableField("biz_type")
    private String bizType;

    //业务主表id
    @TableField("biz_id")
    private String bizId;

    //wx检测结果id
    @TableField("trace_id")
    private String traceId;

    //检测结果说明
    @TableField("check_cont")
    private String checkCont;

    //检测结果：0检测中，1不可以，2可以
    @TableField("check_status")
    private String checkStatus;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
}
