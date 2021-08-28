package com.block.xjfkchain.adapter;

import android.support.annotation.Nullable;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.EarnEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Copyright (C) 2020, Relx
 * PointAdapter
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class EarnAdapter extends BaseQuickAdapter<EarnEntity, BaseViewHolder> {

    public EarnAdapter(@Nullable List<EarnEntity> data, boolean showStatus) {
        super(R.layout.item_earn, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EarnEntity item) {
        helper.setText(R.id.tv_time, item.created_at);
        helper.setText(R.id.tv_earn, item.usdt+"USDT");
    }
}
