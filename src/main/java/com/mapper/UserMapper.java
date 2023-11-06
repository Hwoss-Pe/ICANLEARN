package com.mapper;

import com.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {
    //    获取所有的用户
    @Select("select * from user")
    List<User> getUsers();

    //    登录看是否你查询到User
    @Select("select * from user where account = #{account} and password = #{password}")
    User login(User user);

    //    注册
    @Insert("INSERT INTO user (username,account, password) VALUES (#{username},#{account},#{password}) ")
    void register(User user);

    //    id获取整个对象
    @Select("select * from user where id = #{id}")
    User findUserById(Integer id);

    //    根据账号去获取整个对象
    @Select("select * from user where account = #{account}")
    User findUserByAccount(String account);

    @Update("UPDATE user SET password = #{newPassword} WHERE account = #{account}")
    int resetPassword(String account, String newPassword);

    @Update("UPDATE user SET avatar = #{avatar64} WHERE id = #{id}")
    int uploadAvatar(Integer id, String avatar64);

    //更新整个User
    @Update("UPDATE user SET username = #{username},job = #{job},mbti = #{mbti} ,interest = #{interest},signature = #{signature},interest_mbti =#{interest_mbti},hld = #{hld},disc = #{disc} WHERE id = #{id}")
    int updateUser(User user);

    //    根据mbti去获取对象集合
    @Select("SELECT * FROM user WHERE mbti = #{mbti}")
    List<User> getUsersByMBTI(String mbti);

    @Select("SELECT avatar FROM user WHERE id = #{id}")
    String getAvatar(Integer id);

}
