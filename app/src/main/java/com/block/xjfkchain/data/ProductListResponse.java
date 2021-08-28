package com.block.xjfkchain.data;

import java.util.List;

public class ProductListResponse extends BaseResponse<ProductListResponse.DataBean> {

        public static class DataBean {
            public List<ProductEntity> list;
        }
}
