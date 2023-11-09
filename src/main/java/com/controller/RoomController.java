package com.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.pojo.Result;
import com.pojo.Room;
import com.service.RoomService;
import com.utils.Code;
import com.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private HttpServletRequest req;

    @GetMapping("/create")
    public Result getInviteCode() {
        // 邀请码
        String testCode = null;
        boolean flag = true;
        while (flag) {
            // 生成一个六位邀请码,小写字母换成大写
            testCode = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
            if (!roomService.isRoomExists(testCode)) {
                flag = false;
                System.out.println(testCode);
            }
        }
        Room room = new Room();
        String jwt = req.getHeader("token");
        Integer userId = JwtUtils.getId(jwt);
//生成邀请码后要创建对应的房间，这里房间的id就是一个tokenId作为发起者就可以了，接收者先弄为空
        room.setSenderId(userId);
        room.setInvitationCode(testCode);
        if (roomService.createRoom(room)) {
            Room roomTmp = roomService.getRoomByInvitationCode(testCode);
            return Result.success(Code.CREATE_ROOM_OK, roomTmp);
        } else {
            return Result.error(Code.CREATE_ROOM_ERR, "创建房间失败");
        }

    }


    //好友输入邀请码后进入房间
    @PutMapping("/in-room")
    public Result inRoom(@RequestBody Map<String, String> map) {
        String jwt = req.getHeader("token");
        Integer userId = JwtUtils.getId(jwt);
        String InvitationCode = map.get("InvitationCode");
//根据邀请码进入房间，也即是修改房间的收到者为当前id，排除自己
//        先判断房间有没有人

        Room roomTmp = roomService.getRoomByInvitationCode(InvitationCode);
        if (roomTmp == null) {
            return Result.error(Code.JOIN_ROOM_ERR, "邀请码错误");
        }
        if (roomTmp.getReceiverId() != null && roomTmp.getReceiverId() > 0) {
//证明房间已经满了
            //判断是否为本人
            if (roomTmp.getReceiverId().equals(userId)) {
                return Result.error(Code.JOIN_ROOM_ERR, "您已经在房间中");
            }
            return Result.error(Code.JOIN_ROOM_ERR, "房间满了");
        }
        if (roomTmp.getDetected() != null && roomTmp.getDetected() == 1) {
            return Result.error(Code.JOIN_ROOM_ERR, "房主已经退出");
        }
        boolean update = roomService.updateReceiver(InvitationCode, userId);
//   返回修改后的房间
        Room room = roomService.getRoomByInvitationCode(InvitationCode);
        if (update) {
            return Result.success(Code.JOIN_ROOM_OK, room);
        } else {
            return Result.error(Code.JOIN_ROOM_ERR, "加入房间失败");
        }
    }

    //   退出房间的操作，分为两种情况，一种是客人退出，该接收id，一种是房主退出，逻辑删除该房间
    @DeleteMapping("/exit-room")
    public Result exitRoom(@RequestBody Map<String, Integer> map) {
        String jwt = req.getHeader("token");
        Integer userId = JwtUtils.getId(jwt);
        Integer roomId = map.get("roomId");
//通过当前房间id去获取
        Room room = roomService.getRoomById(roomId);
        Integer senderId = room.getSenderId();
        Integer receiverId = room.getReceiverId();
        if (userId.equals(receiverId)) {
//            设置房间的人为0
            String invitationCode = room.getInvitationCode();
            roomService.updateReceiver(invitationCode, 0);
            return Result.success(Code.EXIT_ROOM_OK, "用户退出");
        }
        if (userId.equals(senderId)) {
//            逻辑删除房间，这里需要两个一起退出,并且通知别人退出。
            room.setDetected(1);
            roomService.updateRoom(room);
//            退出房间，返回信息让前端发送web的close请求，关闭请求
            return Result.success(Code.EXIT_ROOM_OK, "房主退出");
        }
        return Result.error(Code.EXIT_ROOM_ERR, "房间内人员id出错");
    }

    //获取关键词推荐
    @GetMapping("/prompt/{num}")
    public Result getKeyWordsPrompt(@PathVariable Integer num) {
        try {
            List<String> character = roomService.getKeyWordsPrompt(num, "character");
            List<String> occupation = roomService.getKeyWordsPrompt(num, "occupation");

            Map<String, List<String>> map = new HashMap<>();
            map.put("character", character);
            map.put("occupation", occupation);
            return Result.success(Code.KEY_WORDS_PROMPT_OK, map);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.KEY_WORDS_PROMPT_ERR, "获取关键词推荐失败");
        }
    }


    //好友给出对应的关键词
    @PostMapping("/set_guess/{type}")
    public Result setGuessWords(@RequestBody Map<String, List<String>> map, @PathVariable String type) {
        try {
            String jwt = req.getHeader("token");
            Integer id = JwtUtils.getId(jwt);
            List<String> words = map.get("words");
            List<String> code = map.get("roomCode");
            String roomCode = code.get(0);
            //将选词存储起来，返回关键词的个数
            Integer num = roomService.setGuessWords(id, words, roomCode, type);
            return Result.success(Code.SET_GUESS_WORDS_OK, num);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.SET_GUESS_WORDS_ERR, "选词失败");
        }


    }

    //房主填入猜的关键词，校对是否正确
    @PostMapping("/check_guess/{type}")
    public Result checkGuess(@RequestBody Map<String, List<String>> map, @PathVariable String type) {
        try {
            String jwt = req.getHeader("token");
            Integer id = JwtUtils.getId(jwt);
            List<String> guess = map.get("guess");
            List<String> code = map.get("roomCode");
            String roomCode = code.get(0);
            //猜选词，返回list类型
            List<String> guessWords = roomService.guessWords(id, guess, roomCode, type);
            List<String> keyWords = roomService.getKeyWords(roomCode, type);
            List<String> intersection = (List<String>) CollectionUtil.intersection(keyWords,guessWords);
            Map<String, List<String>> listMap = new HashMap<>();
            listMap.put("intersection", intersection);
            listMap.put("keyWords", keyWords);

            return Result.success(Code.GUESS_WORDS_OK, listMap);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Code.GUESS_WORDS_ERR, "对比关键词出错");
        }
    }


    //保存画板
    @PutMapping("/save")
    public Result saveBoard(@RequestBody Map<String, String> map) {
        String invitationCode = map.get("invitationCode");
        String token = req.getHeader("token");
        String base64 = map.get("whiteboard");
        Integer userId = JwtUtils.getId(token);
//       保存的时候就把画板，获取对应的关键词，然后进行保存

        int i = roomService.saveBoard(invitationCode, userId, base64);

        return i > 0 ? Result.success(Code.WHITEBOARD_SAVE_OK, "保存成功") :
                Result.error(Code.WHITEBOARD_SAVE_ERR, "保存失败");
    }

}
