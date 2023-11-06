package com.service.impl;

import com.mapper.CartoonMapper;
import com.pojo.Cartoon;
import com.pojo.MyJob;
import com.service.CartoonService;
import com.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CartoonServiceImpl implements CartoonService {

    @Autowired
    private CartoonMapper cartoonMapper;

    @Override
    @Transactional
    public int addCartoon(Cartoon cartoon) {
        Integer userId = cartoon.getUserId();
        cartoonMapper.deleteCartoon(userId);
        cartoonMapper.deleteJobList(userId);
        String[] encodeAd = encode(cartoon.getAd());
        String[] encodeDisAd = encode(cartoon.getDisAd());
        cartoon.setAd(encodeAd);
        cartoon.setDisAd(encodeDisAd);
//采用覆盖，先删再加
        int i = cartoonMapper.addCartoon(cartoon);
        List<MyJob> myJobList = cartoon.getMyJobList();
        for (MyJob job : myJobList) {
            job.setUserId(cartoon.getUserId());
            cartoonMapper.addMyJob(job);
        }
        return i;
    }

    public Cartoon getCartoon(Integer userId) {
        Cartoon cartoon = cartoonMapper.getCartoon(userId);
        cartoon.setAd(decode(cartoon.getAd()));
        cartoon.setDisAd(decode(cartoon.getDisAd()));
        List<MyJob> jobList = cartoonMapper.getJobList(userId);
        cartoon.setMyJobList(jobList);
        return cartoon;
    }

    @Transactional
    public boolean deleteCartoon(String jwt) {
        Integer userId = JwtUtils.getId(jwt);
//        通过userId去删除对应的数据
        int i = cartoonMapper.deleteCartoon(userId);
//        还要继续删另一个表
        int j = cartoonMapper.deleteJobList(userId);
        return i > 0 && j > 0;
    }


    public String[] encode(String[] data) {
//        获取所有元素并且加密后放到第一个元素里面
        String[] strings = {""};
        for (String datum : data) {
            strings[0] += datum + "&*";
        }
        return strings;
    }

    public String[] decode(String[] strings) {
        // 解析出被加密的字符串
        String encoded = strings[0];

        // 使用分隔符将字符串分割成单独的元素

        // 返回还原后的数组
        return encoded.split("&\\*");
    }

}