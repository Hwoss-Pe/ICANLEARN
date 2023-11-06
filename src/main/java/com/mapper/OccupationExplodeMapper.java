package com.mapper;


import com.pojo.OccupationExplode;
import com.pojo.SearchHistory;
import com.pojo.ToDo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OccupationExplodeMapper {
//    通过id去获取职业信息
    @Select("select * from occupation_explode where  id = #{id}")
     OccupationExplode getOccupationById(Integer id);

//    用like去获取，后面改成es
    @Select("select * from occupation_explode where job like CONCAT('%', #{keyword}, '%')")
    List<OccupationExplode> getOccupation(String keyword);

//    添加到历史记录
    @Insert("insert into search_history  (user_id, content) values(#{userId}, #{content})")
     int addHistory(Integer userId ,String content);

//    获取之前的历史的记录
    @Select("select * from search_history where user_id = #{userId}")
    List<SearchHistory> historyList(Integer userId);
//    获取所有的职业
    @Select("SELECT * from occupation_explode")
    List<OccupationExplode> getOccupations();

//    增加某个职业信息的点赞
    @Update("update occupation_explode set likes = likes + 1 where id = #{id}")
    void addLike(Integer id);

    //    减少某个职业信息的点赞
    @Update("update occupation_explode set likes = likes - 1 where id = #{id}")
    void cancelLike(Integer id);

    //    增加某个职业信息的收藏
    @Update("update occupation_explode set collection = collection + 1 where id = #{id}")
    void addCollection(Integer id);

    //    减少某个职业信息的收藏
    @Update("update occupation_explode set collection = collection - 1 where id = #{id}")
    void cancelCollection(Integer id);

    @Insert("INSERT INTO to_do (user_id,task,start_time,end_time,finish,des)values (#{userId},#{task},#{startTime},#{endTime},#{finishStr},#{desStr})")
    int addPlan(ToDo toDo);


    @Select("SELECT * FROM to_do WHERE user_id = #{userId}")
    ToDo getPlan();
//    根据id去获取计划

    @Update("update to_do set task = #{task},start_time = #{startTime},end_time = #{endTime},finish = #{finishStr},des = #{desStr}")
    int updatePlan(Integer userId,ToDo toDo);
}
