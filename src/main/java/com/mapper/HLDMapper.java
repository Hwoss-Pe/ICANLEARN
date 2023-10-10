package com.mapper;

import com.pojo.HLDQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HLDMapper {
//
 @Select("select * from hld_question")
    List<HLDQuestion> getQuestionList();

}
