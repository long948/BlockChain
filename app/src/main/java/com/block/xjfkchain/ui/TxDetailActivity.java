package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.TxEntity;

import butterknife.BindView;
import butterknife.OnClick;

public class TxDetailActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_acconut)
    TextView mTvAcconut;
    @BindView(R.id.tv_account_username)
    TextView mTvAccountUsername;
    @BindView(R.id.ll_account_username)
    LinearLayout mLlAccountUsername;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_remark)
    TextView mTvRemark;
    @BindView(R.id.tv_status)
    TextView mTvStatus;

    private TxEntity mTxEntity;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tx_detail;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("提现详情");
        mTxEntity = (TxEntity) getIntent().getSerializableExtra("entity");

        mTvMoney.setText(mTxEntity.amount + " USDT");

        String account = mTxEntity.account;
        if ("0".equals(account)) {
            mTvType.setText("银行卡");
            mTvAccountUsername.setText(mTxEntity.account_name);
            mLlAccountUsername.setVisibility(View.VISIBLE);
        } else if ("1".equals(account)) {
            mTvType.setText("支付宝");
            mLlAccountUsername.setVisibility(View.GONE);
        } else if ("2".equals(account)) {
            mTvType.setText("钱包");
            mLlAccountUsername.setVisibility(View.GONE);
        } else {
            mTvType.setText("银行卡");
            mLlAccountUsername.setVisibility(View.GONE);
        }
        mTvAcconut.setText(mTxEntity.account_no);
        mTvRemark.setText(mTxEntity.remark);
        mTvTime.setText(mTxEntity.created_at);
        mTvStatus.setText(mTxEntity.status_txt);


    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
