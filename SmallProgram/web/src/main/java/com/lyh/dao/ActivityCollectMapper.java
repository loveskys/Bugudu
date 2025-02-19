package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyh.entity.activity.ActivityCollect;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 活动收藏表Mapper
 */
public interface ActivityCollectMapper extends BaseMapper<ActivityCollect> {

    @Select("select * from activity_collect where activity_id = #{activityId} and user_id = #{userId}")
    ActivityCollect getCollect(@Param("userId") String userId, @Param("activityId") String activityId);

}
