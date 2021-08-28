package com.block.xjfkchain.data;

/**
 * Copyright (C) 2018,
 * BaseResponse
 * <p>
 * Description
 *
 * @author yue
 * @version 1.0
 * <p>
 * Ver 1.0, 2019-08-01, yue, Create file
 */
public class BaseResponse<T> {

    public static final int RES_OK = 0;

//    public static final String RES_FAIL = "fail";


    public int code;

    public String msg;

    public T data;


    public BaseResponse() {

    }


    public boolean isSuc() {
        return RES_OK == code;
    }
}
