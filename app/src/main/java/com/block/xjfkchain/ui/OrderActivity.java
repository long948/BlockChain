package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.ui.fragment.MyFragmentPagerAdapter;
import com.block.xjfkchain.ui.fragment.OrderFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * OrderActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class OrderActivity extends BusinessBaseActivity {
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
        mTvTitle.setText("我的订单");
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
        mTitleList.add("全部");
        mTitleList.add("未支付");
        mTitleList.add("审核");
        mTitleList.add("进行中");
        mTitleList.add("已完成");
        mFragments.add(OrderFragment.newInstance(OrderFragment.STATUS_ALL));
        mFragments.add(OrderFragment.newInstance(OrderFragment.STATUS_NOPAY_STR));
        mFragments.add(OrderFragment.newInstance(OrderFragment.STATUS_CHECK_STR));
        mFragments.add(OrderFragment.newInstance(OrderFragment.STATUS_PROCEED_STR));
        mFragments.add(OrderFragment.newInstance(OrderFragment.STATUS_COMPLETE_STR));
    }

}
