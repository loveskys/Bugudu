package com.lyh.entity.user;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

@Data
public class StudentInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    //用户名
    private String userName;

    //昵称
    private String nickName;

    //头像url
    private String headUrl;

    //微信OpenId
    private String wxOpenId;

    //性别：0未知，1男2女
    private String sex;

    //个人简介
    private String intro;

    //出生日期
    private String birthdate;

    //年龄
    private String age;

    //MBTI人格
    private String mbti;

    //爱好
    private String hobby;

    private String[] hobbyArray;

    //星座
    private String constellation;

    //认证ID
    private String authId;

    //学生认证状态：0未认证，1已认证
    private String authStatus;

    //认证结果
    private String authCont;

    private String createTime;

    //学生资料完善状态：0未完善，1已完善
    private String infoPerfect;

    //认证数组字符串
    private String[] authenArray;

    //首页展示的一些认证字符串
    private String[] myHomeAuthenArray;



    //获取请求人自身的性别
    private String mySex;
    //获取请求人自身的认证情况
    private String myAuthenStatus;
    //获取请求人自身的认证情况
    private String myAuthenCont;
    //获取用户类型
    private String category;

}
