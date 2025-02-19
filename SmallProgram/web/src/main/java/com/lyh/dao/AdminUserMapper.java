package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyh.entity.user.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author lyh
 * @since 2024-07-10
 */
public interface AdminUserMapper extends BaseMapper<AdminUser> {

    @Select("select id, username from admin_user where username = #{name} and password = #{pwd} limit 1;")
    AdminUser getUserByPwd(@Param("name") String userName, @Param("pwd") String passWord);

}
