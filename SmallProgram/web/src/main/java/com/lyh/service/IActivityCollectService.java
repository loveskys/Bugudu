package com.lyh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.entity.activity.ActivityCollect;
import com.lyh.entity.activity.dto.ActivityApplyDto;

/**
 * @author lyh
 * @since 2024-08-20
 */
public interface IActivityCollectService extends IService<ActivityCollect> {

    //活动报名
    void collect(ActivityApplyDto dto);

}
