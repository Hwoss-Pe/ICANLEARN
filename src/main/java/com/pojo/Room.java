package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private Integer id;
    private String invitationCode;
    private Integer senderId;
    private Integer receiverId;
    private Integer detected;
    private String characterKeyWords;
    private String characterGuessWords;
    private String jobKeyWords;
    private String jobGuessWords;
    private Timestamp createTime;
}
