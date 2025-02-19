package com.lyh.entity.activity.dto;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * 小程序我的搜索用
 * @author lyh
 * @since 2024-08-17
 */
@Data
public class ActivityMyListDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //活动名称
    private String theme;
    //活动ID
    private String id;
    //审核状态：0申请中，1通过，2不通过
    private String status;

    //1我参与的，2我发布的，3我的收藏
    private String tag;


    //1=查看他人的主页的参与的活动列表
    private String other;
    //他人用户ID
    private String otherUserId;

    private Integer pageCount;
    private Integer pageSize;

}
