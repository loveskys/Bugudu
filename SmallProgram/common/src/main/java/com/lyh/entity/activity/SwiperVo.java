package com.lyh.entity.activity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SwiperVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String image;

    private String url;

//    private String createTime;

    private String isDelete;
}
