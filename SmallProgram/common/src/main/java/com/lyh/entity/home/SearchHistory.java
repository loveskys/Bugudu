package com.lyh.entity.home;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lyh
 * @since 2024-08-20
 */
@Data
@TableName("search_history")
public class SearchHistory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    //用户名
    @TableField("user_id")
    private String userId;

    //搜索历史
    @TableField("search_text")
    private String searchText;

    //搜索类型：1活动，2用户
    @TableField("search_type")
    private String searchType;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

}
