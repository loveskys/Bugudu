package com.lyh.controller;

import com.lyh.entity.ComResponse;
import com.lyh.entity.user.AdminUser;
import com.lyh.service.IAdminUserService;
import com.lyh.service.impl.WxRequestImpl;
import com.lyh.util.TokenUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lyh
 * @since 2024-07-10
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private TokenUtil tokenUtil;
    @Resource
    private IAdminUserService adminUserService;

    /**
     * 管理员登录PC端
     * http://127.0.0.1:8080/api/admin/login
     */
    @PostMapping(value = "/login")
    public ComResponse<?> login(@RequestBody AdminUser adminUser) {
        AdminUser user = adminUserService.getByUserName(adminUser.getUserName(), adminUser.getPassWord());
        if (user != null) {
            user.setToken(tokenUtil.signToken(user.getId(), TokenUtil.ADMIN_TOKEN));
            user.setPassWord("");
            adminUserService.saveUserLog(user.getId(), user.getUserName(), "PC管理端");
            return new ComResponse<>().success("登录成功", user);
        }
        return new ComResponse<>().fail("账号或密码错误");
    }

}
