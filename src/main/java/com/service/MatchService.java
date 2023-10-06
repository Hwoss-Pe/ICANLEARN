package com.service;

import com.pojo.MatchDegree;
import com.pojo.User;

import java.util.List;
import java.util.Map;


public interface MatchService {
       MatchDegree getDegree(String jwt);

       List<User> matching(String jwt, Map<String, List<String>> map);
}
