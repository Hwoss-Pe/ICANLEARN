package com.service;


import com.pojo.OccupationExplode;
import com.pojo.SearchHistory;
import com.pojo.ToDo;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface OccupationExplodeService {

    OccupationExplode getOccupationById(Integer id);

    List<OccupationExplode> getOccupation(Integer userId,String keyword);

    List<String> getOccupation(String keyword);

    List<SearchHistory> historyList(Integer userId);

    List<OccupationExplode> getOccupations();

    List<OccupationExplode> getOccupationsByES(Integer userId,String keyword);

    List<String> getAssociate(String keyword);
    //    增加某个职业信息的点赞
    void addLike(Integer userId,Integer id);

    //    减少某个职业信息的点赞
    void cancelLike(Integer userId,Integer id);

    //    增加某个职业信息的收藏
    void addCollection(Integer userId,Integer id);

    //    减少某个职业信息的收藏
    void cancelCollection(Integer userId,Integer id);

//  添加todo
    int addPlan(ToDo toDo,Integer userId);

    int updatePlan(Integer userId,String coordinate,Integer stage);

    ToDo getPlan(Integer userId,Integer stage);
}
