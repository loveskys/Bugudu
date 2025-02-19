package com.lyh.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.entity.ComResponse;
import com.lyh.entity.authen.*;
import com.lyh.service.IAuthenService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * @author lyh
 * @since 2024-07-30
 */
@RestController
@RequestMapping("/api/authen")
public class AuthenController {
    @Resource
    private IAuthenService authenService;

    /**
     * 学生提交认证
     * http://127.0.0.1:8080/api/authen/submit
     */
    @PostMapping(value = "/submit")
    public ComResponse<?> submit(@RequestParam("authenImg") MultipartFile file, @RequestParam Map<String, String> allParams) {
        Authen authen = authenService.submit(file, allParams);
        if (authen != null) {
            return new ComResponse<>().success("submit-ok");
        }
        return new ComResponse<>().fail("submit-fail");
    }

    /**
     * 学生被退回再次提交认证
     * http://127.0.0.1:8080/api/authen/resubmit
     */
    @PostMapping(value = "/resubmit")
    public ComResponse<?> resubmit(@RequestBody Authen dto) {
        Authen authen = authenService.resubmit(dto);
        if (authen != null) {
            return new ComResponse<>().success("resubmit ok");
        }
        return new ComResponse<>().fail("resubmit fail");
    }


    /**
     * 微信图片校验开放接口
     * http://127.0.0.1:8080/api/authen/wx_call_back
     */
    @PostMapping(value = "/wx_call_back")
    public ComResponse<?> wx_call_back(@RequestBody Authen dto) {
        Authen authen = authenService.resubmit(dto);
        if (authen != null) {
            return new ComResponse<>().success("resubmit ok");
        }
        return new ComResponse<>().fail("resubmit fail");
    }


    /**
     * 获取提交认证列表
     * http://127.0.0.1:8080/api/authen/list
     */
    @PostMapping(value = "/list")
    public ComResponse<?> list(@RequestBody AuthenSearchDto dto) {
        IPage<AuthenVo> list = authenService.list(dto);
        if (list != null) {
            return new ComResponse<>().success(list);
        }
        return new ComResponse<>().fail("查询结果为空");
    }


    /**
     * 学生获取认证信息
     * http://127.0.0.1:8080/api/authen/authen_detail
     */
    @PostMapping(value = "/authen_detail")
    public ComResponse<?> authen_detail(@RequestBody AuthenDetailDto dto) {
        Authen authen = authenService.detail(dto);
        if (authen != null) {
            return new ComResponse<>().success(authen);
        }
        return new ComResponse<>().fail("获取失败");
    }


    /**
     * pc端对学生进行认证
     * http://127.0.0.1:8080/api/authen/updateStatus
     */
    @PostMapping(value = "/updateStatus")
    public ComResponse<?> updateStatus(@RequestBody AuthenUpdateDto dto) {
        authenService.updateStatus(dto);
        return new ComResponse<>().success("操作成功");
    }

}
