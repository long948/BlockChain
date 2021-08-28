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

public class FinancialOrderAdapter extends BaseQuickAdapter<OrderEntity, BaseViewHolder> {
    public FinancialOrderAdapter(@Nullable List<OrderEntity> data) {
        super(R.layout.item_financial_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderEntity item) {
        helper.setText(R.id.tv_1, item.product_name);
        helper.setText(R.id.tv_2, item.created_at);
        helper.setText(R.id.tv_3, item.amount);

    }
}
