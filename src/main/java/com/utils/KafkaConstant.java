package com.utils;

public class KafkaConstant {

    //=========================主题topic==========================

    //论坛主题
    public static final String FORUM_TOPIC = "post";


    //==========================分区===============================

    //分区0
    public static final Integer PARTITIONS_0 = 0;


    //=========================标识符=============================

    //post标识头
    public static final String POST_HEADER = "post-";
    //post_like标识头
    public static final String POST_LIKE_HEADER = "post_like-";
    //post_collections标识头
    public static final String POST_COLLECT_HEADER = "post_collection-";
    //post-comments标识头
    public static final String POST_COMMENT_HEADER = "post_comment-";


    //post的crud操作标识符
    public static final String INSERT_POST = POST_HEADER + "insert";
    public static final String UPDATE_POST_VISIBLE_SCOPE = POST_HEADER + "update-visible";
    public static final String DELETE_POST = POST_HEADER + "delete";

    //post_like的delete操作标识符
    public static final String DELETE_POST_LIKE = POST_LIKE_HEADER + "delete";

    //post_collections的操作标识符
    public static final String DELETE_POST_COLLECT = POST_COLLECT_HEADER + "delete";
    public static final String INSERT_POST_COLLECT = POST_COLLECT_HEADER + "insert";

    //post_comments的操作标识符
    public static final String INSERT_POST_COMMENT = POST_COMMENT_HEADER + "insert";
    public static final String DELETE_POST_COMMENT = POST_COMMENT_HEADER + "delete";

}
