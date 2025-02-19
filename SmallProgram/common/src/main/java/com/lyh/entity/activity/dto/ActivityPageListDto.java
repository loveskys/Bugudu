package com.lyh.entity.activity.dto;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * PC端
 * @author lyh
 * @since 2024-08-17
 */
@Data
public class ActivityPageListDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //活动名称
    private String theme;

    //审核状态 0申请中，1通过，2不通过
//    private String status;

    //3=违规图片
    private Integer wxCheck;

    //0全部，1正在进行的活动，2已过期活动，3已删除的活动
    private Integer tag;

    private Integer pageCount;
    private Integer pageSize;


    public ComResponse<?> validate() {
        System.out.println(wxCheck);
        if (wxCheck.equals("3")) {
            System.out.println("参数："+wxCheck);
            tag = 0;
        }
        return null;
    }
}
