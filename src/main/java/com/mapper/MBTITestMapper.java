package com.mapper;

import com.pojo.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MBTITestMapper {

    //按TF SN JP EI的顺序排序
    @Select("select * from mbti_question  order by type desc")
    List<MBTIQuestion> selectQuestions();

    //计算每个type对应题目的个数
    @Select("SELECT type, COUNT(*) AS count FROM mbti_question  GROUP BY type ORDER BY type DESC")
    List<TypeCount> selectTypeCount();

    @Insert("insert into mbti_score(user_id, E, I, S, N, T, F, J, P, result) " +
            "values(#{userId},#{E},#{I},#{S},#{N},#{T},#{F},#{J},#{P},#{result})")
    void insertMBTITestScore(MBTITestScore score);

//合并type和info,并且存入一个对象
    @Select("SELECT t.mbti_name, i.abbreviation, t.brief_introduction, i.analysis, i.advantage, i.disadvantage," +
            " i.career_reference, i.undergraduate_program, i.postsecondary_program, i.book_list, i.song_list, i.movie_list" +
            " FROM mbti_type t JOIN mbti_info i ON t.id = i.mbti_type_id" +
            " WHERE t.mbti_name = #{mbti}")
    MBTITestReport selectReportByMBTI(String mbti);

//获取所有简介
    @Select("select id, type, intro,keywords from mbti_intro")
    List<MBTIIntro> selectIntro();

//    根据userId去获取mbti类型
    @Select("select * from mbti_score where user_id =#{userId}")
    MBTITestScore getResult(Integer userId);

//    根据mbti的类型去获取四个关键词
    @Select("select * from mbti_intro where type = #{type}")
    MBTIIntro getKeywords(String type);

}