package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.entity.activity.Swiper;
import com.lyh.entity.activity.SwiperVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SwiperMapper extends BaseMapper<Swiper> {

    @Select("select * from swiper where is_delete = '0'")
    List<Swiper> getSwiperList();

    @Select("select id, image, url, is_delete as isDelete from swiper where type = #{type}")
    IPage<SwiperVo> getPageSwiperList(@Param("page")IPage<SwiperVo> page, @Param("type")String type);

    @Update("update swiper set is_delete = '1' where id = #{id}")
    void deleteSwiperByArticle(@Param("id") String id);

    @Update("update swiper set is_delete = #{is_delete} where activity_id = #{activity_id}")
    void updateSwiperByActivity(@Param("activity_id") String activityId, @Param("is_delete") String isDelete);

    @Select("select * from swiper where activity_id = #{activity_id}")
    List<Swiper> getSwiperByActivity(@Param("activity_id") String activityId);

    @Select("select image from activity where id = #{activity_id}")
    String getActivityImage(@Param("activity_id") String activityId);
}
