package com.block.xjfkchain.data;

import java.util.List;

public class InComeListResponse extends BaseResponse<InComeListResponse.Bean> {

    public static class Bean {
        public List<InComeEntity> list;
        public PageEntity page;
    }
}
