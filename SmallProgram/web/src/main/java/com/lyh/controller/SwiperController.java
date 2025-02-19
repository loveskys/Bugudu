package com.lyh.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.entity.ComResponse;
import com.lyh.entity.activity.SwiperVo;
import com.lyh.entity.activity.dto.SwiperDeleteDto;
import com.lyh.entity.activity.dto.SwiperPageListDto;
import com.lyh.entity.activity.dto.SwiperUploadDto;
import com.lyh.service.ISwiperService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/loopimg")
public class SwiperController {
    @Resource
    private ISwiperService swiperService;

    /**
     * 上传轮播图
     * http://127.0.0.1:8080/api/loopimg/upload
     */
    @PostMapping(value = "/upload")
    public ComResponse<?> upload(@RequestParam("loopImg") MultipartFile file, @RequestParam("imgUrl") String imgUrl){
        if(swiperService.uploadArticle(file, imgUrl) != null) {
            return new ComResponse<>().success("uploadimg-ok");
        }
        return new ComResponse<>().success("uploadimg-fail");
    }

    /**
     * 查看轮播图(pc)
     * http://127.0.0.1:8080/api/loopimg/list
     */
    @PostMapping(value = "/list")
    public ComResponse<?> list(@RequestBody SwiperPageListDto dto){
        IPage<SwiperVo> list = swiperService.pageList(dto);
        if (list != null) {
            return new ComResponse<>().success(list);
        }
        return new ComResponse<>().fail("获取失败");
    }

    /**
     * 下架轮播图
     * http://127.0.0.1:8080/api/loopimg/delete
     */
    @PostMapping(value = "/delete")
    public ComResponse<?> delete(@RequestBody SwiperDeleteDto dto){
        swiperService.deleteArticle(dto);
        return new ComResponse<>().success("下架成功");
    }
}
