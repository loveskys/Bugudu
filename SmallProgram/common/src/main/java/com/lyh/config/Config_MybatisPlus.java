package com.lyh.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.lyh.util.ThreadLocalUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;

/**
 * mybatis-plus
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.lyh.dao","com.lyh.config"})
public class Config_MybatisPlus implements MetaObjectHandler {

    /**
     * 配置分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * MybatisPlus自动填充时间信息
     * 在字段中用注解
     * 1. @TableField(fill=FieldFill.INSERT)
     * 2. @TableField(fill=FieldFill.UPDATE)
     * 3. @TableField(fill = FieldFill.INSERT_UPDATE)
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        if(metaObject.hasSetter("createBy") && metaObject.hasSetter("updateBy")) {
            if(StringUtils.hasText(ThreadLocalUtil.getAdminUserId())) {
                this.setFieldValByName("createBy", "admin-"+ThreadLocalUtil.getAdminUserId(), metaObject);
                this.setFieldValByName("updateBy", "admin-"+ThreadLocalUtil.getAdminUserId(), metaObject);
            }
            if(StringUtils.hasText(ThreadLocalUtil.getStudentUserId())) {
                this.setFieldValByName("createBy", "student-"+ThreadLocalUtil.getStudentUserId(), metaObject);
                this.setFieldValByName("updateBy", "student-"+ThreadLocalUtil.getStudentUserId(), metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        if(metaObject.hasSetter("updateBy")) {
            if(StringUtils.hasText(ThreadLocalUtil.getAdminUserId())) {
                this.setFieldValByName("updateBy", "admin"+ThreadLocalUtil.getAdminUserId(), metaObject);
            }
            if(StringUtils.hasText(ThreadLocalUtil.getStudentUserId())) {
                this.setFieldValByName("updateBy", "student-"+ThreadLocalUtil.getStudentUserId(), metaObject);
            }
        }
    }

}