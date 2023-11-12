package com.utils;

import com.mapper.OccupationExplodeMapper;
import com.pojo.OccupationCollection;
import com.pojo.OccupationLike;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class CronUtil extends QuartzJobBean {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OccupationExplodeMapper occupationExplodeMapper;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("任务执行-------- {}", sdf.format(new Date()));
        List<Map<String, Object>> likeList = (List<Map<String, Object>>) redisUtil.get(RedisConstant.OCCUPATION_LIKE);
        /*统一都进去数据库*/
        if(likeList!=null){
            if(!likeList.isEmpty()){
                for (Map<String, Object> map : likeList) {
                    OccupationLike occupationLike = BeanMapUtils.mapToBean(map, OccupationLike.class);
//                    获取到数据后再去判断里面是否存在记录，思路和redis一样答辩
                    OccupationLike like = occupationExplodeMapper.getLike(occupationLike.getUserId(), occupationLike.getExplodeId());
                    if(like!=null){
//                     更新数据
                        occupationExplodeMapper.updateLike(like.getUserId(),like.getExplodeId(),occupationLike.getStatus());
                    }else {
                        //添加新数据
                        occupationExplodeMapper.insertLike(occupationLike);
                    }
                    if(occupationLike.getStatus()==1){
//                    点赞
                        occupationExplodeMapper.addLike(occupationLike.getExplodeId());
                    }else {
//                    取消点赞
                        occupationExplodeMapper.cancelLike(occupationLike.getExplodeId());
                    }
                }
                likeList.clear();
                redisUtil.set(RedisConstant.OCCUPATION_LIKE,likeList);
            }
        }
        System.out.println("定时任务执行1");
//        收藏点赞的逻辑一样



        List<Map<String, Object>> collectionList = (List<Map<String, Object>>) redisUtil.get(RedisConstant.OCCUPATION_Collection);
        if(collectionList!=null){
            if(!collectionList.isEmpty()){
                for (Map<String, Object> map : collectionList) {
                    OccupationCollection occupationCollection = BeanMapUtils.mapToBean(map, OccupationCollection.class);
//                    获取到数据后再去判断里面是否存在记录，思路和redis一样答辩
                    OccupationCollection collection = occupationExplodeMapper.getCollection(occupationCollection.getUserId(), occupationCollection.getExplodeId());
                    if(collection!=null){
//                     更新数据
                        occupationExplodeMapper.updateCollection(collection.getUserId(),collection.getExplodeId(),occupationCollection.getStatus());
                    }else {
                        //添加新数据
                        occupationExplodeMapper.insertCollection(occupationCollection);
                    }
                    if(occupationCollection.getStatus()==1){
                        occupationExplodeMapper.addCollection(occupationCollection.getExplodeId());
                    }else {
//                       这里正规的话还要去判断字段>0
                        occupationExplodeMapper.cancelCollection(occupationCollection.getExplodeId());
                    }
                }
                collectionList.clear();
                redisUtil.set(RedisConstant.OCCUPATION_Collection,collectionList);
            }
        }
        System.out.println("定时任务执行2");
    }
}
