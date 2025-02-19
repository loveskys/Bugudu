package com.lyh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.entity.activity.ActivityApply;
import com.lyh.entity.activity.dto.*;

/**
 * @author lyh
 * @since 2024-08-20
 */
public interface IActivityApplyService extends IService<ActivityApply> {

    //活动报名
    int apply(ActivityApplyDto dto);

    //取消报名
    void applyCancel(ActivityApplyDto dto);

}
