package com.service;

import com.pojo.MatchDegree;
import com.pojo.User;

import java.util.List;


public interface MatchService {
    MatchDegree getDegree(String jwt);

    List<User> matching(Integer id);

    List<User> getCartoon(List<User> userList);
}