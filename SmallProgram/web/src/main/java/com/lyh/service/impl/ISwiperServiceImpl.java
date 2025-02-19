package com.lyh.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.dao.SwiperMapper;
import com.lyh.entity.activity.Swiper;
import com.lyh.entity.activity.SwiperVo;
import com.lyh.entity.activity.dto.ActivityRSDto;
import com.lyh.entity.activity.dto.SwiperDeleteDto;
import com.lyh.entity.activity.dto.SwiperPageListDto;
import com.lyh.entity.constant.ActivityConstant;
import com.lyh.entity.constant.MinioConstant;
import com.lyh.service.IStudentUserService;
import com.lyh.service.ISwiperService;
import com.lyh.service.MinioService;
import com.lyh.service.WxRequest;
import com.lyh.util.TokenUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ISwiperServiceImpl extends ServiceImpl<SwiperMapper, Swiper> implements ISwiperService {

    @Resource
    private MinioService minioService;
    @Resource
    private IStudentUserService studentUserService;
    @Resource
    private WxRequest wxRequest;

    @Override
    @Transactional
    public Swiper uploadArticle(MultipartFile file, String imgUrl) {
        Swiper swiper = new Swiper();
//        MultipartFile file = dto.getFile();
        if(file!=null) {
            String swiperimg_url = minioService.putObject(MinioConstant.loopImg, file, TokenUtil.getuid32()+".png");
            if(StringUtils.hasText(swiperimg_url)){
                swiper.setImage(swiperimg_url);
            }
        }
        swiper.setUrl(imgUrl);
        swiper.setType("1");
        swiper.setIsDelete("0");
        swiper.setWxImgCheck(ActivityConstant.wx_pic_check_ing);
        if (save(swiper)){
//            StudentUser studentUser = ThreadLocalUtil.getStudentUser();
//            wxRequest.wxCheckPicSecurity("4", swiper.getId(), studentUser.getWxOpenId(), swiper.getImage());
            return swiper;
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteArticle(SwiperDeleteDto dto) {
        baseMapper.deleteSwiperByArticle(dto.getId());
    }

    @Override
    public IPage<SwiperVo> pageList(SwiperPageListDto dto) {
        return baseMapper.getPageSwiperList(new Page<>(dto.getPageCount(), dto.getPageSize()), "1");
    }

    @Override
    public List<Swiper> getList() {
        return baseMapper.getSwiperList();
    }

    @Override
    @Transactional
    public void uploadActivity(ActivityRSDto dto) {
        String activityId = dto.getId();
        List<Swiper> swipers = baseMapper.getSwiperByActivity(activityId);
        if(swipers.isEmpty()) {
            Swiper swiper = new Swiper();
            swiper.setActivityId(dto.getId());
            swiper.setImage(baseMapper.getActivityImage(activityId));
            swiper.setType("0");
            swiper.setIsDelete("0");
            save(swiper);
        } else if (Objects.equals(swipers.get(0).getIsDelete(), "0")) {
            baseMapper.updateSwiperByActivity(activityId, "1");
        } else baseMapper.updateSwiperByActivity(activityId, "0");
    }



}
