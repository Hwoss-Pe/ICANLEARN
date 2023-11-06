package com.mapper;

import com.pojo.DiscQuestion;
import com.pojo.DiscReport;
import com.pojo.DiscScore;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DiscMapper {
    @Select("select * from disc_question")
    List<DiscQuestion> getQuestionLists();

    @Insert("insert into disc_score  (user_id, D, I, S, C, result) values(#{userId}, #{D}, #{I},#{S},#{C},#{result})")
    void setDiscScore(DiscScore discScore);

    @Select("select * from disc_report where id = #{id}")
    DiscReport getDiscReport(Integer id);

    @Select("SELECT COUNT(*) FROM disc_score WHERE user_id = #{id}")
    int isExistUserId(Integer Id);

    @Update("UPDATE disc_score SET D = #{D}, I = #{I}, S = #{S}, C = #{C}, result = #{result} WHERE user_id = #{userId}")
    void updateDiscScore(DiscScore discScore);

}
