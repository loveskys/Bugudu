package com.lyh.entity.contact;

import lombok.*;
import java.util.List;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxMaSubscribeMessage implements Serializable {
    private static final long serialVersionUID = 6846729898251286686L;
    //接收者的openID
    private String toUser;
    //模板消息ID
    private String templateID;
    //点击后跳转页面（非必填）
    private String page;
    //模板内容
    private List<MsgData> data;
    //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
    private String miniprogramState;
    //进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
    private String lang;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class MsgData implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private String value;
    }
}
