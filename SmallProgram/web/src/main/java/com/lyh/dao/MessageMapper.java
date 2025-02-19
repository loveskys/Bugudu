package com.lyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.entity.home.msg.Message;
import com.lyh.entity.home.msg.MessageVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * @author lyh
 * @since 2024-08-26
 */
public interface MessageMapper extends BaseMapper<Message> {

    List<MessageVo> getMyMsgList(@Param("msgType") String msgType, @Param("userId") String userId);

    IPage<MessageVo> getPageList(@Param("page") IPage<MessageVo> page, @Param("msgType") String msgType);



    @Select("select id from message_read where msg_id = #{msgId} and user_id = #{userId};")
    String getRead(@Param("msgId") String msgId, @Param("userId") String userId);

    @Insert("INSERT INTO message_read (msg_id, user_id) values (#{msgId}, #{userId});")
    void insertRead(@Param("msgId") String msgId, @Param("userId") String userId);

    void insertAllRead(@Param("userId") String userId);
}
