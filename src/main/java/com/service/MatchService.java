package com.service;

import com.pojo.MBTITestScore;
import com.pojo.MatchDegree;
import com.pojo.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface MatchService {
       MatchDegree getDegree(String jwt);

       List<User> matching(Integer id, Map<String,List<String>> map);

       List<User> getCartoon(List<User> userList);
}
