package com.lyh.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.entity.ComResponse;
import com.lyh.entity.activity.*;
import com.lyh.entity.activity.dto.*;
import com.lyh.entity.comment.ActivityLeaveVo;
import com.lyh.service.IActivityApplyService;
import com.lyh.service.IActivityService;
import com.lyh.service.ISwiperService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

/**
 * @author lyh
 * @since 2024-07-26
 */
@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);
    @Resource
    private IActivityService activityService;
    @Resource
    private IActivityApplyService activityApplyService;
    @Resource
    private ISwiperService swiperService;

//    TRUNCATE TABLE activity;
//    TRUNCATE TABLE activity_apply;
//    TRUNCATE TABLE activity_apply_person;
//    TRUNCATE TABLE activity_collect;
//    TRUNCATE TABLE activity_condit;
//    TRUNCATE TABLE activity_img;
//    TRUNCATE TABLE activity_leave;
//    TRUNCATE TABLE activity_location;
//    TRUNCATE TABLE authen;
//    TRUNCATE TABLE message;
//    TRUNCATE TABLE message_read;
//    TRUNCATE TABLE search_history;
//    TRUNCATE TABLE student_user;
//    TRUNCATE TABLE wxcheckbizreco;


    /**
     * 活动发布接口
     * http://127.0.0.1:8080/api/activity/originate
     */
    @PostMapping(value = "/originate")
    public ComResponse<?> originate(@RequestParam("activityImg") MultipartFile file, @RequestParam("activityInfo") String activityInfo) {
        boolean ok = activityService.originate(file, activityInfo);
        if(ok) {
            return new ComResponse<>().success("originate-ok");
        }
        return new ComResponse<>().fail("originate-fail");
    }

    /**
     * 活动联系方式二维码上传接口
     * http://127.0.0.1:8080/api/activity/originate_contact
     */
    @PostMapping(value = "/originate_contact")
    public ComResponse<?> originateContact(@RequestParam("contactImg") MultipartFile file, @RequestParam("activityInfo") String activityInfo) {
        boolean ok = activityService.originateContact(file, activityInfo);
        if(ok) {
            return new ComResponse<>().success("contact-originate-ok");
        }
        return new ComResponse<>().fail("contact-originate-fail");
    }

    /**
     * 活动发布接口
     * http://127.0.0.1:8080/api/activity/reoriginate
     */
    @PostMapping(value = "/reoriginate")
    public ComResponse<?> reOriginate(@RequestBody ActivityDto dto) {
        boolean ok = activityService.reOriginate(dto);
        if(ok) {
            return new ComResponse<>().success("发布成功");
        }
        return new ComResponse<>().fail("发布失败");
    }


    /**
     * 活动发布审核接口
     * http://127.0.0.1:8080/api/activity/originate_audit
     */
    @PostMapping(value = "/originate_audit")
    public ComResponse<?> updateActivityAudit(@RequestBody ActivityAuditDto dto) {
        activityService.updateActivityAudit(dto);
        return new ComResponse<>().success("审核成功");
    }


    /**
     * 设置活动下架
     * http://127.0.0.1:8080/api/activity/delete
     */
    @PostMapping(value = "/delete")
    public ComResponse<?> delete(@RequestBody ActivityDeleteDto dto) {
        activityService.delete(dto);
        return new ComResponse<>().success("活动下架成功");
    }


    /**
     * pc端查看活动列表
     * http://127.0.0.1:8080/api/activity/list
     */
    @PostMapping(value = "/list")
    public ComResponse<?> list(@RequestBody ActivityPageListDto dto) {
        IPage<ActivityVo> list = activityService.pageList(dto);
        if (list != null) {
            return new ComResponse<>().success(list);
        }
        return new ComResponse<>().fail("查询结果为空");
    }


    /**
     * 活动详情(pc端)
     * http://127.0.0.1:8080/api/activity/pc_detail
     */
    @PostMapping(value = "/pc_detail")
    public ComResponse<?> pc_detail(@RequestBody ActivityDetailDto dto) {
        ActivityVo activityVo = activityService.pc_detail(dto);
        if (activityVo != null) {
            return new ComResponse<>().success(activityVo);
        }
        return new ComResponse<>().fail("查询结果为空");
    }


    /**
     * 活动详情(小程序)
     * http://127.0.0.1:8080/api/activity/detail
     */
    @PostMapping(value = "/detail")
    public ComResponse<?> detail(@RequestBody ActivityMyListDto dto) {
        ActivityVo activityVo = activityService.detail(dto);
        if (activityVo != null) {
            return new ComResponse<>().success(activityVo);
        }
        return new ComResponse<>().fail("查询结果为空");
    }


    /**
     * 活动报名接口
     * http://127.0.0.1:8080/api/activity/apply
     */
    @PostMapping(value = "/apply")
    public ComResponse<?> apply(@RequestBody ActivityApplyDto dto) {
        int i = activityApplyService.apply(dto);
        if(i==2) {
            return new ComResponse<>().success("报名成功", 2);
        }
        if(i==1) {
            return new ComResponse<>().success("待生效", 1);
        }
        return new ComResponse<>().success("报名失败", 0);
    }


    /**
     * 活动取消报名接口
     * http://127.0.0.1:8080/api/activity/apply_cancel
     */
    @PostMapping(value = "/apply_cancel")
    public ComResponse<?> applyCancel(@RequestBody ActivityApplyDto dto) {
        activityApplyService.applyCancel(dto);
        return new ComResponse<>().success("取消报名成功");
    }


    /**
     * 活动收藏操作
     * http://127.0.0.1:8080/api/activity/collect
     */
    @PostMapping(value = "/collect")
    public ComResponse<?> collect(@RequestBody ActivityCollectDto dto) {
        activityService.collect(dto);
        return new ComResponse<>().success("操作成功");
    }


    /**
     * pc端对活动进行推荐
     * http://127.0.0.1:8080/api/activity/recommend
     */
    @PostMapping(value = "/recommend")
    public ComResponse<?> recommend(@RequestBody ActivityRSDto dto) {
        activityService.recommend(dto);
        return new ComResponse<>().success("操作成功");
    }

    /**
     * pc端活动上轮播图
     * http://127.0.0.1:8080/api/activity/setCarousel
     */
    @PostMapping(value = "/setCarousel")
    public ComResponse<?> setCarousel(@RequestBody ActivityRSDto dto) {
        swiperService.uploadActivity(dto);
        return new ComResponse<>().success("操作成功");
    }
    /**
     * 获取该学生的（参与，发布，收藏）的活动列表
     * http://127.0.0.1:8080/api/activity/user_list
     */
    @PostMapping(value = "/user_list")
    public ComResponse<?> user_list(@RequestBody ActivityMyListDto dto) {
        List<ActivityVo> list = activityService.getUserActivityList(dto);
        if (list != null) {
            return new ComResponse<>().success(list);
        }
        return new ComResponse<>().fail("查询结果为空");
    }


    /**
     * 获取活动留言
     * http://127.0.0.1:8080/api/activity/get_leaves
     */
    @PostMapping(value = "/get_leaves")
    public ComResponse<?> get_leaves(@RequestBody ActivityLeaveDto dto) {
        List<ActivityLeaveVo> list = activityService.get_leaves(dto);
        if (list != null) {
            return new ComResponse<>().success(list);
        }
        return new ComResponse<>().fail("查询结果为空");
    }


    /**
     * 发送活动留言
     * http://127.0.0.1:8080/api/activity/send_leaves
     */
    @PostMapping(value = "/send_leaves")
    public ComResponse<?> send_leaves(@RequestBody ActivityLeaveDto dto) {
        activityService.send_leaves(dto);
        return new ComResponse<>().success("发送成功");
    }


    /**
     * 删除活动留言
     * http://127.0.0.1:8080/api/activity/delete_leaves
     */
    @PostMapping(value = "/delete_leaves")
    public ComResponse<?> delete_leaves(@RequestBody ActivityLeaveDto dto) {
        System.out.println("收到删除信号");
        System.out.println(dto);
        activityService.delete_leaves(dto);
        return new ComResponse<>().success("删除成功");
    }

    /**
     * 返回活动报名者
     * http://127.0.0.1:8080/api/activity/return_applicants
     */
     @PostMapping(value = "/return_applicants")
     public ComResponse<?> return_applicants(@RequestBody ActivityCheckimg dto) {
         activityService.updateActivityCheckImg(dto);
         return new ComResponse<>().success("上传成功");
     }

    /**
     * 上传审核表
     * http://127.0.0.1:8080/api/activity/update_checkimg
     */
    @PostMapping(value = "/update_checkimg")
    public ComResponse<?> update_checkimg(@RequestBody ActivityCheckimgDto dto) {
        String img = activityService.getActivityCheckImg(dto.getActivityId());
        if (img != null) {

            return new ComResponse<>().success(img);
        }
        return new ComResponse<>().fail("查询结果为空");
    }

    /**
     * 获取审核表
     * http://127.0.0.1:8080/api/activity/get_checkimg
     */
    @PostMapping(value = "/get_checkimg")
    public ComResponse<?> get_checkimg(@RequestBody ActivityCheckimgDto dto) {

        String img = activityService.getActivityCheckImg(dto.getActivityId());
        if (img != null) {

            return new ComResponse<>().success(img);
        }
        return new ComResponse<>().fail("查询结果为空");
    }
}
