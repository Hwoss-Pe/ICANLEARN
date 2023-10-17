package com.utils;

public class RedisConstant {
    //验证码的过期时间（2min）
    public static final Long CAPTCHA_EXPIRE_TIME = 120000L;

    //验证码的Key开头
    public static final String CAPTCHA_CODE_ID = "code_id:";

    //匹配列表的过期时间（1h）
    public static final Long MATCH_EXPIRE_TIME = 3600000L;

    //匹配列表的Key开头
    public static final String MATCH_USER_ID = "match_id:";

    //房间历史记录key开头
    public static final String ROOM_HISTORY = "room_history:";

    //房间历史记录的关键词key
    public static final String ROOM_KEYWORD = "key_words";

    //房间的过期时间（2h）
    public static final Long ROOM_EXPIRE_TIME = 7200000L;

}