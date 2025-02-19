package com.lyh.controller;

import com.lyh.entity.ComResponse;
import com.lyh.entity.user.*;
import com.lyh.service.IStudentUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author lyh
 * @since 2024-07-05
 */
@RestController
@RequestMapping("/api/user")
public class StudentController {

    @Resource
    private IStudentUserService studentUserService;

    /**
     * 学生账号密码登录小程序
     * http://127.0.0.1:8080/api/user/student_login
     */
    @PostMapping(value = "/student_login")
    public ComResponse<?> student_login(@RequestBody UserLoginDto dto) {
        StudentUser user = studentUserService.getByUserName(dto.getUserName(), dto.getPassWord());
        if (user != null) {
            user.setPassWord("");
            user.setWxSessionKey("");
            user.setWxOpenId("");
            studentUserService.saveUserLog(user.getId(), user.getUserName(), "小程序", "登录操作");
            return new ComResponse<>().success("登录成功", user);
        }
        return new ComResponse<>().fail("账号或密码错误");
    }

    /**
     * 学生微信快捷登录小程序
     * http://127.0.0.1:8080/api/user/student_wx_login
     */
    @PostMapping(value = "/student_wx_login")
    public ComResponse<?> student_wx_login(@RequestBody WxUserLoginDto dto) {
        StudentUser user = studentUserService.wxLogin(dto);
        if (user != null) {
            studentUserService.saveUserLog(user.getId(), user.getUserName(), "小程序", "登录操作");
            return new ComResponse<>().success("登录成功", user);
        }
        return new ComResponse<>().fail("微信登录错误");
    }


    /**
     * 学生注册账号
     * http://127.0.0.1:8080/api/user/student_register
     */
    @PostMapping(value = "/student_register")
    public ComResponse<?> student_register(@RequestBody StudentRegistDto dto) {
        StudentUser user = studentUserService.register(dto);
        if (user != null) {
            studentUserService.saveUserLog(user.getId(), user.getUserName(), "小程序", "学生注册操作");
            return new ComResponse<>().success("注册成功", user);
        }
        return new ComResponse<>().fail("注册失败");
    }


    /**
     * 学生信息保存or修改
     * http://127.0.0.1:8080/api/user/student_save
     */
    @PostMapping(value = "/student_save")
    public ComResponse<?> student_save(@RequestBody StudentSaveDto dto) {
        StudentUser user = studentUserService.saveOrUpdate(dto);
        if (user != null) {
            return new ComResponse<>().success(user);
        }
        return new ComResponse<>().success();
    }



    /**
     * 学生综合信息获取（信息，认证）
     * http://127.0.0.1:8080/api/user/student_info
     */
    @PostMapping(value = "/student_info")
    public ComResponse<?> student_info(@RequestBody StudentDetailDto dto) {
        StudentInfoVo infoVo = studentUserService.getInfoDetail(dto);
        if (infoVo != null) {
            return new ComResponse<>().success(infoVo);
        }
        return new ComResponse<>().fail("查询失败");
    }

    /**
     * 修改学生身份
     * http://127.0.0.1:8080/api/user/student_update_category
     */
    @PostMapping(value = "/student_update_category")
    public ComResponse<?> student_update_category(@RequestBody StudentCategoryDto dto) {
        boolean ok = studentUserService.updateCategory(dto.getId(),dto.getCategory());
        if(ok) {
            return new ComResponse<>().success("修改成功");
        }return new ComResponse<>().fail("修改失败");
    }





    /**
     * 白名单测试请求
     * http://127.0.0.1:8080/api/user/getTest
     */
    @GetMapping(value = "/getTest")
    public ComResponse<?> getToken() {
        return new ComResponse<>().success("你好，同学");
    }

}
