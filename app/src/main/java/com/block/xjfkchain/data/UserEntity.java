package com.block.xjfkchain.data;

import java.io.Serializable;

/**
 * Copyright (C) 2020, Relx
 * UserEntity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/11, muwenlei, Create file
 */
public class UserEntity implements Serializable {

    /**
     * id : 2284
     * invite_id : 2289
     * parent_id : null
     * team_id : null
     * mobile : 157****5787
     * name : 朱军
     * level : 初级矿工
     * avatar : null
     * power : 25.00
     * invite_code : 2284
     * team_power : 901.00
     * usdt : 45.00
     * total_usdt : 45.00
     * status : 1
     * status_txt : 正常
     * is_show_financial: 0
     */

    public String id;
    public String invite_id;
    public String parent_id;
    public String team_id;
    public String mobile;
    public String name;
    public String level;
    public String avatar;
    public String power;
    public int invite_unnode_num;
    public String invite_code;
    public String team_power;
    public String usdt;
//    public int valid_power;
    public String total_usdt;
    public int status;
    public String status_txt;
    public int is_show_financial;
    public String qrcode_url;
    public String share_url;


    public String bankcard_no;
    public String bank_name;
    public String bankcard_name;
    public String alipay;
    public String wallet;
    public String wallet_usdt;
    public String default_account;
    public String reward_power;

    public String fil;

}
