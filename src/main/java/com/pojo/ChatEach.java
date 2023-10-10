package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatEach {
//    一对一聊天
    private List<ChatRecords> senders;
    private List<ChatRecords> recipients;
}
