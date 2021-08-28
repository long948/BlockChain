package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.UserEntity;
import com.block.xjfkchain.data.UserResponse;
import com.block.xjfkchain.utils.ClipboardUtils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * FilEarningActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/10/20, muwenlei, Create file
 */
public class FilEarningActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.tv_fil)
    TextView mTvFil;
    @BindView(R.id.tv_wallet)
    TextView mTvWallet;
    @BindView(R.id.tv_cz)
    TextView mTvCz;
    @BindView(R.id.tv_tx)
    TextView mTvTx;

    private UserEntity entity;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fil_earning;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("我的钱包");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("收支记录");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void onMTvRightClicked() {
        Intent intent = new Intent(this, FileEarnListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_cz)
    public void onMTvCzClicked() {
        if (entity == null) {
            return;
        }
        Intent intent = new Intent(this, RechargeActivity.class);
        intent.putExtra("address", entity.wallet);
        startActivity(intent);

    }

    @OnClick(R.id.tv_tx)
    public void onMTvTxClicked() {
        Intent intent = new Intent(FilEarningActivity.this, FilWithDrawActivity.class);
        intent.putExtra("entity", entity);
        startActivity(intent);
    }

    private void getData() {
        HashMap<String, String> maps = new HashMap<>();
        showLoadding();
        LogUtils.e("Bearer " + App.getApplication().getUserEntity().token);
        EasyHttp.post("/api/profile")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        dismissLoadding();
                        e.printStackTrace();
                        ToastUtils.showShort("获取信息错误");
                    }

                    @Override
                    public void onSuccess(String string) {
                        dismissLoadding();
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            UserEntity userEntity = JSONObject.parseObject(string, UserResponse.class).data;
                            if (userEntity != null) {
                                App.getApplication().setUserEntity(userEntity);
                                showDataView(userEntity);
                            }
                        } else if (returnResponse.code >= 9000 || returnResponse.code == 401) {
                            Intent intent = new Intent(FilEarningActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }

    private void showDataView(UserEntity entity) {
        this.entity = entity;
        mTvFil.setText(entity.fil + "");
        mTvWallet.setText(entity.wallet + "");
    }


    @OnClick(R.id.tv_wallet)
    public void onViewClicked() {
        if (entity != null) {
            ClipboardUtils.copyText(entity.wallet);
            ToastUtils.showShort("复制成功");
        }
    }
}
