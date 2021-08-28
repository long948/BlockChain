package com.block.xjfkchain.data;

import java.util.List;

/**
 * Copyright (C) 2020, Relx
 * NoticeResponse
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/2, muwenlei, Create file
 */
public class NoticeResponse extends BaseResponse<NoticeResponse.ClassBean> {

    public static class ClassBean {
        public List<NoticeEntity> list;
        public int unread_notice_num;
        public long member_id;
    }
}
