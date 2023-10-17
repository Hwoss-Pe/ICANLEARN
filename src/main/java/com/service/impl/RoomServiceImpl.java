package com.service.impl;

import com.mapper.RoomMapper;
import com.pojo.Room;
import com.service.RoomService;
import com.utils.RedisConstant;
import com.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomMapper roomMapper;

    private final RedisUtil redisUtil;

    @Autowired
    public RoomServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

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
    public Integer setGuessWords(Integer id, List<String> words, String roomCode) {
        String roomKey = RedisConstant.ROOM_HISTORY + roomCode;
        boolean exist = redisUtil.hasKey(roomKey);
        if (exist){
            //若key存在则对缓存操作
            redisUtil.hset(roomKey,RedisConstant.ROOM_KEYWORD,words);
        }else {
            //若缓存中不存在则读取数据库

        }

        return null;
    }

    @Override
    public Boolean guessWords(Integer id, List<String> guess,String roomCode) {
        String roomKey = RedisConstant.ROOM_HISTORY + roomCode;
        boolean exist = redisUtil.hasKey(roomKey);
        if (exist){
            //若缓存中存在则读取缓存数据

        }else {
            //若缓存中不存在则读取数据库

        }
        return null;
    }

    @Override
    public boolean updateRoom(Room room) {
        int i = roomMapper.updateRoom(room);

        return i>0;
    }

    @Override
    public Room getRoomById(Integer id) {
        return roomMapper.getRoomById(id);
    }


    @Override
    public boolean isRoomExists(String invitationCode) {
        Room roomExists = roomMapper.isRoomExists(invitationCode);
        return roomExists != null;
    }
}
