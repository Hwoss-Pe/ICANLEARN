package com.mapper;

import com.pojo.ChatRecords;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMapper {
//    根据id去获取所有集合
    @Select("select message_content,timestamp from chat_records where sender_id  = #{fromId} and recipient_id = #{toId}")
    List<ChatRecords> getChatRecords(Integer fromId, Integer toId);
}
