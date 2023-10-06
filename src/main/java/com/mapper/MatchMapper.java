package com.mapper;

import com.pojo.MatchDegree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchMapper {
//根据mbti去获取各种匹配度
    @Select("SELECT * FROM match_degree where mbti = #{mbti}")
    MatchDegree getDegreeByMBTI(String mbti);
}
