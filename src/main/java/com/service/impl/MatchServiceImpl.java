package com.service.impl;

import com.mapper.MatchMapper;
import com.mapper.UserMapper;
import com.service.MatchService;
import com.service.RadioWaveService;
import com.service.UserService;
import com.pojo.MatchDegree;
import com.pojo.User;
import com.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MatchMapper matchMapper;
    @Autowired
    private UserService userService;
    @Autowired
    RadioWaveService radioWaveService;

    @Override
    public List<User> matching(Integer id,Map<String, List<String>> map) {

        List<String> list = new ArrayList<String>();

        List<User> returnList = new ArrayList<User>();
//      注意这里返回的List里面 是需要去掉好友和等待列表以及自己，

        if (map==null||map.size()==0) {
            List<User> userList = userService.getUsers();
            List<User> filtration = filtration(userList, id);
//获取所有用户，走一次过滤后随机后添加
            returnList.addAll(filtration);
//            returnList = new ArrayList<>(userList.subList(0,5));
        }

        else {
//            传值
//                returnList = new ArrayList<>(userList.subList(0,5));
                if(map.containsKey("MBTI")){
                    list = map.get("MBTI");
                }
            List<User> userList = new ArrayList<>();
//            获取选择后筛选出只包含选择的集合userList，获取所有userList后进行操作
                for (String mbti : list) {
//                分别去获取对应的用户加上去再打乱
                    List<User> tempUserList  = userService.getUsersByMBTI(mbti);
                    userList.addAll(tempUserList);
//                    这里的returnList是包含所选的mbti类型的组，这里没有过滤
                }
                returnList= filtration(userList, id);

//                returnList = new ArrayList<>(returnList.subList(0,5));
            }
//               这里如果要保证刷新的五个不会重复以及匹配到的需要和后面的重复，思路是查询当前id的好友和等待列表然后去掉对应（待完善）
        Collections.shuffle(returnList);
        return returnList;
    }

    @Override
    public MatchDegree getDegree(String jwt) {
//        通过id去获取对应的对象的mbti
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer user_id = (Integer) claims.get("id");
       User user=  userMapper.FindUserById(user_id);
       String mbti = user.getMbti();
       mbti = mbti.toUpperCase();
//       mbti的各种匹配度
        //        注意这里degree是包含第一个mbti类型的
        return matchMapper.getDegreeByMBTI(mbti);
    }

    public  List<User> filtration(List<User> userList,Integer id){


//        传入需要过滤的集合以及id
//        获取需要不再显示的集合
        List<User> returnLists = new ArrayList<User>();
        List<User> queryFriendsList = radioWaveService.queryFriendsList(id);
        List<User> queryWaitingList = radioWaveService.queryWaitingList(id);
        returnLists.addAll(queryFriendsList);
        returnLists.addAll(queryWaitingList);
        User self = userService.getUserById(id);
        returnLists.add(self);


         userList.removeAll(returnLists);
        return  userList;
    }

}
