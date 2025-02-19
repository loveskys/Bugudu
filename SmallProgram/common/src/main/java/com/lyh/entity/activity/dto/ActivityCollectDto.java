package com.lyh.entity.activity.dto;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

/**
 * 活动报名dto
 */
@Data
public class ActivityCollectDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String activityId;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(activityId)) {
            return new ComResponse<>().error("活动ID不能为空");
        }
        return null;
    }
}
