package com.block.xjfkchain.data;

import java.io.Serializable;

/**
 * Copyright (C) 2020, Relx
 * TbEntity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/1, muwenlei, Create file
 */
public class TxEntity implements Serializable {

    /**
     * id : 1101
     * member_id : 2553
     * amount : 5
     * current : 950.00
     * after : 945
     * account : 1
     * account_txt : 银行卡
     * account_name : null
     * account_no : null
     * remark : 测试
     * audit_remark : null
     * status : 0
     * status_txt : 待审核
     * created_at : 2020-08-12
     */

    public int id;
    public int member_id;
    public String amount;
    public String current;
    public String after;
    public String account;
    public String account_txt;
    public String account_name;
    public String account_no;
    public String remark;
    public String audit_remark;
    public int status;
    public String status_txt;
    public String created_at;
}
