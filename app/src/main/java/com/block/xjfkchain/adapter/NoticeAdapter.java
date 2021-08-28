package com.block.xjfkchain.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.NoticeEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (C) 2020, Relx
 * NoticeAdapter
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/2, muwenlei, Create file
 */
public class NoticeAdapter extends BaseQuickAdapter<NoticeEntity, BaseViewHolder> {
    public NoticeAdapter(@Nullable List<NoticeEntity> data) {
        super(R.layout.item_tip, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeEntity item) {
        helper.setText(R.id.tv_title, item.subject);
        helper.setText(R.id.tv_time, item.created_at);
        if (item.is_read == 0) {
            helper.setVisible(R.id.iv_unread, true);
        } else {
            helper.setVisible(R.id.iv_unread, false);
        }
    }

    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.iv_unread)
        ImageView mIvUnread;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
