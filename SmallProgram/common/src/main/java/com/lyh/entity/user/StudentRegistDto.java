package com.lyh.entity.user;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author lyh
 * @since 2024-07-25
 */
@Data
public class StudentRegistDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String headUrl;
    private String nickName;
    private String userName;
    private String passWord;
    private String phone;
    private String birthdate;
    private String code;

    private String[] region;
    private String[] region_code;
    private String address;


    public ComResponse<?> validate() {
        if (!StringUtils.hasText(nickName)) {
            return new ComResponse<>().error("请填写昵称");
        }
        if (!StringUtils.hasText(userName)) {
            return new ComResponse<>().error("请填写用户名");
        }
        if (!StringUtils.hasText(passWord)) {
            return new ComResponse<>().error("请填写密码");
        }
        if (!StringUtils.hasText(phone)) {
            return new ComResponse<>().error("请填写手机号");
        }
        if (!StringUtils.hasText(headUrl)) {
            return new ComResponse<>().error("请上传头像");
        }
        if (!StringUtils.hasText(birthdate)) {
            return new ComResponse<>().error("请填写出生日期");
        }
        if (!StringUtils.hasText(code)) {
            return new ComResponse<>().error("微信code不能为空");
        }
        if (region_code==null || region_code.length==0) {
            return new ComResponse<>().error("请选择地区");
        }
        if (!StringUtils.hasText(address)) {
            return new ComResponse<>().error("请填写详细地址");
        }
        return null;
    }
}
