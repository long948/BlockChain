package com.block.xjfkchain.data;

import java.io.Serializable;

/**
 * Copyright (C) 2020, Relx
 * OrderEntity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class OrderEntity implements Serializable {

    public static final int STATUS_NOPAY = 0;
    public static final int STATUS_CHECK = 1;
    public static final int STATUS_PROCEED = 2;
    public static final int STATUS_COMPLETE = 3;

    /**
     * wait
     * waitconfirm
     * runing
     * complete
     * id : 93
     * product_id : 27
     * price : 12.00
     * num : 2
     * power : 24
     * total : 24
     * product_name : 昵称
     * product_price : 12.00
     * pay_screenshot : null
     * start :
     * end :
     * status : 0
     * status_txt : 待支付
     * <p>
     * case 0: return '待支付'; break;
     * case 1: return '审核中'; break;
     * case 2: return '进行中'; break;
     * case 3: return '已完成'; break;
     */

    public String id;
    public String product_id;
    public String product_cover;
    public String price;
    public String num;
    public String price_unit;
    public String power;
    public String total;
    public String product_name;
    public String product_price;
    public String pay_screenshot;
    public String start;
    public String end;
    public int status;
    public String status_txt;
    public String duration;
    public String wallet_address;
    public String wallet_qrcode;
    public String wechat_client;
    public String use_usdt;
    public String need_pay;
    public String target;
    public String amount;
    public String fil_addr;
    public String valid_power;
    public String created_at;
    public String fil_product_id;
    public String income_fil;
    public String open_days;
    public String member_id;
    public String product_type;
    public String buy_notice_url;
}
