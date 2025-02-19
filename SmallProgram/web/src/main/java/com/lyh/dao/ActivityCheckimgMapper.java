package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyh.entity.activity.ActivityCheckimg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ActivityCheckimgMapper extends BaseMapper<ActivityCheckimg> {

    @Select("select * from activity_checkimg where activity_id = #{activityId}")
    ActivityCheckimg getCheckimg(@Param("activityId") String activityId);

    @Update("update activity_checkimg set checkimg_url = #{checkimgUrl} where activity_id = #{activityId}")
    void updateCheckimgurl(@Param("activityId") String activityId, @Param("checkimgUrl") String checkimgUrl);

}