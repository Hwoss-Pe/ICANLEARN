package com.controller;

import com.pojo.ChatEach;
import com.pojo.Result;
import com.service.ChatService;
import com.utils.Code;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private HttpServletRequest req;

    @PostMapping
    public Result getChatHistory(@RequestBody Map<String, String> map) {
        //    仍然从jwt里面获取id，但是需要前端传进来聊天对象的id
        String jwt = req.getHeader("token");
        String toIdStr = map.get("toId");
        int toId = Integer.parseInt(toIdStr);
        ChatEach chatRecords = chatService.getChatRecords(jwt, toId);
        if (chatRecords == null) {
            log.info("Char Records not found");
            Result.error(Code.CHAT_HISTORY_ERR, "获取聊天记录失败");
        }
        return Result.success(Code.CHAT_HISTORY_OK, chatRecords);
    }
}
