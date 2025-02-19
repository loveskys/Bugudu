package com.lyh.entity.wxresponse;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 微信图片校验推送结果
 */
@Data
public class WxPicCheckDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String ToUserName; // 小程序的username
    private String FromUserName; // 平台推送服务UserName
    private long CreateTime; // 发送时间
    private String MsgType; // 默认为：event
    private String Event; // 默认为：wxa_media_check
    private String appid; // 小程序的appid
    private String trace_id; // 任务id
    private int version; // 可用于区分接口版本
    private WxPciCheckResult result; // 综合结果
    private List<WxPciCheckDetail> detail; // 详细检测结果

}
