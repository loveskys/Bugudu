package com.lyh.entity.authen;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * 学生认证listVo
 */
@Data
public class AuthenVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String userId;

    private String realName;

    private String authenType;

    private String authenImg;

    private String schoolName;

    private String schoolId;

    private String grade;

    private String studentNum;

    private String studentCard;

    //认证状态：0未认证，1审核中，2已认证，3认证不通过
    private String status;


    //微信图片安全检测状态: 0未检测,1检测中,2通过,3不通过
    private String wxImgCheck;

    //认证结果
    private String cont;

    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;


    private String nickName;
    private String userName;
    private String wxNum;
    private String headUrl;
    private String wxOpenId;
    private String phone;

}
