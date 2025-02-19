package com.lyh.entity.wxresponse;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

@Data
public class WxCommonResp implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String openId;
    private String sessionKey;


    private String accessToken;
    private Integer expiresIn;
}
