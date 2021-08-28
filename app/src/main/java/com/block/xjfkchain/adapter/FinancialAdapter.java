package com.block.xjfkchain.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.FinancialBuffEntity;
import com.block.xjfkchain.data.ProductEntity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class FinancialAdapter extends BaseQuickAdapter<FinancialBuffEntity, BaseViewHolder> {

    public FinancialAdapter(@Nullable List<FinancialBuffEntity> data) {
        super(R.layout.item_financial, data);
    }

    @SuppressLint("Range")
    @Override
    protected void convert(BaseViewHolder helper, FinancialBuffEntity item) {
        String str=item.rate;
        String str1=item.duration;
        String str2=item.lowest_price;
        String str4=item.limit_fil;
        String str5=item.avail_amount;
        helper.setText(R.id.tv_name,item.name);
        helper.setText(R.id.tv_1, changTVsize(str+"%",str.length()));
        helper.setText(R.id.tv_2, changTVsize(str1+"DAY",str1.length()));
        helper.setText(R.id.tv_3, changTVsize(str2+"FIL",str2.length()));
        helper.setText(R.id.tv_4, changTVsize(str4+"个",str4.length()));
        helper.setText(R.id.tv_5, changTVsize(str5+"个",str5.length()));
        String rtype="";
        String rtypeColor="";
        if(item.is_sell_out==0){
            rtype="可购买";
            rtypeColor="#58CF97";
        } else {
            rtype="已售罄";
            rtypeColor="#DB5576";
        }
        helper.setText(R.id.tv_6,rtype);
        helper.setTextColor(R.id.tv_6, Color.parseColor(rtypeColor));
        //如果已售罄把“立即购买”换成“已售罄”，不能进详情页  "is_sell_out": 0 	//是否售罄，0：否，1：是
    }


    public static SpannableString changTVsize(String value,int lastIndex) {
        SpannableString spannableString = new SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(new RelativeSizeSpan(0.6f), value.indexOf("."), lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        spannableString.setSpan(new RelativeSizeSpan(0.6f), lastIndex, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
