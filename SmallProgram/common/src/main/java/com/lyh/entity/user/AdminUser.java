package com.lyh.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lyh
 * @since 2024-07-10
 */
@Data
@TableName("admin_user")
public class AdminUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    //学校
    @TableField("school_id")
    private String schoolId;

    //学号
    @TableField("student_num")
    private String studentNum;

    //年级
    @TableField("grade")
    private String grade;

    //班级
    @TableField("classz")
    private String classz;

    //用户名
    @TableField("username")
    private String userName;

    //昵称
    @TableField("nickname")
    private String nickName;

    //密码
    @TableField("password")
    private String passWord;

    //手机号
    @TableField("phone")
    private String phone;

    //性别：0未知，1男2女
    @TableField("sex")
    private String sex;

    //年龄
    @TableField("age")
    private String age;

    //省份
    @TableField("prov")
    private String prov;

    //城市
    @TableField("city")
    private String city;

    //区县
    @TableField("county")
    private String county;

    //详细现地址
    @TableField("address")
    private String address;

    @TableField(exist = false)
    private String token;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

}
