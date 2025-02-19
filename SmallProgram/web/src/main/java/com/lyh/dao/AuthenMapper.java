package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.entity.authen.Authen;
import com.lyh.entity.authen.AuthenVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author lyh
 * @since 2024-07-31
 */
public interface AuthenMapper extends BaseMapper<Authen> {

    IPage<AuthenVo> getList(@Param("page")IPage<AuthenVo> page,
                            @Param("schoolName") String schoolName,
                            @Param("studentName") String studentName,
                            @Param("status") String status);

    void updateStatus(@Param("id") String id, @Param("cont") String cont, @Param("status") String status);

    @Select("select count(*) sn from authen where student_num = #{studentNum} and status = '2' and wx_img_check = '2' and user_id <> #{userId};")
    int checkStudentNum(@Param("studentNum") String studentNum, @Param("userId") String userId);

    List<Authen> findAuthenByUserIds(@Param("userIds") List<String> userIds);
}
