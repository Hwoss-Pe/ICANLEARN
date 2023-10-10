package com.service.impl;


import com.mapper.RadioWaveMapper;
import com.pojo.User;
import com.service.RadioWaveService;
import com.utils.Identification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RadioWaveServiceImpl implements RadioWaveService {

    @Autowired
    private RadioWaveMapper radioWaveMapper;

    @Override
    public void sendWave(Integer id, Integer sendId) {
        //添加发送电波的数据
        radioWaveMapper.insertToFriendList(id, sendId, Identification.WAITING);
    }

    @Override
    public void acceptWave(Integer id, Integer addId) {
        //修改关系表中的等待状态
        radioWaveMapper.updateWaitingToFriend(id, addId, Identification.FRIEND);
    }

    @Override
    public List<User> queryWaitingList(Integer id) {
        //获取该id关系表中状态码为等待的用户
        return queryUserList(id,Identification.WAITING);
    }

    @Override
    public List<User> queryFriendsList(Integer id) {
        //获取该id关系表中状态码为好友的用户
        return queryUserList(id,Identification.FRIEND);
    }

    private List<User> queryUserList(Integer id,int identification) {
        //获取关系表中指定状态的用户
        List<Integer> idList = radioWaveMapper.selectWaitingByIdent(id, identification);

        if (idList != null) {
            //根据List返回结果
            return radioWaveMapper.selectUserById(idList);
        }
        return new ArrayList<>();
    }
}