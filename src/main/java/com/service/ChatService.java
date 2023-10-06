package com.service;

import com.pojo.ChatEach;

public interface ChatService {
    ChatEach getChatRecords(String jwt, Integer toId);
}
