package com.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatEach {
    private List<ChatRecords> senders;
    private List<ChatRecords> recipients;
}
