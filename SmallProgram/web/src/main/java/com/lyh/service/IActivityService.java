package com.lyh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.entity.activity.*;
import com.lyh.entity.activity.dto.*;
import com.lyh.entity.comment.ActivityLeaveVo;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * @author lyh
 * @since 2024-07-30
 */
public interface IActivityService extends IService<Activity> {

    //发布活动接口
    boolean originate(MultipartFile file, String activityInfo);

    //活动联系方式二维码上传接口
    boolean originateContact(MultipartFile file, String activityInfo);

    //编辑重发活动接口
    boolean reOriginate(ActivityDto dto);

    //根据活动名模糊查询列表
    List<ActivityVo> getByThemeList(String theme);

    //pc端分页查询列表
    IPage<ActivityVo> pageList(ActivityPageListDto dto);

    //活动发布审核接口
    void updateActivityAudit(ActivityAuditDto dto);

    //用户查询活动列表
    List<ActivityVo> getUserActivityList(ActivityMyListDto dto);

    //小程序首页查询列表
    List<ActivityVo> getHomeActivityList(String tag);

    //查询轮播图
//    List<Swiper> getSwiperActivityList(Integer num);

    //把活动设置为失效
    void delete(ActivityDeleteDto dto);

    //小程序获取活动详情
    ActivityVo detail(ActivityMyListDto dto);

    //pc端获取活动详情
    ActivityVo pc_detail(ActivityDetailDto dto);

    //获取活动报名者列表
    List<ActivityApplyPerson> getActivityApplicantsList(String dto);

    //上传审核表
    void updateActivityCheckImg(ActivityCheckimg dto);
    //获取审核表
    String getActivityCheckImg(String dto);

    void collect(ActivityCollectDto dto);
    void recommend(ActivityRSDto dto);
    List<ActivityLeaveVo> get_leaves(ActivityLeaveDto dto);
    void send_leaves(ActivityLeaveDto dto);
    void delete_leaves(ActivityLeaveDto dto);
}
