package com.block.xjfkchain.adapter;

import android.view.View;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.Level0Item;
import com.block.xjfkchain.data.Level1Item;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;


public class InComeRewardListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;


    public InComeRewardListAdapter(List data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_income_head);
        addItemType(TYPE_LEVEL_1, R.layout.item_income_info);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final Level0Item lv0 = (Level0Item) item;
                holder.setText(R.id.tv_date, lv0.title)
                        .setText(R.id.tv_num, lv0.subTitle);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
//                            if (pos % 3 == 0) {
//                                expandAll(pos, false);
//                            } else {
                            expand(pos);
//                            }
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final Level1Item lv1 = (Level1Item) item;
                holder.setText(R.id.tv_info_num_1, lv1.mInComeEntity.lockup_fil);//75%未释放收益
                holder.setText(R.id.tv_info_num_2, lv1.mInComeEntity.unlock_fil);//25%直接释放
                holder.setText(R.id.tv_info_num_3, lv1.mInComeEntity.return_fil);//线性释放收益
                holder.setText(R.id.tv_info_num_4, lv1.mInComeEntity.company_pledge_fil);//公司垫付质押

//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int pos = holder.getAdapterPosition();
//                        if (lv1.isExpanded()) {
//                            collapse(pos, false);
//                        } else {
//                            expand(pos, false);
//                        }
//                    }
//                });
                break;
        }
    }

}
