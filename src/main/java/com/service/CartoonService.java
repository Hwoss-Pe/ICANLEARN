package com.service;


import com.pojo.Cartoon;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Service;


public interface CartoonService {

//    这里传进来的信息要进行解析，然后分别传进去
    int  addCartoon(Cartoon cartoon);

    Cartoon getCartoon(Integer userId);

    boolean deleteCartoon(String jwt);


}
