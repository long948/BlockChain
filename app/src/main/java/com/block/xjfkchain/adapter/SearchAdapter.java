package com.block.xjfkchain.adapter;

import android.support.annotation.Nullable;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.NewsEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Copyright (C) 2020, Relx
 * SearchAdapter
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/7/26, muwenlei, Create file
 */
public class SearchAdapter extends BaseQuickAdapter<NewsEntity, BaseViewHolder> {
    public SearchAdapter(@Nullable List<NewsEntity> data) {
        super(R.layout.item_search, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsEntity item) {
        helper.setText(R.id.tv_title, item.title)
                .setText(R.id.tv_time, item.created_at);
    }

}
