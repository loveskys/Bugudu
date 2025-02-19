package com.lyh.entity.authen;

import com.baomidou.mybatisplus.annotation.*;
import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生认证表
 */
@Data
@TableName("authen")
public class Authen implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("real_name")
    private String realName;

    @TableField("authen_type")
    private String authenType;

    @TableField("authen_img")
    private String authenImg;

    @TableField("school_name")
    private String schoolName;

    @TableField("school_id")
    private String schoolId;

    //年级
    @TableField("grade")
    private String grade;

    //学号
    @TableField("student_num")
    private String studentNum;


    //认证状态：0未认证，1审核中，2已认证，3认证不通过
    @TableField("status")
    private String status;


    //微信图片安全检测状态: 0未检测,1检测中,2通过,3不通过
    @TableField("wx_img_check")
    private String wxImgCheck;


    //认证/检测结果
    @TableField("cont")
    private String cont;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(schoolName)) {
            return new ComResponse<>().error("请填写学校名称");
        }
        if (!StringUtils.hasText(studentNum)) {
            return new ComResponse<>().error("请填写学号");
        }
        if (!StringUtils.hasText(authenType)) {
            return new ComResponse<>().error("请选择上传的证件类型");
        }
        if (!StringUtils.hasText(authenImg)) {
            return new ComResponse<>().error("请上传照片");
        }
        return null;
    }
}
