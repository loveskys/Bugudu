package com.lyh.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.entity.ComResponse;
import com.lyh.entity.home.msg.*;
import com.lyh.service.IMessageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lyh
 * @since 2024-08-26
 */
@RestController
@RequestMapping("/api/msg")
public class MsgController {

    @Resource
    private IMessageService iMessageService;

    /**
     * 小程序获取消息列表
     * http://127.0.0.1:8080/api/msg/my_msg_list
     */
    @PostMapping(value = "/my_msg_list")
    public ComResponse<?> list(@RequestBody MessageDto dto) {
        MyMessageVo vo = iMessageService.getMyMsgList(dto);
        if (vo != null) {
            return new ComResponse<>().success("获取成功", vo);
        }
        return new ComResponse<>().fail("获取失败");
    }


    /**
     * pc端分页获取消息列表
     * http://127.0.0.1:8080/api/msg/page_list
     */
    @PostMapping(value = "/page_list")
    public ComResponse<?> page_list(@RequestBody MessageDto dto) {
        IPage<MessageVo> list = iMessageService.getPageList(dto);
        if (list != null) {
            return new ComResponse<>().success(list);
        }
        return new ComResponse<>().fail("获取失败");
    }

    /**
     * 删除消息
     * http://127.0.0.1:8080/api/msg/delete_msg
     */
    @PostMapping(value = "/delete_msg")
    public ComResponse<?> deleteMsg(@RequestBody MessageUpdateDto dto) {
        iMessageService.deleteMsg(dto);
        return new ComResponse<>().success("删除成功");
    }


    /**
     * 消除消息小红点（已读）
     * http://127.0.0.1:8080/api/msg/read
     */
    @PostMapping(value = "/read")
    public ComResponse<?> read(@RequestBody MessageUpdateDto dto) {
        iMessageService.read(dto);
        return new ComResponse<>().success("已读成功");
    }


    /**
     * pc后端发送官方通知消息
     * http://127.0.0.1:8080/api/msg/send_official
     */
    @PostMapping(value = "/send_official")
    public ComResponse<?> sendOfficial(@RequestBody MessageSendDto dto) {
        iMessageService.sendOfficial(dto);
        return new ComResponse<>().success("发送成功");
    }

    /**
     * 消除全部消息小红点（全部已读）
     * http://127.0.0.1:8080/api/msg/readAll
     */
    @PostMapping(value = "/readAll")
    public ComResponse<?> readAll(@RequestBody MessageDto dto) {
        iMessageService.readAll(dto);
        return new ComResponse<>().success("全部已读成功");
    }

    /**
     * 消除全部留言和收藏消息小红点
     * http://127.0.0.1:8080/api/msg/readAll/comments
     */
    @PostMapping(value = "/readAll/comments")
    public ComResponse<?> readAllComments(@RequestBody MessageDto dto) {
        iMessageService.readAllComments(dto);
        return new ComResponse<>().success("全部留言和收藏已读成功");
    }

    /**
     * 消除全部活动消息小红点
     * http://127.0.0.1:8080/api/msg/readAll/activity
     */
    @PostMapping(value = "/readAll/activity")
    public ComResponse<?> readAllActivity(@RequestBody MessageDto dto) {
        iMessageService.readAllActivity(dto);
        return new ComResponse<>().success("全部活动已读成功");
    }
}
