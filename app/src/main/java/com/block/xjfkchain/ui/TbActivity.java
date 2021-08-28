package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.TbAdapter;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.TbEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * TbActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/1, muwenlei, Create file
 */
public class TbActivity extends BusinessBaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.tv_ustd)
    TextView mTvUstd;
    @BindView(R.id.tv_fil)
    TextView mTvFil;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<TbEntity> mTbEntities = new ArrayList<>();
    private TbAdapter mTbAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tb;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("提币记录");
        mTbAdapter = new TbAdapter(mTbEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mTbAdapter);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
