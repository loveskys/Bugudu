package com.lyh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.entity.home.SearchHistory;
import com.lyh.entity.home.SearchHistoryDto;
import java.util.List;

/**
 * @author lyh
 * @since 2024-08-22
 */
public interface ISearchHistoryService extends IService<SearchHistory> {


    List<?> toSearch(SearchHistoryDto dto);

    List<SearchHistory> getSearchHistory();

}
