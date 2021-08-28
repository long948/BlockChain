package com.block.xjfkchain.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.NewsEntity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Copyright (C) 2020, Relx
 * HomeAdapter
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class HomeAdapter extends BaseQuickAdapter<NewsEntity, BaseViewHolder> {
    public HomeAdapter(@Nullable List<NewsEntity> data) {
        super(R.layout.item_home, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsEntity item) {
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_time, item.created_at);
        int color = 0;
        if (item.is_read == 0) {
            color = Color.parseColor("#80D2D9F1");
        } else {
            color = Color.parseColor("#D2D9F1");
        }
        helper.setTextColor(R.id.tv_title, color);
        helper.setText(R.id.tv_read_num,item.view+"");
        Glide.with(mContext)
                .load(item.cover)
                .into((ImageView) helper.getView(R.id.iv_img));
    }
}
