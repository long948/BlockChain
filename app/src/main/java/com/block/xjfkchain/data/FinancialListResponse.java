package com.block.xjfkchain.data;

import java.util.List;

public class FinancialListResponse extends BaseResponse<FinancialListResponse.DataBean> {

        public static class DataBean {
            public List<FinancialBuffEntity> list;
        }
}
