package com.service.impl;

import com.mapper.ChatMapper;
import com.service.ChatService;
import com.pojo.ChatEach;
import com.pojo.ChatRecords;
import com.utils.JwtUtils;
import com.utils.TimestampComparator;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;
    @Override
    public ChatEach getChatRecords(String jwt, Integer toId) {
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer fromId = (Integer) claims.get("id");
        List<ChatRecords> sendList = chatMapper.getChatRecords(fromId, toId);
        sendList.sort(new TimestampComparator());
        List<ChatRecords> recipientList = chatMapper.getChatRecords(toId, fromId);
        recipientList.sort(new TimestampComparator());

        ChatEach chat_each = new ChatEach();
        chat_each.setSenders(sendList);
        chat_each.setRecipients(recipientList);
        return chat_each;
    }
}