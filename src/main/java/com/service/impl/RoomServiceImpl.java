package com.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.mapper.RoomMapper;
import com.mapper.UserMapper;
import com.pojo.FriendSettings;
import com.pojo.Images;
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

    @Autowired
    private UserMapper userMapper;

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
    public Integer setGuessWords(Integer id, List<String> words, String roomCode, String type) {
        try {
            String roomKey = RedisConstant.ROOM_HISTORY + roomCode;
            boolean exist = redisUtil.hasKey(roomKey);
            String json = JSON.toJSONString(words);
            String keyWordKey;
            Integer status;
            if (type.equals("character")) {
                keyWordKey = RedisConstant.ROOM_CHARACTER_KEYWORD;
                status = 1;
            } else if (type.equals("job")) {
                keyWordKey = RedisConstant.ROOM_JOB_KEYWORD;
                status = 2;
            } else {
                throw new RuntimeException("RoomServiceImpl$setGuessWords类型出错");
            }

            if (exist) {
                //若key存在则对缓存操作
                redisUtil.hset(roomKey, keyWordKey, json);
                if (status.equals(1)) {
                    roomMapper.updateCharacterKeyWords(roomCode, json); // 更新数据库中的数据
                } else {
                    roomMapper.updateJobKeyWords(roomCode, json);
                }
            } else {
                Room room = roomMapper.isRoomExists(roomCode);
                if (room == null) {
                    return null;
                }
                if (status.equals(1)) {
                    room.setCharacterKeyWords(json);
                    roomMapper.updateCharacterKeyWords(roomCode, json);
                } else {
                    room.setJobKeyWords(json);
                    roomMapper.updateJobKeyWords(roomCode, json);
                }
                Map<String, Object> map = BeanMapUtils.beanToMap(room);
                redisUtil.hmset(roomKey, map, RedisConstant.ROOM_EXPIRE_TIME);
            }
            return words.size();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //猜关键词
    @Override
    public List<String> guessWords(Integer id, List<String> guess, String roomCode, String type) {
        String roomKey = RedisConstant.ROOM_HISTORY + roomCode;
        if (redisUtil.hasKey(roomKey) && redisUtil.hHasKey(roomKey, RedisConstant.RECEIVER_ID)) {
            if (!id.equals(redisUtil.hget(roomKey,"senderId"))){
                throw new RuntimeException("执行RoomServiceImpl$guessWords时用户id出错");
            }
        }
        String guessWordKey;
        if (type.equals("job")) {
            guessWordKey = RedisConstant.GUESS_JOB_WORDS;
        } else if (type.equals("character")) {
            guessWordKey = RedisConstant.GUESS_CHARACTER_WORDS;
        } else {
            throw new RuntimeException("RoomServiceImpl$guessWords类型出错");
        }
        String guessJson = JSON.toJSONString(guess);
        redisUtil.hset(roomKey, guessWordKey, guessJson);
        if (type.equals("job")) {
            roomMapper.updateCharacterGuessWords(roomCode, guessJson);
        } else {
            roomMapper.updateJobGuessWords(roomCode, guessJson);
        }
        return guess;
    }

    @Override
    public List<String> getKeyWordsPrompt(Integer num, String promptName) {
        String prompt = roomMapper.selectWordsPrompt(promptName);
        List<String> promptList = Arrays.asList(prompt.split("、"));
        Collections.shuffle(promptList);
        return promptList.subList(0, num);// 取前面 n 个元素
    }

    @Override
    public List<String> getKeyWords(String roomCode, String type) {
        String roomKey = RedisConstant.ROOM_HISTORY + roomCode;
        String guessKeyWords;
        if (type.equals("job")) {
            guessKeyWords = RedisConstant.GUESS_JOB_WORDS;
        } else if (type.equals("character")) {
            guessKeyWords = RedisConstant.GUESS_CHARACTER_WORDS;
        } else {
            throw new RuntimeException("RoomServiceImpl$setGuessWords类型出错");
        }

        boolean exist = redisUtil.hHasKey(roomKey, guessKeyWords);
        String json;
        if (exist) {
            //若缓存中存在则读取缓存数据
            json = (String) redisUtil.hget(roomKey, guessKeyWords);
        } else {
            //若缓存中不存在则读取数据库
            if (guessKeyWords.equals(RedisConstant.GUESS_JOB_WORDS)) {
                json = roomMapper.selectCharacterKeyWords(roomCode);
            } else {
                json = roomMapper.selectJobKeyWords(roomCode);
            }
        }
        if (json == null) {
            return null;
        }
        return JSON.parseObject(json, new TypeReference<>() {
        });
    }


    public int saveBoard(String invitationCode, Integer userId, String base64) {
        Images images = new Images();
        images.setWhiteboard(base64);
        images.setUserId(userId);
//        通过房间邀请码去获取对面的id，还有关键词
        Room room = roomMapper.getRoomByInvitationCode(invitationCode);
        Integer receiverId = room.getReceiverId();
        try {
            images.setFromId(room.getReceiverId());
            //再通过id去获取名字
            images.setFromName(userMapper.findUserById(receiverId).getUsername());
            images.setJobKeywords(room.getJobKeyWords());
            images.setCharacterKeywords(room.getCharacterKeyWords());
        } catch (Exception e) {
            return 0;
        }
        return roomMapper.saveImage(images);
    }

    public int updateKeywords(String content, Integer userId) {
//        先判断关键词有没有
        FriendSettings keyword = roomMapper.getKeyword(content);
        int i;
        if (keyword == null) {
//            加数据
            i = roomMapper.addKeyword(userId, content);
        } else {
//            采用+1
            i = roomMapper.updateKeywords(content);
        }
        return i;
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
