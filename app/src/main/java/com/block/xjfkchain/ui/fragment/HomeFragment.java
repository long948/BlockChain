package com.block.xjfkchain.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseFragment;
import com.block.xjfkchain.data.BannerEntity;
import com.block.xjfkchain.data.BannerResponse;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.GongGaoEntity;
import com.block.xjfkchain.data.GongGaoResponse;
import com.block.xjfkchain.data.NoticeResponse;
import com.block.xjfkchain.ui.CommonWebActivity;
import com.block.xjfkchain.ui.NoticeActivity;
import com.block.xjfkchain.ui.SearchActivity;
import com.block.xjfkchain.widget.autoscrollviewpager.BGABanner;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BusinessBaseFragment implements OnTabSelectListener {


    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.rl_tip)
    RelativeLayout mRlTip;
    @BindView(R.id.banner)
    BGABanner mBanner;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.iv_unread)
    ImageView mIvUnRead;
    @BindView(R.id.ll_tip)
    LinearLayout mLlTip;
    @BindView(R.id.sl_layout)
    SlidingTabLayout mSlLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    private List<HomeItemFragment> homeItemFragments = new ArrayList<>();

    private List<BannerEntity> mBannerEntities = new ArrayList<>();

    private MyPagerAdapter mAdapter;

    private final String[] mTitles = {
            "最新", "热榜", "私塾"
    };

    private int currentIndex = 0;

    private GongGaoEntity mGongGaoEntity;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {

        HomeItemFragment newItemFragment = HomeItemFragment.getInstance(1);
        newItemFragment.setRefreshListener(new HomeItemFragment.RefreshListener() {
            @Override
            public void onFinish() {
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }
        });
        newItemFragment.setUnreadNumListener(new HomeItemFragment.UnreadNumListener() {
            @Override
            public void refreshNum(int type, int num) {
                refreshDot(type, num);
            }
        });
        HomeItemFragment hotItemFragment = HomeItemFragment.getInstance(2);
        hotItemFragment.setRefreshListener(new HomeItemFragment.RefreshListener() {
            @Override
            public void onFinish() {
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }
        });
        hotItemFragment.setUnreadNumListener(new HomeItemFragment.UnreadNumListener() {
            @Override
            public void refreshNum(int type, int num) {
                refreshDot(type, num);
            }
        });
        HomeItemFragment privateItemFragment = HomeItemFragment.getInstance(3);
        privateItemFragment.setRefreshListener(new HomeItemFragment.RefreshListener() {
            @Override
            public void onFinish() {
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }
        });
        privateItemFragment.setUnreadNumListener(new HomeItemFragment.UnreadNumListener() {
            @Override
            public void refreshNum(int type, int num) {
                refreshDot(type, num);
            }
        });
        homeItemFragments.add(newItemFragment);
        homeItemFragments.add(hotItemFragment);
        homeItemFragments.add(privateItemFragment);
        initBanner();

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                HomeItemFragment homeItemFragment = homeItemFragments.get(currentIndex);
                if (homeItemFragment != null) {
                    homeItemFragment.loadMore();
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                HomeItemFragment homeItemFragment = homeItemFragments.get(currentIndex);
                if (homeItemFragment != null) {
                    homeItemFragment.refreshData();
                }
            }
        });

        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mSlLayout.setViewPager(mViewPager, mTitles);
        mViewPager.setOffscreenPageLimit(3);
        getBanner();
    }

    void refreshDot(int type, int num) {
        if (num > 0) {
            mSlLayout.showDot(type - 1);
        } else {
            mSlLayout.hideMsg(type - 1);
        }
    }


    void initBanner() {
        mBanner.setDelegate(new BGABanner.Delegate<View, BannerEntity>() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, BannerEntity bannerEntity, int position) {
            }
        });
        mBanner.setAdapter(new BGABanner.Adapter<View, BannerEntity>() {
            @Override
            public void fillBannerItem(BGABanner banner, View itemView, BannerEntity model, int position) {
                ImageView imageView = itemView.findViewById(R.id.iv_image);
                Glide.with(HomeFragment.this)
                        .load(model.image)
                        .into(imageView);
            }
        });
        mBanner.setData(R.layout.item_banner, mBannerEntities, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getTipUnRead();
        getGongGao();
    }

    private void getBanner() {
        HashMap<String, String> maps = new HashMap<>();
        showLoadding();
        EasyHttp.post("/api/banner/list")
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
                            BannerResponse bannerResponse = JSONObject.parseObject(string, BannerResponse.class);
                            if (bannerResponse != null && bannerResponse.data != null && bannerResponse.data.list != null) {
                                mBannerEntities.addAll(bannerResponse.data.list);
                                mBanner.setData(R.layout.item_banner, mBannerEntities, null);
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }

    private void getTipUnRead() {
        HashMap<String, String> maps = new HashMap<>();
        EasyHttp.post("/api/notice/list")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                        ToastUtils.showShort("获取信息错误");
                    }

                    @Override
                    public void onSuccess(String string) {
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            NoticeResponse noticeResponse = JSONObject.parseObject(string, NoticeResponse.class);
                            if (noticeResponse != null && noticeResponse.data != null) {
                                refreshUnRead(noticeResponse.data.unread_notice_num);
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }

    private void getGongGao() {
        HashMap<String, String> maps = new HashMap<>();
        EasyHttp.post("/api/article/gonggao")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                        ToastUtils.showShort("获取信息错误");
                    }

                    @Override
                    public void onSuccess(String string) {
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            GongGaoResponse response = JSONObject.parseObject(string, GongGaoResponse.class);
                            if (response != null && response.data != null) {
                                refreshGongGao(response.data);
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }

    public void refreshUnRead(int num) {
        if (num > 0) {
            mIvUnRead.setVisibility(View.VISIBLE);
        } else {
            mIvUnRead.setVisibility(View.GONE);
        }
    }

    public void refreshGongGao(GongGaoEntity gongGaoEntity) {
        mGongGaoEntity = gongGaoEntity;
        if (gongGaoEntity == null && TextUtils.isEmpty(gongGaoEntity.title)) {
            mLlTip.setVisibility(View.GONE);
        } else {
            mLlTip.setVisibility(View.VISIBLE);
            mTvTip.setText(gongGaoEntity.title);
        }
    }


    @OnClick(R.id.ll_search)
    public void onMLlSearchClicked() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_tip)
    public void onMRlTipClicked() {
        Intent intent = new Intent(getActivity(), NoticeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_tip)
    public void onMLlTipClicked() {
        if (mGongGaoEntity == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), CommonWebActivity.class);
        intent.putExtra("url", mGongGaoEntity.url);
        startActivity(intent);
    }

    @Override
    public void onTabSelect(int position) {
        currentIndex = position;
    }

    @Override
    public void onTabReselect(int position) {
        currentIndex = position;
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return homeItemFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return homeItemFragments.get(position);
        }
    }

}
