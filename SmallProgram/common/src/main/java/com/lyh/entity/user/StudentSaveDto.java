package com.lyh.entity.user;

import com.lyh.entity.ComResponse;
import com.lyh.util.TimeUtil;
import com.lyh.util.ValidUtil;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author lyh
 * @since 2024-07-29
 */
@Data
public class StudentSaveDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String headUrl;

    private String nickName;

    private String phone;

    private String birthdate;

    private String intro;

    private String sex;

    private String age;

    private String mbti;

    private String constellation;

    private String[] hobby;

    private String[] region;

    private String[] region_code;

    private String address;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(nickName)) {
            return new ComResponse<>().error("请填写昵称");
        }
        if (!StringUtils.hasText(sex) || "0".equals(sex)) {
            return new ComResponse<>().error("请选择性别");
        }
        if (!StringUtils.hasText(birthdate)) {
            return new ComResponse<>().error("请填写生日");
        } else {
            if (TimeUtil.withinLast14y(birthdate)) {
                return new ComResponse<>().error("必须14岁以上哦");
            }
            constellation = TimeUtil.determineZodiacSign(birthdate);
            age = String.valueOf(TimeUtil.calculateAge(birthdate));
        }
        if (!StringUtils.hasText(headUrl)) {
            return new ComResponse<>().error("请上传头像");
        }

//        if (!StringUtils.hasText(mbti)) {
//            return new ComResponse<>().error("请选择您的 MBTI");
//        }
//        System.out.println(hobby.length);
//        if (hobby == null || hobby.length == 0) {
//            return new ComResponse<>().error("请选择爱好");
//        }
        return null;
    }
}
