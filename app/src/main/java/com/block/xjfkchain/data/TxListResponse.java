package com.block.xjfkchain.data;

import java.util.List;

public class TxListResponse extends BaseResponse<TxListResponse.DataBean> {

    public static class DataBean {
        public List<TxEntity> list;
    }
}
