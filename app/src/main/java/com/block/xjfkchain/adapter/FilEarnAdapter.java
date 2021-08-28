package com.block.xjfkchain.adapter;

import android.support.annotation.Nullable;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.FilEarnListEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Copyright (C) 2020, Relx
 * FilEarnAdapter
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/10/22, muwenlei, Create file
 */
public class FilEarnAdapter extends BaseQuickAdapter<FilEarnListEntity, BaseViewHolder> {
    public FilEarnAdapter(@Nullable List<FilEarnListEntity> data) {
        super(R.layout.item_fil_earning,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilEarnListEntity item) {
        helper.setText(R.id.tv_time, item.created_at);
        helper.setText(R.id.tv_name, "FIL");
        helper.setText(R.id.tv_num, item.amount+"");
        helper.setText(R.id.tv_status, item.status_txt);
    }
}
