package com.lyh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.entity.activity.Swiper;
import com.lyh.entity.activity.SwiperVo;
import com.lyh.entity.activity.dto.ActivityRSDto;
import com.lyh.entity.activity.dto.SwiperDeleteDto;
import com.lyh.entity.activity.dto.SwiperPageListDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISwiperService extends IService<Swiper> {
    Swiper uploadArticle(MultipartFile file, String imgUrl);

    void deleteArticle(SwiperDeleteDto dto);

    IPage<SwiperVo> pageList(SwiperPageListDto dto);
    
    List<Swiper> getList();

    void uploadActivity(ActivityRSDto dto);
}
