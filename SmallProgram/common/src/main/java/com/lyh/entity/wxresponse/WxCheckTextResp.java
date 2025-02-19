package com.lyh.entity.wxresponse;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * 微信文本校验结果
 */
@Data
public class WxCheckTextResp implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    public WxCheckTextResp(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
