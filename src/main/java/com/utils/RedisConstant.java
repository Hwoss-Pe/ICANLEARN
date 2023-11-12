package com.utils;

public class RedisConstant {

    //=======================验证码=======================

    //验证码的过期时间（2min）
    public static final Long CAPTCHA_EXPIRE_TIME = 120000L;

    //验证码的Key开头
    public static final String CAPTCHA_CODE_ID = "code_id:";

    //=======================匹配========================

    //匹配列表的过期时间（1h）
    public static final Long MATCH_EXPIRE_TIME = 3600000L;

    //房间的过期时间（2h）
    public static final Long ROOM_EXPIRE_TIME = 7200000L;

    //匹配列表的Key开头
    public static final String MATCH_USER_ID = "match_id:";

    //=======================房间=======================

    //房间历史记录key开头
    public static final String ROOM_HISTORY = "room_history:";

    //房间关键词
    public static final String ROOM_CHARACTER_KEYWORD = "characterKeyWords";
    public static final String ROOM_JOB_KEYWORD = "jobKeyWords";


    //房主所猜关键词
    public static final String GUESS_CHARACTER_WORDS = "characterGuessWords";
    public static final String GUESS_JOB_WORDS = "jobGuessWords";

    //好友id
    public static final String RECEIVER_ID = "receiverId";


    //=======================论坛========================

    //posts表自增id全局计数器key
    public static final String POST_AUTO_INCREMENT_ID = "post_increment_id";
    public static final String COMMENT_AUTO_INCREMENT_ID = "comment_increment_id";

    //posts表全局自增id计数器过期时间(5min)
    public static final Long INCREMENT_ID_EXPIRE_TIME = 300000L;

    //论坛post的key开头
    public static final String FORUM_POST = "forum_post:";

    //即时发送帖子过期时间(5min)
    public static final Long FORUM_POST_EXPIRE_TIME = 300000L;

    //点赞帖子的key开头
    public static final String FORUM_POST_LIKE = "post_like:";

    //点赞帖子的key备份开头
    public static final String BACKUP_FORUM_POST_LIKE = "backup_post_like:";


    //点赞/收藏帖子数据缓存的过期时间(2min)
    public static final Long FORUM_POST_LIKE_COLLECT_EXPIRE_TIME = 120000L;

    //收藏帖子的key开头
    public static final String FORUM_POST_COLLECT = "post_collect:";

    //用户id对应已预览的帖子
    public static final String POST_PREVIEWS_IDS = "previews_ids:";

    //用户id对应已预览的帖子的过期时间(5min)
    public static final Long POST_PREVIEWS_IDS_EXPIRE_TIME = 300000L;

    //======================职业价值观==========================

    //职业价值观进度缓存第一关
    public static final String OCCUPATION_VALUES_1= "values_user_id:1";
    //职业价值观进度缓存（拷贝）第一关
    public static final String OCCUPATION_VALUES_COPY_1= "values_user_id_copy:1";
    //监听的key
    public static final String OCCUPATION_VALUES= "values_user_id_:";
    //职业价值观进度缓存第二关
    public static final String OCCUPATION_VALUES_2= "values_user_id:2";
    //职业价值观进度缓存（拷贝）第二关
    public static final String OCCUPATION_VALUES_COPY_2= "values_user_id_copy:2";
    //职业价值观进度缓存(两个小时)
    public static final Long OCCUPATION_VALUES_EXPIRE_TIME_COPY = 7200000L;
    //职业价值观进度缓存(一小时)
    public static final Long OCCUPATION_VALUES_EXPIRE_TIME = 3600000L;
    //职业点赞
    public static final String OCCUPATION_LIKE = "occupation_like";
    //职业收藏
    public static final String OCCUPATION_Collection = "occupation_collection";

}
