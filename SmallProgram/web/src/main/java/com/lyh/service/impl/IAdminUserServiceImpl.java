package com.lyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.dao.AdminUserMapper;
import com.lyh.dao.UserLogMapper;
import com.lyh.entity.user.AdminUser;
import com.lyh.entity.user.UserLog;
import com.lyh.service.IAdminUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lyh
 * @since 2023-07-10
 */
@Service
public class
IAdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements IAdminUserService {

    @Resource
    private UserLogMapper userLogMapper;

    @Override
    @Transactional
    public AdminUser getByUserName(String userName, String pwd) {
        if(StringUtils.hasText(pwd)){
            QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(AdminUser::getUserName, userName).eq(AdminUser::getPassWord, pwd);
            List<AdminUser> list = list(queryWrapper);
            if (list != null && !list.isEmpty()) {
                AdminUser user = list.getFirst();
                if (pwd.equals(user.getPassWord())) {
                    return user;
                }
            }
        }
        return null;
    }


    @Override
    @Transactional
    public void saveUserLog(String userId, String userName, String sysTag) {
        UserLog userLog = new UserLog(userId, userName, sysTag,"管理员登录",
                LocalDateTime.now(), userId, LocalDateTime.now(), userId);
        userLogMapper.insert(userLog);
    }

}
