package com.lyh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.entity.authen.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * @author lyh
 * @since 2024-07-30
 */
public interface IAuthenService extends IService<Authen> {

    Authen submit(MultipartFile file, Map<String, String> allParams);

    Authen resubmit(Authen dto);

    Authen detail(AuthenDetailDto dto);

    IPage<AuthenVo> list(AuthenSearchDto dto);

    void updateStatus(AuthenUpdateDto dto);
}
