package com.service;

import com.pojo.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    //    判断是否有这个用户
    User login(User user);
    //    判断注册是否合法
    boolean register(User user);
    //    判断是否存在账号存在
    boolean isAccountExist(String account);
    //
    boolean updatePassword(String account, String newPassword);

    boolean updateAvatar64(Integer user_id, String avatar64);

    User getUserById(Integer userId);

    boolean updateUser(User user);

    List<User> getUsersByMBTI(String mbti);

    String getAvatar(Integer userId);
}