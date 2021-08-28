package com.block.xjfkchain.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.EarningAdapter;
import com.block.xjfkchain.adapter.OrderAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseFragment;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.EarnResponse;
import com.block.xjfkchain.data.EarningEntity;
import com.block.xjfkchain.data.OrderEntity;
import com.block.xjfkchain.data.OrderListResponse;
import com.block.xjfkchain.ui.PayActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
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

public class InComeUsdtFragment extends BusinessBaseFragment {

    public static final String STATUS_ALL = "all";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private List<EarningEntity> mEarnEntities = new ArrayList<>();
    private EarningAdapter mEarningAdapter;
    private int mPageNo = 1;

    public static InComeUsdtFragment newInstance(String orderType) {
        InComeUsdtFragment orderFragment = new InComeUsdtFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", orderType);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_usdt_income;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mEarningAdapter = new EarningAdapter(mEarnEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mEarningAdapter);
        View empty = LayoutInflater.from(getActivity()).inflate(R.layout.item_empty,null,false);
        mEarningAdapter.setEmptyView(empty);
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
        getData(true);

    }


    private void getData(boolean showLoading) {
        HashMap<String, String> maps = new HashMap<>();
        if (showLoading) {
            showLoadding();
        }
        maps.put("page", mPageNo + "");
        EasyHttp.post("/api/reward/list")
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
                            mEarnEntities.clear();
                        }
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            EarnResponse productListResponse = JSONObject.parseObject(string, EarnResponse.class);
                            if (productListResponse.data != null && productListResponse.data != null) {
                                mEarnEntities.addAll(productListResponse.data.list);
                            }
                            mEarningAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }


}
