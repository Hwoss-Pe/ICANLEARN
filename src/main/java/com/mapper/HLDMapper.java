package com.mapper;

import com.pojo.HLDQuestion;
import com.pojo.HLDTestReport;
import com.pojo.HLDTestResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HLDMapper {
//
 @Select("select * from hld_question")
    List<HLDQuestion> getQuestionList();

    @Insert("insert into hld_score(user_id, R, I, A, S, E, C, result) " +
            "VALUES(#{userId},#{R},#{I},#{A},#{S},#{E},#{C},#{result})")
    void insertHLDTestScore(HLDTestResult hldTestScore);


    List<HLDTestReport> getHLDTestReport(List<String> types);

}
