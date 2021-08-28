package com.block.xjfkchain.data;

import java.util.List;

/**
 * Copyright (C) 2020, Relx
 * NoticeResponse
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/2, muwenlei, Create file
 */
public class HomeDataResponse extends BaseResponse<HomeDataResponse.ClassBean> {

    public static class ClassBean {
        public String new_price; //	// 实时价格
        public String total_lockup_fil;	// 累计锁仓收益
        public String total_unlock_fil;// 累计解锁收益
        public String total_company_pledge_fil;	// 累计垫付质押
        public String total_income;	// 累计收益
        public String total_power;	// 累计存储
        public String today_increment;	// 今日新增收益
        public String avail_withdraw_fil;	// 今日新增收益
    }
}
