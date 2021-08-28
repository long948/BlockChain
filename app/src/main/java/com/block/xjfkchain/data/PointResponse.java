package com.block.xjfkchain.data;

import java.util.List;

public class PointResponse extends BaseResponse<PointResponse.PointBean> {
    public static class PointBean {
        public List<PointEntity> list;
    }
}
