package com.block.xjfkchain.data;

import java.io.Serializable;


public class FinancialBuffEntity implements Serializable {

    public long id;
    public String name;
    public String content;
    public String duration;
    public String rate;
    public String lowest_price;
    public String status;
    public String balance_fil;
    public String limit_fil;
    public String avail_amount;
    public String expected_income_time;
    public int is_sell_out;

    // "is_sell_out": 0 	//是否售罄，0：否，1：是

}
