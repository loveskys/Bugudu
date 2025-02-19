package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.entity.activity.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动主表Mapper
 * @author lyh
 * @since 2024-07-10
 */
public interface ActivityMapper extends BaseMapper<Activity> {

    @Insert("INSERT INTO activity_condit (activity_id, people_num, sex, grade, other) VALUES (#{activityId}, #{peopleNum}, #{sex}, #{grade}, #{other});")
    void saveActCondit(@Param("activityId") String activityId, @Param("peopleNum") String peopleNum,@Param("sex") String sex, @Param("grade") String grade, @Param("other") String other);
    @Update("update activity_condit set people_num = #{peopleNum},sex = #{sex}, grade = #{grade}, other = #{other} where activity_id = #{activityId};")
    void updateActCondit(@Param("activityId") String activityId, @Param("peopleNum") String peopleNum, @Param("sex") String sex, @Param("grade") String grade, @Param("other") String other);


    @Insert("INSERT INTO activity_img (activity_id, activity_image, valid, create_by, create_time) VALUES (#{activityId}, #{activityImg}, '0', #{createBy}, #{dateTime});")
    void saveActImg(@Param("activityId") String activityId, @Param("activityImg") String activityImg, @Param("createBy") String createBy, @Param("dateTime") LocalDateTime dateTime);
    @Update("update activity_img set activity_image = #{activityImg}, update_by = #{updateBy}, update_time = #{dateTime} where activity_id = #{activityId};")
    void updateActImg(@Param("activityId") String activityId, @Param("activityImg") String activityImg, @Param("updateBy") String updateBy, @Param("dateTime") LocalDateTime dateTime);

    @Update("update activity set contact_image = #{contactImg} where id = #{activityId};")
    void updateContactImg(@Param("activityId") String activityId, @Param("contactImg") String contactImg);

    @Insert("INSERT INTO activity_location (activity_id, location_name, location_address, longitude, latitude, address) values (#{location.activityId}, #{location.locationName}, #{location.locationAddress}, #{location.longitude}, #{location.latitude}, #{location.address});")
    void saveActLocation(@Param("location") ActivityLocation location);
    @Insert("update activity_location set location_name = #{location.locationName}, location_address = #{location.locationAddress}, longitude = #{location.longitude}, latitude = #{location.latitude}, address = #{location.address} where activity_id = #{activityId};")
    void updateActLocation(@Param("activityId") String activityId, @Param("location") ActivityLocation location);


    @Update("update activity set audit_status = #{status}, audit_cont = #{cont} where id = #{id};")
    void updateActivityAudit(@Param("id") String id, @Param("status") String status, @Param("cont") String cont);
    @Update("update activity set is_delete = '1' where id = #{id};")
    void updateActivityValid(@Param("id") String id);


    @Update("update activity set recommend = #{recommend} where id = #{id};")
    void updateByIdRecommend(@Param("id") String id, @Param("recommend") String recommend);


    ActivityVo pc_detail(@Param("id") String id);

    ActivityVo detail(@Param("id") String id, @Param("userId") String userId);

    IPage<ActivityVo> getPageList(@Param("page")IPage<ActivityVo> page,
                                  @Param("theme") String theme,
                                  @Param("tag") Integer tag,
                                  @Param("wxCheck") Integer wxCheck);

    List<ActivityVo> getByThemeList(@Param("theme") String theme);

    List<ActivityVo> getUserActivityList(@Param("apply") String apply, @Param("issue") String issue, @Param("collect") String collect);

    List<ActivityVo> getHomeActivityList(@Param("category") String category, @Param("recommend") String recommend);

    List<Swiper> getSwiperList(@Param("num") Integer num);

}