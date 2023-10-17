package com.mapper;

import com.pojo.Room;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RoomMapper {
    //    判断有没有重复的邀请码
    @Select("select * from room where invitationCode = #{invitationCode}")
    Room isRoomExists(String invitationCode);

    //   创建房间
    @Insert("Insert into room(invitationCode,senderId,detected)values(#{invitationCode},#{senderId},1) ")
    int createRoom(Room room);

    //    通过邀请码修改
    @Update("Update room set receiverId = #{receiverId} where InvitationCode = #{InvitationCode}")
    int updateReceiver(String InvitationCode,Integer receiverId);
    //    通过邀请码获取房间对象
    @Select("select * from room where InvitationCode = #{InvitationCode}")
    Room getRoomByInvitationCode(String InvitationCode);

    //    通过id去获取房间
    @Select("select * from room where id = #{id}")
    Room getRoomById(Integer id);

    //   更新房间
    @Update("Update room set senderId = #{senderId},receiverId = #{receiverId} ,detected = #{detected} where id = #{id} ")
    int updateRoom(Room room);
}
