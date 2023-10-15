package com.service.impl;

import com.mapper.RoomMapper;
import com.pojo.Room;
import com.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomMapper roomMapper;

    @Override
    public boolean createRoom(Room room) {
        return roomMapper.createRoom(room) > 0;
    }


    @Override
    public boolean updateReceiver(String InvitationCode, Integer receiverId) {
//        这里要去判断不能进入自己的房间
        Room room = roomMapper.getRoomByInvitationCode(InvitationCode);
        Integer senderId = room.getSenderId();
        if (senderId.equals(receiverId)) {
            return false;
        }
        int i = roomMapper.updateReceiver(InvitationCode, receiverId);
        return i > 0;
    }

    @Override
    public Room getRoomByInvitationCode(String InvitationCode) {
        return roomMapper.getRoomByInvitationCode(InvitationCode);
    }

    @Override
    public boolean isRoomExists(String invitationCode) {
        Room roomExists = roomMapper.isRoomExists(invitationCode);
        return roomExists != null;
    }
}
