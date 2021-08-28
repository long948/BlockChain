package com.block.xjfkchain.adapter;

import android.graphics.Color;
import android.media.Image;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.MarkEntity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2020, Relx
 * MarketAdapter
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/1, muwenlei, Create file
 */
public class MarketAdapter extends BaseQuickAdapter<MarkEntity, BaseViewHolder> {
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.tv_arrow)
    ImageView mTvArrow;

    public MarketAdapter(@Nullable List<MarkEntity> data) {
        super(R.layout.item_market, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MarkEntity item) {
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        Glide.with(mContext).load(item.logo_png).into(ivIcon);
        helper.setText(R.id.tv_name, item.symbol);
        helper.setText(R.id.tv_price, "$ "+item.price_usd);
        helper.setText(R.id.tv_amount, item.percent_change_24h+"%");

        if (new BigDecimal(item.percent_change_24h).compareTo(BigDecimal.ZERO)!=-1){
            helper.setTextColor(R.id.tv_price, Color.parseColor("#2FA89D"));
            helper.setTextColor(R.id.tv_amount,  Color.parseColor("#2FA89D"));
        }else {
            helper.setTextColor(R.id.tv_price, Color.parseColor("#E95924"));
            helper.setTextColor(R.id.tv_amount,  Color.parseColor("#E95924"));
        }
    }
}
