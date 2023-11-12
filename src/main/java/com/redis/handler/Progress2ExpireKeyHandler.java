package com.redis.handler;

import com.mapper.OccupationExplodeMapper;
import com.pojo.PersonalProgress;
import com.redis.RedisExpiredKeyHandler;
import com.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Progress2ExpireKeyHandler implements RedisExpiredKeyHandler {
    private RedisUtil redisUtil;

    private  OccupationExplodeMapper occupationExplodeMapper;

    public Progress2ExpireKeyHandler(RedisUtil redisUtil, OccupationExplodeMapper occupationExplodeMapper) {
        this.redisUtil = redisUtil;
        this.occupationExplodeMapper = occupationExplodeMapper;
    }

    @Override
    public void handleExpiredKey(String expiredKey) {
        int id = Integer.parseInt(expiredKey.substring(RedisConstant.OCCUPATION_VALUES_2.length()));
        //获取备份key
        String backupKey = RedisConstant.OCCUPATION_VALUES_COPY_2 + id;
        Map<String, Object> map = this.redisUtil.hmget(backupKey);
        PersonalProgress progress = BeanMapUtils.mapToBean(map, PersonalProgress.class);
        //        这里应该去判断里面原本有没有数据
        PersonalProgress personalProgress = occupationExplodeMapper.getProgress(progress);
        progress.convertValuesListToValues();
        if(personalProgress==null){
            occupationExplodeMapper.addPersonalProgress(progress);
        }else {
//            更新
            occupationExplodeMapper.updateProgress(progress);
        }
    }
}
