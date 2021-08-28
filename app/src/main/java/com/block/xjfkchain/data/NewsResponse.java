package com.block.xjfkchain.data;

import java.util.List;

public class NewsResponse extends BaseResponse<NewsResponse.BannerClass> {

    public static class BannerClass {
        public List<NewsEntity> list;
        public int unread_num;
    }
}
