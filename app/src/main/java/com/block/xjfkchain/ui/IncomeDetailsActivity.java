package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.ui.fragment.InComeFilFragment;
import com.block.xjfkchain.ui.fragment.InComeRewardFragment;
import com.block.xjfkchain.ui.fragment.InComeUsdtFragment;
import com.block.xjfkchain.ui.fragment.MyFragmentPagerAdapter;
import com.block.xjfkchain.ui.fragment.OrderFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class IncomeDetailsActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tab_gank)
    TabLayout mTabGank;
    @BindView(R.id.vp_type)
    ViewPager mVpType;


    private ArrayList<String> mTitleList = new ArrayList<>(3);
    private ArrayList<Fragment> mFragments = new ArrayList<>(3);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_income_detail;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("收益明细");
        initFragmentList();
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
        mVpType.setAdapter(myFragmentPagerAdapter);
        myFragmentPagerAdapter.notifyDataSetChanged();
        mTabGank.setTabMode(TabLayout.MODE_FIXED);
        mTabGank.setupWithViewPager(mVpType);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    private void initFragmentList() {
        mTitleList.clear();
        mTitleList.add("FIL收益");
        mTitleList.add("USDT收益");
        mTitleList.add("FIL奖励");
        mFragments.add(InComeFilFragment.newInstance(OrderFragment.STATUS_ALL));
        mFragments.add(InComeUsdtFragment.newInstance(OrderFragment.STATUS_NOPAY_STR));
        mFragments.add(InComeRewardFragment.newInstance(OrderFragment.STATUS_ALL));
    }

}
