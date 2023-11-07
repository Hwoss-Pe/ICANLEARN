package com.utils;

public class Code {

    //======================登陆注册=======================

    //    注册
    public static final Integer REGISTER_OK = 10011;
    public static final Integer REGISTER_ERR = 10010;
    //    验证
    public static final Integer VERTICAL_LOGIN_ERR = 10020;
    //    登录
    public static final Integer LOGIN_ERR = 10000;
    public static final Integer LOGIN_OK = 20001;
    //    注册验证
    public static final Integer VERTICAL_OK = 20031;
    public static final Integer VERTICAL_ERR = 10030;
    //    重置密码
    public static final Integer RESET_PASSWORD_OK = 20041;
    public static final Integer RESET_PASSWORD_ERR = 10040;


    //======================个人信息=======================

    //    上传头像
    public static final Integer UPLOAD_AVATAR_OK = 20091;
    public static final Integer UPLOAD_AVATAR_ERR = 10090;
    //    查询用户
    public static final Integer FIND_USER_OK = 20101;
    public static final Integer FIND_USER_ERR = 10100;
    //    更新用户信息
    public static final Integer USER_UPDATE_OK = 20111;
    public static final Integer USER_UPDATE_ERR = 10110;

    //======================测试=======================

    //    获取MBTI题目
    public static final Integer MBTI_QUESTION_OK = 20201;
    public static final Integer MBTI_QUESTION_ERR = 10200;
    //获取MBTI测试结果
    public static final Integer MBTI_RESULT_OK = 20211;
    public static final Integer MBTI_RESULT_ERR = 10210;
    //获取MBTI测试报告
    public static final Integer MBTI_REPORT_OK = 20221;
    public static final Integer MBTI_REPORT_ERR = 10220;

    //获取霍兰德题目
    public static final Integer HLD_TEST_OK = 20341;
    public static final Integer HLD_TEST_ERR = 10340;

    //返回霍兰德测试结果
    public static final Integer HLD_REPORT_OK = 20351;
    public static final Integer HLD_REPORT_ERR = 10350;

    //返回Disc测试结果
    public static final Integer DISC_TEST_OK = 20351;
    public static final Integer DISC_TEST_ERR = 10350;

    //返回Disc测试结果
    public static final Integer DISC_REPORT_OK = 20361;
    public static final Integer DISC_REPORT_ERR = 10360;

    //======================匹配=======================

    //    匹配度表格
    public static final Integer DEGREE_OK = 20301;
    public static final Integer DEGREE_ERR = 10300;
    //    获取匹配用户
    public static final Integer MATCH_USER_OK = 20311;
    public static final Integer MATCH_USER_ERR = 10300;
    //    获取聊天记录
    public static final Integer CHAT_HISTORY_OK = 20321;
    public static final Integer CHAT_HISTORY_ERR = 10320;
    //    发送聊天
    public static final Integer CHAT_OK = 20331;
    public static final Integer CHAT_ERR = 10330;
    //发送电波
    public static final Integer WAVE_SEND_OK = 20401;
    public static final Integer WAVE_SEND_ERR = 10400;

    //接受电波
    public static final Integer WAVE_ACCEPT_OK = 20411;
    public static final Integer WAVE_ACCEPT_ERR = 10410;

    //查询列表
    public static final Integer QUERY_WAITING_OK = 20421;
    public static final Integer QUERY_WAITING_ERR = 10420;
    public static final Integer QUERY_FRIEND_OK = 20431;
    public static final Integer QUERY_FRIEND_ERR = 10430;


    //======================画板=======================


    //创建房间
    public static final Integer CREATE_ROOM_OK = 20371;
    public static final Integer CREATE_ROOM_ERR = 10370;

    //加入房间
    public static final Integer JOIN_ROOM_OK = 20381;
    public static final Integer JOIN_ROOM_ERR = 10380;

    //退出房间
    public static final Integer EXIT_ROOM_OK = 20381;
    public static final Integer EXIT_ROOM_ERR = 10380;

    //关键词提示
    public static final Integer KEY_WORDS_PROMPT_OK = 20441;
    public static final Integer KEY_WORDS_PROMPT_ERR = 20441;


    //选关键词
    public static final Integer SET_GUESS_WORDS_OK = 20451;
    public static final Integer SET_GUESS_WORDS_ERR = 10450;

    //猜关键词
    public static final Integer GUESS_WORDS_OK = 20461;
    public static final Integer GUESS_WORDS_ERR = 10460;


    //===================职业成长路线====================


    //搜索出来的网页信息
    public static final Integer SEARCH_KEYWORDS_OK = 20391;
    public static final Integer SEARCH_KEYWORDS_ERR = 10390;

    //获取历史记录
    public static final Integer SEARCH_HISTORY_OK = 20611;
    public static final Integer SEARCH_HISTORY_ERR = 10610;

    //联想词
    public static final Integer ASSOCIATE_OK = 20621;
    public static final Integer ASSOCIATE_ERR = 10620;

    //职业具体信息
    public static final Integer Occupation_DETAIL_OK = 20631;
    public static final Integer Occupation_DETAIL_ERR = 10610;

    //职业点赞
    public static final Integer SEARCH_LIKE_OK = 20641;
    public static final Integer SEARCH_LIKE_ERR = 10640;

    //职业取消点赞
    public static final Integer SEARCH_CANCEL_LIKE_OK = 20651;
    public static final Integer SEARCH_CANCEL_LIKE_ERR = 10650;

    //职业收藏
    public static final Integer SEARCH_COLLECTION_OK = 20661;
    public static final Integer SEARCH_COLLECTION_ERR = 10660;

    //职业取消收藏
    public static final Integer SEARCH_COLLECTION_CANCEL_OK = 20671;
    public static final Integer SEARCH_COLLECTION_CANCEL_ERR = 10670;


    //=====================绘本======================


    //添加绘本
    public static final Integer CARTOON_ADD_OK = 20681;
    public static final Integer CARTOON_ADD_ERR = 10680;

    //获取绘本
    public static final Integer CARTOON_GET_OK = 20691;
    public static final Integer CARTOON_GET_ERR = 10690;

    //更新绘本
    public static final Integer CARTOON_UPDATE_OK = 20691;
    public static final Integer CARTOON_UPDATE_ERR = 10690;

    //保存画板
    public static final Integer WHITEBOARD_SAVE_OK = 20701;
    public static final Integer WHITEBOARD_SAVE_ERR = 10700;

    //删除绘本
    public static final Integer CARTOON_DELETE_OK = 20711;
    public static final Integer CARTOON_DELETE_ERR = 10710;



    //=====================个人主页======================


    //获取头像
    public static final Integer GET_AVATAR_OK = 20721;
    public static final Integer GET_AVATAR_ERR = 10720;

    //画板主页
    public static final Integer WHITEBOARD_GET_OK = 20731;
    public static final Integer WHITEBOARD_GET_ERR = 10730;

    //======================论坛=======================

    //发布帖子
    public static final Integer UPLOAD_POST_OK = 20801;
    public static final Integer UPLOAD_POST_ERR = 10800;

    //更改帖子可见范围
    public static final Integer UPDATE_POST_STATUS_OK = 20811;
    public static final Integer UPDATE_POST_STATUS_ERR = 10810;

    //删除帖子
    public static final Integer DELETE_POST_OK = 20821;
    public static final Integer DELETE_POST_ERR = 10820;

    //预览帖子
    public static final Integer PREVIEW_POST_OK = 20831;
    public static final Integer PREVIEW_POST_ERR = 10830;

    //查看帖子详细信息
    public static final Integer VIEW_DETAILS_POST_OK = 20841;
    public static final Integer VIEW_DETAILS_POST_ERR = 10840;

    //点赞帖子
    public static final Integer LIKE_POST_OK = 20851;
    public static final Integer LIKE_POST_ERR = 10850;

    //收藏帖子
    public static final Integer COLLECT_POST_OK = 20861;
    public static final Integer COLLECT_POST_ERR = 10860;

    //获取当前用户收藏的帖子列表预览
    public static final Integer GET_COLLECTIONS_OK = 20871;
    public static final Integer GET_COLLECTIONS_ERR = 10870;

    //评论帖子
    public static final Integer COMMENT_POST_OK = 20881;
    public static final Integer COMMENT_POST_ERR = 10880;

    //删除评论
    public static final Integer DELETE_COMMENT_OK = 20891;
    public static final Integer DELETE_COMMENT_ERR = 10890;

    //搜索帖子
    public static final Integer SEARCH_POST_OK = 20901;
    public static final Integer SEARCH_POST_ERR = 10900;

//========================= 5*5计划==================
//    设置任务
    public static final Integer SET_PLAN_OK = 20911;
    public static final Integer SET_PLAN_ERR = 10910;
//    完成任务
    public static final Integer FINISH_PLAN_OK = 20921;
    public static final Integer FINISH_PLAN_ERR = 10920;
//    获取任务
    public static final Integer GET_PLAN_OK = 20931;
    public static final Integer GET_PLAN_ERR = 10930;
}