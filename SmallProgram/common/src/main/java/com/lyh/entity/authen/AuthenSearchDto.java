package com.lyh.entity.authen;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author lyh
 * @since 2024-08-17
 */
@Data
public class AuthenSearchDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String schoolName;
    private String studentName;
    private String status;

    private Integer pageCount;
    private Integer pageSize;

}
