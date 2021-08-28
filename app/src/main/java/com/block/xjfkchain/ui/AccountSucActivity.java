package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * AccountSucActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/2, muwenlei, Create file
 */
public class AccountSucActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_account_suc;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("我的实名认证");
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
