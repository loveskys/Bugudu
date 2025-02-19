package com.lyh.entity.activity;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

@Data
public class DetailApplyPersonVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String userId;
    private String userHeadUrl;
    //此人是否发布人：0否，1是
    private String isIssue;
    //0不是我发布的，1是我发布的
    private String itSme;
    private String sex;
}
