package com.block.xjfkchain.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.InComeListAdapter;
import com.block.xjfkchain.adapter.OrderAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseFragment;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.InComeListResponse;
import com.block.xjfkchain.data.Level0Item;
import com.block.xjfkchain.data.Level1Item;
import com.block.xjfkchain.data.OrderEntity;
import com.block.xjfkchain.data.OrderListResponse;
import com.block.xjfkchain.ui.PayActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
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

public class InComeFilFragment extends BusinessBaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private int mPageNo = 1;
    private List<MultiItemEntity> mInComeEntityBuff = new ArrayList<>();
    private InComeListAdapter mInComeListAdapter;


    public static InComeFilFragment newInstance(String orderType) {
        InComeFilFragment orderFragment = new InComeFilFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", orderType);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_fil_income;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mInComeListAdapter = new InComeListAdapter(mInComeEntityBuff);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mInComeListAdapter);
        View empty = LayoutInflater.from(getActivity()).inflate(R.layout.item_empty,null,false);
        mInComeListAdapter.setEmptyView(empty);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getInComeRecords(false, true);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPageNo = 1;
                getInComeRecords(false, false);
            }
        });
        getInComeRecords(true, false);
    }

    private void getInComeRecords(boolean showLoading, boolean isLoadMore) {
        HashMap<String, String> maps = new HashMap<>();
        if (showLoading) {
            showLoadding();
        }
        maps.put("page", mPageNo + "");
        EasyHttp.post("/api/dayincome/getIncomeList")
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
                            mInComeEntityBuff.clear();
                        }
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            InComeListResponse mInComeListResponse = JSONObject.parseObject(string, InComeListResponse.class);
                            if (mInComeListResponse.data != null && mInComeListResponse.data != null) {
                                if (mInComeListResponse.data.page.total_pages == mInComeListResponse.data.page.current_page) {
                                    mRefreshLayout.setNoMoreData(true);
                                } else {
                                    if (isLoadMore) {
                                        mPageNo = mInComeListResponse.data.page.next_page;
                                    }
                                }
                                for (int i = 0; i < mInComeListResponse.data.list.size(); i++) {
                                    Level0Item mLevel0Item = new Level0Item(mInComeListResponse.data.list.get(i).created_at, mInComeListResponse.data.list.get(i).real_income_fil);
                                    Level1Item mLevel1Item = new Level1Item(mInComeListResponse.data.list.get(i));
                                    mLevel0Item.addSubItem(mLevel1Item);
                                    mInComeEntityBuff.add(mLevel0Item);
                                }
                            }
                            mInComeListAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }
}
