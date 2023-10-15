package com.controller;

import com.pojo.Result;
import com.pojo.Room;
import com.service.RoomService;
import com.utils.Code;
import com.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("room")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private HttpServletRequest req;

    @GetMapping("/create")
    public Result getInviteCode() throws Exception {
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
            return Result.success(Code.CREATE_ROOM_OK, room);
        } else {
            return Result.error(Code.CREATE_ROOM_ERR, "创建房间失败");
        }

    }


    //好友输入邀请码后进入房间
    @PutMapping("/inRoom")
    public Result inRoom(@RequestBody Map<String, String> map) {
        String jwt = req.getHeader("token");
        Integer userId = JwtUtils.getId(jwt);
        String InvitationCode = map.get("InvitationCode");
//根据邀请码进入房间，也即是修改房间的收到者为当前id，排除自己
//        先判断房间有没有人
        Room roomTmp = roomService.getRoomByInvitationCode(InvitationCode);
        if (roomTmp.getReceiverId() > 0) {
//证明房间已经满了
            return Result.error(Code.JOIN_ROOM_ERR, "房间满了");
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

}
