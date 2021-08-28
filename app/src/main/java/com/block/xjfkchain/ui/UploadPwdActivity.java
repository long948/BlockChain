package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * UploadPwdActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/2, muwenlei, Create file
 */
public class UploadPwdActivity extends BusinessBaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.et_old_pwd)
    EditText mEtOldPwd;
    @BindView(R.id.et_newpwd)
    EditText mEtNewpwd;
    @BindView(R.id.et_repwd)
    EditText mEtRepwd;
    @BindView(R.id.tv_forgetpwd)
    TextView mTvForgetpwd;
    @BindView(R.id.iv_save)
    ImageView mIvSave;

    private int mType;//1 是登录密码修改  2是资金密码修改

    @Override
    protected int getContentViewId() {
        return R.layout.activity_update_login_pwd;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mType = getIntent().getIntExtra("type", 1);
        if (mType == 1) {
            mTvTitle.setText("登录密码修改");
        } else {
            mTvTitle.setText("资金密码修改");
        }


    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_forgetpwd)
    public void onMTvForgetpwdClicked() {

    }

    @OnClick(R.id.iv_save)
    public void onMIvSaveClicked() {
    }
}
