package com.block.xjfkchain.data;

/**
 * Copyright (C) 2020, Relx
 * NewsEntity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class NewsEntity {


    /**
     * id : 188
     * title : 东坡先生错了！岂止“儿戏”？
     * cover : /uploads/news/20200119/1579406974375.jpg
     * sort_order : 0
     * status : 0
     * created_at : 2020-01-19
     */

    public long id;
    public String title;
    public String cover;
    public long sort_order;
    public long status;
    public String created_at;
    public String url;
    public int is_read;//0是未读
    public int view;
}
