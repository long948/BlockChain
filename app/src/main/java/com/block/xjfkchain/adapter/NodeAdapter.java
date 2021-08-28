package com.block.xjfkchain.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.NodeEntity;
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
public class NodeAdapter extends BaseQuickAdapter<NodeEntity, BaseViewHolder> {
    boolean showStatus;

    public NodeAdapter(@Nullable List<NodeEntity> data, boolean showStatus) {
        super(R.layout.item_point, data);
        this.showStatus = showStatus;
    }

    @Override
    protected void convert(BaseViewHolder helper, NodeEntity item) {
        helper.setText(R.id.tv_name, item.name);
        helper.setText(R.id.tv_hashrate, item.team_power + "T");
        helper.setText(R.id.tv_level, item.level);
        if (!item.mount) {
            helper.setTextColor(R.id.tv_tag, Color.parseColor("#333333"));
            helper.setText(R.id.tv_tag, "已满");
        } else {
            helper.setTextColor(R.id.tv_tag, Color.parseColor("#ffce3d3a"));
            helper.setText(R.id.tv_tag, "可挂载");
        }
        helper.addOnClickListener(R.id.tv_tag);
        helper.getView(R.id.tv_tag).setVisibility(showStatus ? View.VISIBLE : View.GONE);


    }
}
