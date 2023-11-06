package com.mapper;

import com.pojo.FriendSettings;
import com.pojo.Images;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface HomePageMapper {

    @Select("SELECT * FROM images WHERE user_id = #{userId}")
    List<Images> getImages(Integer userId);

    //    获取前四个最大的关键词
    @Select("SELECT * FROM friend_settings WHERE counts IN (SELECT t.counts FROM" +
            " (SELECT counts FROM friend_settings  WHERE user_id = #{userId} ) AS t)\n" +
            "ORDER BY counts DESC LIMIT 4")
    List<FriendSettings> getKeywords(Integer userId);

}