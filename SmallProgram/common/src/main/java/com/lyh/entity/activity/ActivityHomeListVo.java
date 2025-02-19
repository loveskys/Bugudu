package com.lyh.entity.activity;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ActivityHomeListVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<Swiper> swiperList;

    private List<ActivityVo> activityList;

}
