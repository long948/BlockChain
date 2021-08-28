package com.block.xjfkchain.adapter;

import android.support.annotation.Nullable;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.OrderEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


public class FinancialOrder1Adapter extends BaseQuickAdapter<OrderEntity, BaseViewHolder> {
    public FinancialOrder1Adapter(@Nullable List<OrderEntity> data) {
        super(R.layout.item_financial_order_1, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderEntity item) {
        helper.setText(R.id.tv_1, item.product_name);
        helper.setText(R.id.tv_2, item.created_at);
        helper.setText(R.id.tv_3, item.status_txt);
        helper.setText(R.id.tv_4, item.product_type);
        helper.setText(R.id.tv_5, item.amount);
        helper.setText(R.id.tv_6, item.open_days);
        helper.setText(R.id.tv_7, item.income_fil);
    }
}
