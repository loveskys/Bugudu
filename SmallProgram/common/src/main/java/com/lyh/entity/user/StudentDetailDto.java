package com.lyh.entity.user;

import com.lyh.entity.ComResponse;
import com.lyh.util.ThreadLocalUtil;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

@Data
public class StudentDetailDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String userId;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(userId)) {
            userId = ThreadLocalUtil.getStudentUserId();
        }
        return null;
    }


}
