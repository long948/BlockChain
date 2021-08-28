package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.ui.fragment.FileEarnLisFragment;
import com.block.xjfkchain.ui.fragment.MyFragmentPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * FileEarnListActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/10/22, muwenlei, Create file
 */
public class FileEarnListActivity extends BusinessBaseActivity {

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
        return R.layout.activity_fil_earning_list;

    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("收支记录");
        initFragmentList();
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
        mVpType.setAdapter(myFragmentPagerAdapter);
        myFragmentPagerAdapter.notifyDataSetChanged();
        mTabGank.setTabMode(TabLayout.MODE_FIXED);
        mTabGank.setupWithViewPager(mVpType);
    }

    private void initFragmentList() {
        mTitleList.clear();
        mTitleList.add("全部");
        mTitleList.add("充值");
        mTitleList.add("提现");
        mFragments.add(FileEarnLisFragment.newInstance(""));
        mFragments.add(FileEarnLisFragment.newInstance("recharge"));
        mFragments.add(FileEarnLisFragment.newInstance("withdraw"));
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

}
