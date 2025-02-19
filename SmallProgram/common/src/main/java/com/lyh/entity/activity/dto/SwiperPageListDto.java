package com.lyh.entity.activity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SwiperPageListDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer pageCount;
    private Integer pageSize;
}
