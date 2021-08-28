package com.block.xjfkchain.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.MarketAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseFragment;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.MarkEntity;
import com.block.xjfkchain.data.MarketListResponse;
import com.block.xjfkchain.data.OrderListResponse;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MarketFragment extends BusinessBaseFragment {
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.ll_tip)
    LinearLayout mLlTip;
    @BindView(R.id.ll_increase)
    LinearLayout ll_increase;
    @BindView(R.id.ll_price)
    LinearLayout ll_price;
    @BindView(R.id.img_increase)
    ImageView img_increase;
    @BindView(R.id.img_price)
    ImageView img_price;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    int increase_=0;
    int price_=0;


    private static final int PERIOD = 5 * 1000;
    private static final int DELAY = 100;
    private Disposable mDisposable;


    private List<MarkEntity> mMarkEntities = new ArrayList<>();

    private int mPageNo = 1;

    private MarketAdapter mMarketAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_market;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mMarketAdapter = new MarketAdapter(mMarkEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mMarketAdapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageNo++;
                getData(false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageNo = 1;
                getData(false);
            }
        });
        ll_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (price_==0){
                    price_=1;
                    img_price.setImageResource(R.mipmap.icon_down);
                    Collections.sort(mMarkEntities, new Comparator<MarkEntity>() {
                        @Override
                        public int compare(MarkEntity u1, MarkEntity u2) {
                            return new BigDecimal(u1.price_usd).compareTo(new BigDecimal(u2.price_usd));
                        }
                    });
                }else if (price_==1){
                    price_=2;
                    img_price.setImageResource(R.mipmap.icon_top);
                    Collections.sort(mMarkEntities, new Comparator<MarkEntity>() {
                        @Override
                        public int compare(MarkEntity u1, MarkEntity u2) {
                            return new BigDecimal(u2.price_usd).compareTo(new BigDecimal(u1.price_usd));
                        }
                    });
                }else {
                    price_=1;
                    img_price.setImageResource(R.mipmap.icon_down);
                    Collections.sort(mMarkEntities, new Comparator<MarkEntity>() {
                        @Override
                        public int compare(MarkEntity u1, MarkEntity u2) {
                            return new BigDecimal(u1.price_usd).compareTo(new BigDecimal(u2.price_usd));
                        }
                    });
                }
                mMarketAdapter.notifyDataSetChanged();
            }
        });
        ll_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (increase_==0){
                    increase_=1;
                    img_increase.setImageResource(R.mipmap.icon_down);
                    Collections.sort(mMarkEntities, new Comparator<MarkEntity>() {
                        @Override
                        public int compare(MarkEntity u1, MarkEntity u2) {
                            return u1.percent_change_24h.compareTo(u2.percent_change_24h);
                        }
                    });
                }else if (increase_==1){
                    increase_=2;
                    img_increase.setImageResource(R.mipmap.icon_top);
                    Collections.sort(mMarkEntities, new Comparator<MarkEntity>() {
                        @Override
                        public int compare(MarkEntity u1, MarkEntity u2) {
                            return u2.percent_change_24h.compareTo(u1.percent_change_24h);
                        }
                    });
                }else {
                    increase_=1;
                    img_increase.setImageResource(R.mipmap.icon_down);
                    Collections.sort(mMarkEntities, new Comparator<MarkEntity>() {
                        @Override
                        public int compare(MarkEntity u1, MarkEntity u2) {
                            return u1.percent_change_24h.compareTo(u2.percent_change_24h);
                        }
                    });
                }
                mMarketAdapter.notifyDataSetChanged();

            }
        });
        getData(true);
        timeLoop();
    }
    private void timeLoop() {
        mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
                .map((aLong -> aLong + 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> getData(true));//getUnreadCount()执行的任务
    }

    @OnClick(R.id.ll_tip)
    public void onViewClicked() {

    }

    private void getData(boolean showLoading) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("page", mPageNo + "");
        maps.put("row","100");
        EasyHttp.get("/api/market/ticks")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        if (mRefreshLayout == null) {
                            return;
                        }
                        dismissLoadding();
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadMore();
                        e.printStackTrace();
                        ToastUtils.showShort("获取信息错误");
                    }

                    @Override
                    public void onSuccess(String string) {
                        if (mRefreshLayout == null) {
                            return;
                        }
                        dismissLoadding();
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadMore();
                        if (mPageNo == 1) {
                            mMarkEntities.clear();
                        }
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            MarketListResponse mMarketListResponse = JSONObject.parseObject(string, MarketListResponse.class);
                            if (mMarketListResponse.data != null && mMarketListResponse.data.list != null) {
                                mMarkEntities.addAll(mMarketListResponse.data.list);
                            }
                            mMarketAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDisposable != null) mDisposable.dispose();
    }
}
