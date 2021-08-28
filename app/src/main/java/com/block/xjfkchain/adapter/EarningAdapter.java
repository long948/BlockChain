package com.block.xjfkchain.adapter;

import android.support.annotation.Nullable;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.EarningEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Copyright (C) 2020, Relx
 * TbAdapter
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/1, muwenlei, Create file
 */
public class EarningAdapter extends BaseQuickAdapter<EarningEntity, BaseViewHolder> {
    public EarningAdapter(@Nullable List<EarningEntity> data) {
        super(R.layout.item_tb, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EarningEntity item) {
        helper.setText(R.id.tv_time, item.created_at);
        helper.setText(R.id.tv_name, "USDT");
        helper.setText(R.id.tv_num, item.usdt);
    }
}
