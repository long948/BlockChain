package com.block.xjfkchain.data;

/**
 * Copyright (C) 2020, Relx
 * ImageUploadResponse
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/11, muwenlei, Create file
 */
public class ImageUploadResponse extends BaseResponse<ImageUploadResponse.ImageBean> {

    public static class ImageBean {
        public String src;
    }

    public String link;
}
