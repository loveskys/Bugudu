package com.lyh.entity.home;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author lyh
 * @since 2024-08-20
 */
@Data
public class SearchHistoryDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //搜索历史
    private String searchText;

    //搜索类型，1活动，2用户
    private String searchType;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(searchText)) {
            return new ComResponse<>().error("搜索字符串不能为空");
        }
        if (!StringUtils.hasText(searchType)) {
            return new ComResponse<>().error("搜索类型不能为空");
        }
        return null;
    }

}
