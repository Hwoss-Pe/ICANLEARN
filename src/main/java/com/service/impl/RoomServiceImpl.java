package com.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.mapper.RoomMapper;
import com.pojo.Room;
import com.service.RoomService;
import com.utils.BeanMapUtils;
import com.utils.RedisConstant;
import com.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomMapper roomMapper;

    private final RedisUtil redisUtil;

    @Autowired
    public RoomServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    //创建房间
    @Override
    public boolean createRoom(Room room) {
        if (roomMapper.createRoom(room) > 0) {
            //若数据库存入成功则操作缓存
            String roomCode = room.getInvitationCode();
            String roomKey = RedisConstant.ROOM_HISTORY + roomCode;

            Map<String, Object> map = BeanMapUtils.beanToMap(room);
            //将map存储到Redis中key为roomKey对应的哈希数据
            redisUtil.hmset(roomKey, map, RedisConstant.ROOM_EXPIRE_TIME);
            return true;
        }
        return false;
    }


    //更新状态
    @Override
    public boolean updateReceiver(String invitationCode, Integer receiverId) {
        String roomKey = RedisConstant.ROOM_HISTORY + invitationCode;
        //这里判断不能进入自己的房间
        Room room = getRoomByInvitationCode(invitationCode);

        Integer senderId = room.getSenderId();
        if (senderId.equals(receiverId)) {
            return false;
        }
        return roomMapper.updateReceiver(invitationCode, receiverId) > 0 && redisUtil.hset(roomKey, RedisConstant.RECEIVER_ID, receiverId);
    }

    @Override
    public Room getRoomByInvitationCode(String invitationCode) {
        String roomKey = RedisConstant.ROOM_HISTORY + invitationCode;
        boolean exist = redisUtil.hasKey(roomKey);
        if (exist) {
            Map<String, Object> get = redisUtil.hmget(roomKey);
            return BeanMapUtils.mapToBean(get, Room.class);
        }
        return roomMapper.getRoomByInvitationCode(invitationCode);
    }

    //将keyWords存入缓存以及数据库
    @Override
    public Integer setGuessWords(Integer id, List<String> words, String roomCode) {
        try {
            String roomKey = RedisConstant.ROOM_HISTORY + roomCode;
            boolean exist = redisUtil.hasKey(roomKey);
            String json = JSON.toJSONString(words);
            if (exist) {
                //若key存在则对缓存操作
                redisUtil.hset(roomKey, RedisConstant.ROOM_KEYWORD, json);
                roomMapper.updateKeyWords(roomCode, json); // 更新数据库中的数据
            } else {
                Room room = roomMapper.isRoomExists(roomCode);
                if (room == null) {
                    return null;
                }
                room.setKeyWords(json);
                Map<String, Object> map = BeanMapUtils.beanToMap(room);
                //判断数据库是否有该数据库，后添加缓存
                roomMapper.updateKeyWords(roomCode, json);
                redisUtil.hmset(roomKey, map, RedisConstant.ROOM_EXPIRE_TIME);
            }
            return words.size();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //猜关键词，获取好友设置的关键词，与猜的关键词进行比较，返回重复的关键词
    @Override
    public List<String> guessWords(Integer id, List<String> guess, String roomCode) {
        String roomKey = RedisConstant.ROOM_HISTORY + roomCode;
        String guessJson = JSON.toJSONString(guess);
        redisUtil.hset(roomKey,RedisConstant.GUESS_WORDS,guessJson);
        roomMapper.updateGuessWords(roomCode,guessJson);
        List<String> list = getKeyWords(roomCode);
        return (List<String>) CollectionUtil.intersection(list, guess);
    }

    @Override
    public List<String> getKeyWordsPrompt(Integer num, String promptName) {
        String prompt = roomMapper.selectWordsPrompt(promptName);
        List<String> promptList = Arrays.asList(prompt.split("、"));
        Collections.shuffle(promptList);
        return promptList.subList(0, num);// 取前面 n 个元素
    }

    @Override
    public List<String> getKeyWords(String roomCode) {
        String roomKey = RedisConstant.ROOM_HISTORY + roomCode;
        boolean exist = redisUtil.hHasKey(roomKey, RedisConstant.ROOM_KEYWORD);
        String json;
        if (exist) {
            //若缓存中存在则读取缓存数据
            json = (String) redisUtil.hget(roomKey, RedisConstant.ROOM_KEYWORD);
        } else {
            //若缓存中不存在则读取数据库
            json = roomMapper.selectKeyWords(roomCode);
        }
        if (json == null) {
            return null;
        }
        return JSON.parseObject(json, new TypeReference<>() {
        });
    }

    //更新房间状态
    @Override
    public boolean updateRoom(Room room) {
        String roomKey = RedisConstant.ROOM_HISTORY + room.getInvitationCode();
        Integer senderId = room.getSenderId();
        if (!redisUtil.hasKey(roomKey)) {
            return false;
        }

        return roomMapper.updateRoom(room) > 0 && redisUtil.hset(roomKey, RedisConstant.RECEIVER_ID, senderId);
    }

    //根据id获取房间
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
