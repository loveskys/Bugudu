package com.lyh.controller;

import com.lyh.entity.ComResponse;
import com.lyh.entity.activity.ActivityVo;
import com.lyh.entity.activity.Swiper;
import com.lyh.entity.activity.dto.ActivityHomeListDto;
import com.lyh.entity.home.SearchHistory;
import com.lyh.entity.home.SearchHistoryDto;
import com.lyh.service.IActivityService;
import com.lyh.service.ISearchHistoryService;
import com.lyh.service.ISwiperService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author lyh
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Resource
    private IActivityService activityService;
    @Resource
    private ISearchHistoryService iSearchHistoryService;
    @Resource
    private ISwiperService swiperService;

    /**
     * 小程序进行搜索活动或者用户
     * http://127.0.0.1:8080/api/home/to_search
     */
    @PostMapping(value = "/to_search")
    public ComResponse<?> to_search(@RequestBody SearchHistoryDto dto) {
        List<?> list = iSearchHistoryService.toSearch(dto);
        if (list != null) {
            return new ComResponse<>().success("获取成功", list);
        }
        return new ComResponse<>().fail("获取失败");
    }


    /**
     * 获取搜索历史
     * http://127.0.0.1:8080/api/home/get_search_history
     */
    @PostMapping(value = "/get_search_history")
    public ComResponse<?> getSearchHistory() {
        List<SearchHistory> list = iSearchHistoryService.getSearchHistory();
        if (list != null) {
            return new ComResponse<>().success("获取成功", list);
        }
        return new ComResponse<>().fail("获取失败");
    }


    /**
     * 获取首页活动列表
     * http://127.0.0.1:8080/api/home/home_list
     */
    @PostMapping(value = "/home_list")
    public ComResponse<?> home_list(@RequestBody ActivityHomeListDto dto) {
        List<ActivityVo> list = activityService.getHomeActivityList(dto.getTag());
        if (list != null) {
            return new ComResponse<>().success(list);
        }
        return new ComResponse<>().fail("查询结果为空");
    }


    /**
     * 获取首页的轮播图
     * http://127.0.0.1:8080/api/home/swiper_list
     */
    @PostMapping(value = "/swiper_list")
    public ComResponse<?> swiper_list() {
//        List<Swiper> list = activityService.getSwiperActivityList(4);
        List<Swiper> list = swiperService.getList();
        if (list != null) {
            return new ComResponse<>().success(list);
        }
        return new ComResponse<>().fail("查询结果为空");
    }
}
