package com.service;

import com.pojo.Room;

public interface RoomService {
    boolean  isRoomExists(String invitationCode);
//创建房间
    boolean createRoom(Room room);
//加入房间
    boolean updateReceiver(String InvitationCode,Integer receiverId);
//
Room getRoomByInvitationCode(String InvitationCode);

    Room getRoomById(Integer id);

    boolean updateRoom(Room room);
}
