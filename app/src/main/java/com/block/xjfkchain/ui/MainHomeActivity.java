package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.ui.fragment.NewHomeFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainHomeActivity extends BusinessBaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_home;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new NewHomeFragment()).commitAllowingStateLoss();
        mTvTitle.setText("收益记录");
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}