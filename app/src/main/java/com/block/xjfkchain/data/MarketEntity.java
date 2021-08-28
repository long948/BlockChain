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
public class MarketEntity implements Serializable {

    /**
     * id: "yearnfinance",
     * name: "yearn.finance",
     * symbol: "YFI",
     * rank: 26,
     * logo: "https://s1.bqiapp.com/logo/1/yearnfinance.png?x-oss-process=style/coin_36_webp&v=1595226681",
     * logo_png: "https://s1.bqiapp.com/logo/1/yearnfinance.png?x-oss-process=style/coin_72&v=1595226681",
     * price_usd: 24755,
     * price_btc: 1.36,
     * volume_24h_usd: 688637108,
     * market_cap_usd: 741871025,
     * available_supply: 29968,
     * total_supply: 30000,
     * max_supply: 30000,
     * percent_change_1h: 4.41,
     * percent_change_24h: 2.93,
     * percent_change_7d: -1.65,
     * last_updated: 1606658863
     */

    public String id;
    public String name;
    public String symbol;
    public String rank;
    public String logo;
    public String logo_png;
    public String price_usd;
    public String price_btc;
    public String volume_24h_usd;
    public String market_cap_usd;
    public String available_supply;
    public String total_supply;
    public String max_supply;
    public String percent_change_1h;
    public String percent_change_24h;
    public String percent_change_7d;
    public String last_updated;

}
