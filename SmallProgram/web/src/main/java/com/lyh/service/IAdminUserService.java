package com.lyh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.entity.user.AdminUser;

/**
 * @author lyh
 * @since 2024-07-10
 */
public interface IAdminUserService extends IService<AdminUser> {

    AdminUser getByUserName(String userName, String pwd);

    void saveUserLog(String userId, String userName, String sysTag);
}
