package com.lyh.entity.activity.dto;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

/**
 * pc端搜索活动列表
 * @author lyh
 * @since 2024-08-28
 */
@Data
public class ActivityDetailDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //活动ID
    private String id;


    public ComResponse<?> validate() {
        if (!StringUtils.hasText(id)) {
            return new ComResponse<>().error("id不能为空");
        }
        return null;
    }
}
