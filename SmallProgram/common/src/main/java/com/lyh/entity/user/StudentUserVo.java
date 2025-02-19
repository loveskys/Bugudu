package com.lyh.entity.user;

import com.lyh.entity.authen.Authen;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author lyh
 * @since 2024-08-25
 */
@Data
public class StudentUserVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Authen authen;

    private String[] authenArray;

    private StudentUser studentUser;

}
