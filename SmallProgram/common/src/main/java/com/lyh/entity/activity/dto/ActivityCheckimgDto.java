package com.lyh.entity.activity.dto;

import com.lyh.entity.ComResponse;
import com.lyh.entity.activity.ActivityCheckimg;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ActivityCheckimgDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String checkimgUrl;
    private String activityId;

    public ComResponse<?> validate() {
        return null;
    }
}
