package com.lyh.entity.activity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

//活动地址
@Data
@TableName("activity_location")
public class ActivityLocation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    @TableField("activity_id")
    private String activityId;

    //活动地址名  深圳市民中心百果园(深圳深耕村店)
    @TableField("locationName")
    private String locationName;

    //具体地址 广东省深圳市福田区福中三路
    @TableField("locationAddress")
    private String locationAddress;

    //手写补充地址
    @TableField("address")
    private String address;

    //经度  114.05956
    @TableField("longitude")
    private String longitude;

    //纬度  22.54286
    @TableField("latitude")
    private String latitude;
}