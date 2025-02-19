package com.lyh.entity.user;

import com.lyh.entity.BizException;
import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

@Data
public class WxUserLoginDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String code;
    private String encryptedData;
    private String iv;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(code)) {
            throw new BizException(ComResponse.ERROR, "微信code为空！");
        }
        return null;
    }

}