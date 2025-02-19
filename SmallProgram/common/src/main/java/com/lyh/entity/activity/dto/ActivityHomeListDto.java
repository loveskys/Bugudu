package com.lyh.entity.activity.dto;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 小程序首页搜索用
 * @author lyh
 * @since 2024-08-17
 */
@Data
public class ActivityHomeListDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //0=推荐，1=娱乐，2=出游，3=运动
    private String tag;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(tag)) {
            return new ComResponse<>().error("请传入tag");
        }
        return null;
    }
}
