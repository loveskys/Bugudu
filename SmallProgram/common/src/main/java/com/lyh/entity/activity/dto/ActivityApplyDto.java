package com.lyh.entity.activity.dto;

import com.lyh.entity.ComResponse;
import com.lyh.util.ThreadLocalUtil;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

/**
 * 活动报名dto
 */
@Data
public class ActivityApplyDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String userId;
    private String activityId;

    //报名状态：0申请中，1成功，2不通过，3取消报名
    private String applyStatus;

    //称呼
    private String nickName;

    //联系方式
    private String contact;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(userId)) {
            userId = ThreadLocalUtil.getStudentUserId();
        }
        if (!StringUtils.hasText(activityId)) {
            return new ComResponse<>().error("活动ID不能为空");
        }
        return null;
    }
}
