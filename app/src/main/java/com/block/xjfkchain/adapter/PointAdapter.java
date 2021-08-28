package com.block.xjfkchain.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.PointEntity;
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
public class PointAdapter extends BaseQuickAdapter<PointEntity, BaseViewHolder> {
    public PointAdapter(@Nullable List<PointEntity> data) {
        super(R.layout.item_point, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PointEntity item) {
        helper.setText(R.id.tv_name, item.name);
        helper.setText(R.id.tv_hashrate, item.team_power + "T");
//        helper.setText(R.id.tv_level, item.level);
        if (item.node_id != null) {
            helper.setTextColor(R.id.tv_tag, Color.parseColor("#C2CEFA"));
            helper.setText(R.id.tv_tag, "已挂载");
        } else {
            helper.setTextColor(R.id.tv_tag, Color.parseColor("#E95924"));
            helper.setText(R.id.tv_tag, "待挂载");
        }
        helper.addOnClickListener(R.id.tv_tag);


    }
}
