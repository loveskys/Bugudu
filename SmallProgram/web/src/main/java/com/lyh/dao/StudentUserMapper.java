package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyh.entity.user.StudentUser;
import kotlin.text.UStringsKt;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

/**
 * @author lyh
 * @since 2024-07-05
 */
public interface StudentUserMapper extends BaseMapper<StudentUser> {

    @Select("select id, username from student_user where username = #{name} and password = #{pwd} limit 1;")
    StudentUser getUserByPwd(@Param("name") String userName, @Param("pwd") String passWord);

    @Update("update student_user set auth_id = #{auth_id}, auth_status = #{auth_status}, auth_cont = #{auth_cont} where id = #{id};")
    void updateUserAuthenById(
            @Param("auth_id") String auth_id,
            @Param("auth_cont") String auth_cont,
            @Param("auth_status") String auth_status,
            @Param("id") String id);

    @Update("UPDATE student_category SET category = #{category} WHERE id = #{id}")
    void updateUserCategory(
            @Param("id")String id,
            @Param("category")String category
    );

    @Select("select count(*) sn from student_user where nickname = #{nickName} and id <> #{id};")
    Integer checkNickName(@Param("nickName") String nickName, @Param("id") String id);


    @Select("select * from student_user where nickname like concat('%',#{nickName},'%') order by create_time desc;")
    List<StudentUser> getByNameList(@Param("nickName") String nickName);

    @Select("select category from student_category WHERE id = #{id}")
    String getUserCategory(
            @Param("id")String id
    );
}
