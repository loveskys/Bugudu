package com.lyh.entity.activity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

//活动限制信息表
@Data
@TableName("activity_condit")
public class ActivityCondit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //活动人数限制
    @TableField("people_num")
    private String peopleNum;

    //性别限制: 0不限, 1限男, 2限女
    @TableField("sex")
    private String sex;

    //年级限制
    @TableField("grade")
    private String grade;

    //其他手填限制
    @TableField("other")
    private String other;


}