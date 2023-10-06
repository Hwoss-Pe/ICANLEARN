package com.mapper;

import com.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RadioWaveMapper {

    //给指定id添加新的关系
    @Insert("insert into friend_list(user_id, friend_id, identification) " +
            "values(#{id},#{sendId},#{identification})")
    void insertToFriendList(Integer id, Integer sendId, Integer identification);

    @Update("update friend_list set identification = #{identification} " +
            "where user_id = #{id} and friend_id = #{addId}")
    void updateWaitingToFriend(Integer id, Integer addId, Integer identification);

    @Select("select friend_id from friend_list where user_id = #{id} and identification = #{identification}")
    List<Integer> selectWaitingByIdent(Integer id, Integer identification);


    List<User> selectUserById(List<Integer> idList);
}
