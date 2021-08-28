package com.block.xjfkchain.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.OrderEntity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Copyright (C) 2020, Relx
 * OrderAdapter
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class OrderAdapter extends BaseQuickAdapter<OrderEntity, BaseViewHolder> {
    public OrderAdapter(@Nullable List<OrderEntity> data) {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderEntity item) {
        Glide.with(mContext).load(item.product_cover).into((ImageView) helper.getView(R.id.iv_image));
        helper.setText(R.id.tv_name, item.product_name);
        helper.setText(R.id.tv_status, item.status_txt);
        if (item.duration.equals("99")){
            helper.setText(R.id.tv_service_time, "服务期：" +"长期");
        }else {
            helper.setText(R.id.tv_service_time, "服务期：" + item.duration + "年");
        }

        helper.setText(R.id.tv_hashrate, "算力：" + (TextUtils.isEmpty(item.power) ? 0 : item.power) + "T");
        helper.setText(R.id.tv_num, item.num + "");
        helper.setText(R.id.tv_price_unit, item.price_unit + "");
        helper.setText(R.id.tv_money, item.total + "");
    }
}
