package com.service;

import com.pojo.Room;

import java.util.List;

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

    //存储给房主猜的选词
    Integer setGuessWords(Integer id, List<String> words, String roomCode);

    //判断房主猜词是否正确
    Boolean guessWords(Integer id, List<String> guess, String roomCode);



}
