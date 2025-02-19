package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyh.entity.wxresponse.Wxcheckbizreco;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface WxcheckbizrecoMapper extends BaseMapper<Wxcheckbizreco> {

    @Select("select * from wxcheckbizreco where trace_id = #{traceId};")
    Wxcheckbizreco getByTraceId(@Param("traceId") String traceId);

}
