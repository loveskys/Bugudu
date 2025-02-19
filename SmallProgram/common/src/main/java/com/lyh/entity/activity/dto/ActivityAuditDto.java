package com.lyh.entity.activity.dto;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 活动发布审核接口
 */
@Data
public class ActivityAuditDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String status;
    private String cont;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(id)) {
            return new ComResponse<>().error("id不能为空");
        }
        if (!StringUtils.hasText(userId)) {
            return new ComResponse<>().error("userId不能为空");
        }
        if (!StringUtils.hasText(status)) {
            return new ComResponse<>().error("状态不能为空");
        }
        return null;
    }
}
