package com.block.xjfkchain.adapter;

import android.support.annotation.Nullable;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.TbEntity;
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
public class TbAdapter extends BaseQuickAdapter<TbEntity, BaseViewHolder> {
    public TbAdapter(@Nullable List<TbEntity> data) {
        super(R.layout.item_tb, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TbEntity item) {
        helper.setText(R.id.tv_time, "");
        helper.setText(R.id.tv_name, "");
        helper.setText(R.id.tv_num, "");
    }
}
