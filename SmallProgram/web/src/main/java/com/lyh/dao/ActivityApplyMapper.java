package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyh.entity.activity.ActivityApply;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 活动报名表Mapper
 */
public interface ActivityApplyMapper extends BaseMapper<ActivityApply> {

    @Select("select * from activity_apply where activity_id = #{activityId} and user_id = #{userId}")
    ActivityApply getStatus(@Param("userId") String userId, @Param("activityId") String activityId);


    @Delete("delete from activity_apply where activity_id = #{activityId} and user_id = #{userId}")
    void deleteApplyInfo(@Param("userId") String userId, @Param("activityId") String activityId);
    @Delete("delete from activity_apply_person where activity_id = #{activityId} and user_id = #{userId}")
    void deleteApplyPersonInfo(@Param("userId") String userId, @Param("activityId") String activityId);
}
