package com.lyh.entity.activity.dto;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ActivityContactDto implements Serializable {
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

