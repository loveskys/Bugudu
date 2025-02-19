package com.lyh.service;

import com.lyh.entity.user.WxUserLoginDto;
import com.lyh.entity.wxresponse.WxCheckTextResp;
import com.lyh.entity.wxresponse.WxCommonResp;
import com.lyh.entity.wxresponse.WxPicCheckDto;

/**
 * 微信请求方法
 */
public interface WxRequest {

    String getAccessToken();

    WxCommonResp wxGetOpenId(String code);

    String decryptWxInfo(WxUserLoginDto dto, String sessionKey);

    //scene：场景枚举值(1资料；2评论；3论坛；4社交日志)
    WxCheckTextResp wxCheckTextSecurity(String openId, String content, String scene);

    //bizType业务类型：1用户认证，2活动提交，3联系二维码，4轮播图
    String wxCheckPicSecurity(String bizType, String bizId, String openId, String mediaUrl);

    String clWxBackCell(WxPicCheckDto dto);
}
