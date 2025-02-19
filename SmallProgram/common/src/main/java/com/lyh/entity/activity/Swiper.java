package com.lyh.entity.activity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("swiper")
public class Swiper implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("image")
    private String image;

    @TableField("url")
    private String url;

    @TableField("activity_id")
    private String activityId;

    //0 活动，1 文章
    @TableField("type")
    private String type;

//    @TableField(value = "create_time", fill = FieldFill.INSERT)
//    private LocalDateTime createTime;

    @TableField("is_delete")
    private String isDelete;

    //微信图片安全检测状态: 0未检测,1检测中,2通过,3不通过
    @TableField("wx_img_check")
    private String wxImgCheck;

}
