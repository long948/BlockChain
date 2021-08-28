package com.block.xjfkchain.data;

import java.util.List;

public class BannerResponse extends BaseResponse<BannerResponse.BannerClass> {
    public static class BannerClass {
        public List<BannerEntity> list;
    }
}
