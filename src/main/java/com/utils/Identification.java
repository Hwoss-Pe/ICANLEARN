package com.utils;

public class Identification {

    //用户关系表
    public static final Integer WAITING = 0;//等待添加
    public static final Integer FRIEND = 1;//好友
    public static final Integer BLACKLIST = 2;//黑名单
    public static final Integer DELETED = 3;//删除


    //帖子可见范围
    public static final Integer PRIVATE_VISIBLE = -1;//仅自己可见
    public static final Integer ALL_VISIBLE = 1;//所有人可见
    public static final Integer FRIEND_VISIBLE = 2;//好友可见

}