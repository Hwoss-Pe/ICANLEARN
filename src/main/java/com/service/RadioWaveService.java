package com.service;

import com.pojo.User;

import java.util.List;

public interface RadioWaveService {

    //发送电波
    void sendWave(Integer id, Integer sendId);

    //添加好友
    void acceptWave(Integer id, Integer addId);

    //查询等待列表
    List<User> queryWaitingList(Integer id);

    //查询好友列表
    List<User> queryFriendsList(Integer id);
}