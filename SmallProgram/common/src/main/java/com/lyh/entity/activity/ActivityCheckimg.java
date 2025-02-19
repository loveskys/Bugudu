package com.lyh.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("activity_checkimg")
public class ActivityCheckimg implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //审核表id
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    //审核表网址
    @TableField("checkimg_url")
    private String checkimgUrl;


    //审核表活动id
    @TableField("activity_id")
    private String activityId;
}
