package com.lyh.entity.wxresponse;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

@Data
public class WxPciCheckDetail implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String strategy;
    private Integer errcode;
    private String suggest;
    private Integer label;
    private Integer prob;



//    strategy string	策略类型
//    errcode	number	错误码，仅当该值为0时，该项结果有效
//    suggest	string	建议，有risky、pass、review三种值
//    label	number	命中标签枚举值，100 正常；20001 时政；20002 色情；20006 违法犯罪；21000 其他
//    prob	number	0-100，代表置信度，越高代表越有可能属于当前返回的标签（label）

}
