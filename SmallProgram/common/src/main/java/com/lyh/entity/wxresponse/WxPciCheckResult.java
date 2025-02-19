package com.lyh.entity.wxresponse;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

@Data
public class WxPciCheckResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String suggest;
    private Integer label;

//    suggest	string	建议，有risky、pass、review三种值（风险的，通过的，复审的）
//    label	number	命中标签枚举值，100 正常；20001 时政；20002 色情；20006 违法犯罪；21000 其他
}
