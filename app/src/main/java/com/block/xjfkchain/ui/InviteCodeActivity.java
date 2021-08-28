package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.UserEntity;
import com.block.xjfkchain.utils.ClipboardUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;

public class InviteCodeActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.iv_icon)
    ImageView mIvCode;
    @BindView(R.id.tv_address)
    TextView mTvAddress;

    private String mQrcode;
    private String mShareurl;

    private UserEntity entity;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_invite_code;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("我的邀请码");
        mTvCode.setText("我的邀请码:" + App.getApplication().getUserEntity().user.union_uid + "");
        entity = (UserEntity) getIntent().getSerializableExtra("entity");
        mQrcode = getIntent().getStringExtra("qrcode");
        mShareurl = getIntent().getStringExtra("shareurl");
        Glide.with(this).load(mQrcode).into(mIvCode);
        mTvAddress.setText(mShareurl);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }


//    @OnClick(R.id.ll_cash)
//    public void onMLlCashClicked() {
//        Intent intent = new Intent(this, CashActivity.class);
//        intent.putExtra("entity", entity);
//        startActivity(intent);
//    }

    @OnClick(R.id.tv_address)
    public void onViewAddressClicked() {
        ClipboardUtils.copyText(mShareurl);
        ToastUtils.showShort("复制成功");

    }
}
