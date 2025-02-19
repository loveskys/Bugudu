package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyh.entity.activity.ActivityApplyPerson;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 活动报名人员表Mapper
 */
public interface ActivityApplyPersonMapper extends BaseMapper<ActivityApplyPerson> {

    @Select("select * from activity_apply_person where activity_id = #{activityId} order by create_time asc;")
    List<ActivityApplyPerson> getApplyPersonList(@Param("activityId") String activityId);

}
