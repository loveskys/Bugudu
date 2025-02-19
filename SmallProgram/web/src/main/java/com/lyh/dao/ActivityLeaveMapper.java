package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyh.entity.comment.ActivityLeave;
import com.lyh.entity.comment.ActivityLeaveVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 活动留言表Mapper
 * @author lyh
 * @since 2024-08-30
 */
public interface ActivityLeaveMapper extends BaseMapper<ActivityLeave> {

    @Select("select * from activity_leave where activity_id = #{activityId} order by create_time asc;")
    List<ActivityLeaveVo> get_leaves(@Param("activityId") String activityId);


}