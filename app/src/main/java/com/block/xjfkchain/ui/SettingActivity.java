package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.constant.Constants;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * SettingActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/2, muwenlei, Create file
 */
public class SettingActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.ll_setpwd)
    LinearLayout mLlSetpwd;
    @BindView(R.id.ll_setpwd_money)
    LinearLayout mLlSetpwdMoney;
    @BindView(R.id.ll_setpwd_walet)
    LinearLayout mLlSetpwdWalet;
    @BindView(R.id.ll_account_update)
    LinearLayout mLlAccountUpdate;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("设置");

    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }

    @OnClick(R.id.ll_setpwd)
    public void onMLlSetpwdClicked() {
        Intent intent = new Intent(this, UpdatePwdByCodeActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    @OnClick(R.id.ll_setpwd_money)
    public void onMLlSetpwdMoneyClicked() {
        Intent intent = new Intent(this, UpdatePwdByCodeActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    @OnClick(R.id.ll_setpwd_walet)
    public void onMLlSetpwdWaletClicked() {
        Intent intent = new Intent(this, CashActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_account_update)
    public void onMLlAccountUpdateClicked() {
        Intent intent = new Intent(this, UpdateAccountActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_logout)
    public void onIvLogoutClicked() {
        SPUtils.getInstance().put(Constants.SP_USER, "");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
