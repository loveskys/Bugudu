package com.lyh.entity.activity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动主表
 */
@Data
@TableName("activity")
public class Activity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("theme")
    private String theme;

    @TableField("intro")
    private String intro;

    @TableField("category")
    private String category;

    @TableField("image")
    private String image;

    //微信公众号链接
    @TableField("wx_url")
    private String wxUrl;

    //图片类型，0：图片，1：微信公众号
//    @TableField("image_type")
//    private String imageType;

    //活动报名时间
    @TableField("apply_start")
    private LocalDateTime applyStart;
    @TableField("apply_end")
    private LocalDateTime applyEnd;

    //活动开始时间
    @TableField("activity_start")
    private LocalDateTime activityStart;
    @TableField("activity_end")
    private LocalDateTime activityEnd;

    //活动是否删除：0未删，1已删除
    @TableField("is_delete")
    private String isDelete;

    //是否首页推荐：0否1是
    @TableField("recommend")
    private String recommend;

    //活动发起人
    @TableField("issue")
    private String issue;

    //活动发起人联系方式 (微信,电话)
    @TableField("contact_type")
    private String contactType;

    //活动发起人联系方式
    @TableField("contact")
    private String contact;
    @TableField("contact_image")
    private String contact_image;

    //活动申请状态：0申请中，1通过，2不通过
    @TableField("audit_status")
    private String auditStatus;

    //活动申请通过时间
    @TableField("audit_time")
    private LocalDateTime auditTime;

    //活动申请状态返回内容
    @TableField("audit_cont")
    private String auditCont;


    //微信图片安全检测状态: 0未检测,1检测中,2通过,3不通过
    @TableField("wx_img_check")
    private String wxImgCheck;




    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

}
