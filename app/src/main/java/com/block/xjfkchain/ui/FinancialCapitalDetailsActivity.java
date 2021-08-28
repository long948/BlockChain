package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.ui.fragment.FinancialCapitalDetailsFragment;
import com.block.xjfkchain.ui.fragment.MyFragmentPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class FinancialCapitalDetailsActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tab_gank)
    TabLayout mTabGank;
    @BindView(R.id.vp_type)
    ViewPager mVpType;


    private ArrayList<String> mTitleList = new ArrayList<>(4);
    private ArrayList<Fragment> mFragments = new ArrayList<>(4);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_order;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("资金明细");
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
        mTitleList.add("买入记录");
        mTitleList.add("转出记录");
        mTitleList.add("收益记录");
        mFragments.add(FinancialCapitalDetailsFragment.newInstance(FinancialCapitalDetailsFragment.STATUS_ALL));
        mFragments.add(FinancialCapitalDetailsFragment.newInstance(FinancialCapitalDetailsFragment.STATUS_NOPAY_STR));
        mFragments.add(FinancialCapitalDetailsFragment.newInstance(FinancialCapitalDetailsFragment.STATUS_CHECK_STR));
    }

}
