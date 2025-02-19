package com.lyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.dao.SearchHistoryMapper;
import com.lyh.entity.activity.ActivityVo;
import com.lyh.entity.home.SearchHistory;
import com.lyh.entity.home.SearchHistoryDto;
import com.lyh.entity.user.StudentUserVo;
import com.lyh.service.IActivityService;
import com.lyh.service.ISearchHistoryService;
import com.lyh.service.IStudentUserService;
import com.lyh.util.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author lyh
 * @since 2023-08-25
 */
@Service
public class ISearchHistoryServiceImpl extends ServiceImpl<SearchHistoryMapper, SearchHistory> implements ISearchHistoryService {

    @Resource
    private IActivityService iActivityService;
    @Resource
    private IStudentUserService iStudentUserService;

    @Override
    @Transactional
    public List<?> toSearch(SearchHistoryDto dto) {
        if("1".equals(dto.getSearchType())) {
            List<ActivityVo> list = iActivityService.getByThemeList(dto.getSearchText());
            saveHistory(dto);
            return list;
        }
        if("2".equals(dto.getSearchType())) {
            List<StudentUserVo> list = iStudentUserService.getByNameList(dto.getSearchText());
            saveHistory(dto);
            return list;
        }
        return null;
    }


    @Override
    public List<SearchHistory> getSearchHistory() {
        QueryWrapper<SearchHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SearchHistory::getUserId, ThreadLocalUtil.getStudentUserId()).orderByDesc(SearchHistory::getCreateTime)
                .last("LIMIT 7");
        List<SearchHistory> list = list(queryWrapper);
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return null;
    }


    private void saveHistory(SearchHistoryDto dto) {
        QueryWrapper<SearchHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SearchHistory::getUserId, ThreadLocalUtil.getStudentUserId())
                .eq(SearchHistory::getSearchText, dto.getSearchText())
                .eq(SearchHistory::getSearchType, dto.getSearchType());
        List<SearchHistory> list = list(queryWrapper);
        if(list.isEmpty()) {
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setUserId(ThreadLocalUtil.getStudentUserId());
            searchHistory.setSearchText(dto.getSearchText());
            searchHistory.setSearchType(dto.getSearchType());
            save(searchHistory);
        }
    }
}
