package com.lyh.controller;

import com.lyh.entity.wxresponse.WxPicCheckDto;
import com.lyh.service.WxRequest;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 接受微信消息推送的开放接口
 * @author lyh
 * @since 2024-09-01
 */
@RestController
@RequestMapping("/api/wxmsg")
public class WxMsgController {

    @Resource
    private WxRequest wxRequest;


    /**
     * https://bugudutechsz.cn/api/wxmsg/checktest
     */
//    @GetMapping(value = "/checktest")
//    public String checkTest(@RequestParam("signature") String signature,@RequestParam("timestamp") String timestamp,
//            @RequestParam("nonce") String nonce,@RequestParam("echostr") String echostr ) {
//        System.out.println(signature);
//        System.out.println(timestamp);
//        System.out.println(nonce);
//        System.out.println(echostr);
//        return echostr;
//    }


    /**
     * 得到图片校验结果的
     * 小程序后台配置的token：C797D97955ADOSIRIS5A868286A6000D
     * 小程序后台配置的url：https://bugudutechsz.cn/api/wxmsg/checktest
     */
    @PostMapping(value = "/checktest")
    public String checkTest(@RequestBody WxPicCheckDto dto) {
        System.out.println("检测图片接口回调："+dto);
        return wxRequest.clWxBackCell(dto);
    }


    //C797D97955ADOSIRIS5A868286A6000D
//    public static void main(String[] args) {
//        String ss = TokenUtil.getuid32();
//        System.out.println(ss);
//    }
}
