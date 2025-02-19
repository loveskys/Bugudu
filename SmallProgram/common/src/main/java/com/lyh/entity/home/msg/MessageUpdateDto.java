package com.lyh.entity.home.msg;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author lyh
 * @since 2024-08-26
 */
@Data
public class MessageUpdateDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(id)) {
            return new ComResponse<>().error("消息id不能为空");
        }
        return null;
    }

}
