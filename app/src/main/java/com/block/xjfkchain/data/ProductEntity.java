package com.block.xjfkchain.data;

import java.io.Serializable;

/**
 * Copyright (C) 2020, Relx
 * ProductEntity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class ProductEntity implements Serializable {


    /**
     * id : 26
     * name : 昵称
     * power : 12.00
     * duration : 365
     * content : 心愿是...
     * price : 12.00
     * cover : 12
     * recommend : 1
     */

    public long id;
    public String name;
    public String power;
    public int duration;
    public String content;
    public String price;
    public String price_txt;
    public String cover;
    public String stock;
    public String unit;
    public String income_start_time;
    public int recommend;
    public String wechat_client;
    public String buy_notice_url;
}
