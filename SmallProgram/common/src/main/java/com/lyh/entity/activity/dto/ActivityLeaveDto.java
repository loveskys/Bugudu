package com.lyh.entity.activity.dto;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

/**
 * 活动留言Dto
 * @author lyh
 * @since 2024-08-30
 */
@Data
public class ActivityLeaveDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //留言ID
    private String id;

    //发送的留言内容
    private String leaveCont;

    //活动ID
    private String activityId;

    public ComResponse<?> validate() {
//        if (!StringUtils.hasText(activityId)) {
//            return new ComResponse<>().error("活动id不能为空");
//        }
        return null;
    }
}
