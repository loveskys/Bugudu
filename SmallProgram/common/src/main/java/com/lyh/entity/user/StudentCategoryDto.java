package com.lyh.entity.user;

import com.lyh.entity.ComResponse;
import com.lyh.util.ThreadLocalUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;

@Data
public class StudentCategoryDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String id;
    private String category;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(id)) {
            return new ComResponse<>().error("请填写用户id");
        }
        if (!StringUtils.hasText(category)) {
            return new ComResponse<>().error("请填写用户类别码");
        }
        return null;
    }
}
