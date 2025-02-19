package com.lyh.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lyh.entity.BizException;
import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

@Data
public class UserLoginDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //用户名
    @TableField("username")
    private String userName;
    //密码
    @TableField("password")
    private String passWord;
    //手机号
    @TableField("phone")
    private String phone;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(userName)) {
            throw new BizException(ComResponse.ERROR, "用户名为空！");
        }
        if (!StringUtils.hasText(passWord)) {
            throw new BizException(ComResponse.ERROR, "密码为空！");
        }
        return null;
    }

}