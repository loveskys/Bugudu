package com.lyh.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lyh
 * @since 2024-07-05
 */
@Data
@TableName("student_user")
public class StudentUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    //用户名
    @TableField("username")
    private String userName;

    //昵称
    @TableField("nickname")
    private String nickName;

    //密码
    @TableField("password")
    private String passWord;

    //头像url
    @TableField("head_url")
    private String headUrl;

    //微信号
    @TableField("wx_num")
    private String wxNum;

    //微信OpenId
    @TableField("wx_openid")
    private String wxOpenId;

    //微信UnionId
    @TableField("wx_unionid")
    private String wxUnionId;

    //微信sessionKey
    @TableField("wx_session_key")
    private String wxSessionKey;

    //手机号
    @TableField("phone")
    private String phone;

    //性别：0未知，1男2女
    @TableField("sex")
    private String sex;

    //个人简介
    @TableField("intro")
    private String intro;

    //出生日期
    @TableField("birthdate")
    private String birthdate;

    //年龄
    @TableField("age")
    private String age;

    //省市区中文
    @TableField("pcc_Text")
    private String pccText;

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

    //MBTI人格
    @TableField("mbti")
    private String mbti;

    //标签
    @TableField("hobby")
    private String hobby;

    //星座
    @TableField("constellation")
    private String constellation;

    //认证ID
    @TableField("auth_id")
    private String authId;

    //学生认证状态：0未认证，1已认证
    @TableField("auth_status")
    private String authStatus;

    //认证结果
    @TableField("auth_cont")
    private String authCont;

    @TableField(exist = false)
    private String token;
    @TableField(exist = false)
    private String code;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

}
