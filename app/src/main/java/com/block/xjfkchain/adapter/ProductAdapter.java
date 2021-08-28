package com.block.xjfkchain.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.ProductEntity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Copyright (C) 2020, Relx
 * ProductAdapter
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class ProductAdapter extends BaseQuickAdapter<ProductEntity, BaseViewHolder> {

    public ProductAdapter(@Nullable List<ProductEntity> data) {
        super(R.layout.item_product, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductEntity item) {
        Glide.with(mContext).load(item.cover)
                .into((ImageView) helper.getView(R.id.iv_img));
        helper.setText(R.id.tv_name, item.name);
        if (item.duration==99){
            helper.setText(R.id.tv_time_tip, "服务期 " + "长期");
        }else {
            helper.setText(R.id.tv_time_tip, "服务期 " + item.duration + "年");
        }

        helper.setText(R.id.tv_num, "算力 " + item.power + "T");
        helper.setText(R.id.tv_price, item.price_txt);
        helper.setText(R.id.tv_unit, item.unit);
        if (item.stock.equals("0")) {
            helper.setText(R.id.tv_goumai, "已售馨");
            helper.setBackgroundRes(R.id.tv_goumai, R.drawable.bg_goumai);
        } else {
            helper.setText(R.id.tv_goumai, "立即购买");
            helper.setBackgroundRes(R.id.tv_goumai, R.drawable.bg_goumai2);
        }
    }
}
