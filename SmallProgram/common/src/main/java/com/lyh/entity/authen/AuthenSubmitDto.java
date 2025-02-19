package com.lyh.entity.authen;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生认证提交
 */
@Data
public class AuthenSubmitDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String userId;

    private String realName;

    private String authenType;

    private MultipartFile authenImg;

    private String schoolName;

    private String schoolId;

    private String grade;

    private String studentNum;

    private String status;

    private String cont;

    private LocalDateTime createTime;
    private String createBy;
    private LocalDateTime updateTime;
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
        if (authenImg!=null) {
            return new ComResponse<>().error("请上传照片");
        }
        return null;
    }

}
