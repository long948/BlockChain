package com.block.xjfkchain.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.FinancialOrder1Adapter;
import com.block.xjfkchain.adapter.FinancialOrderAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseFragment;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.FinancialOrderListResponse;
import com.block.xjfkchain.data.OrderEntity;
import com.block.xjfkchain.ui.FinancialTransferOutActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

public class FinancialOrderFragment extends BusinessBaseFragment {

    public static final String STATUS_ALL = "0";
    public static final String STATUS_NOPAY_STR = "1";
    public static final String STATUS_CHECK_STR = "2";
    public static final String STATUS_PROCEED_STR = "3";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private String mOrderType;

    private List<OrderEntity> mOrderEntities = new ArrayList<>();

    private FinancialOrder1Adapter mOrderAdapter;

    private int mPageNo = 1;

    public static FinancialOrderFragment newInstance(String orderType) {
        FinancialOrderFragment orderFragment = new FinancialOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", orderType);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mOrderType = getArguments().getString("type");
        mOrderAdapter = new FinancialOrder1Adapter(mOrderEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mOrderAdapter);

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mPageNo++;
                getData(false);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPageNo = 1;
                getData(false);
            }
        });
        getData(true);
        mOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (mOrderEntities.get(position).status == OrderEntity.STATUS_NOPAY || mOrderEntities.get(position).status == OrderEntity.STATUS_CHECK) {
                    Intent intent = new Intent(getActivity(), FinancialTransferOutActivity.class);
                    intent.putExtra("buy_record_id", mOrderEntities.get(position).id.toString());
                    intent.putExtra("fil_product_id", mOrderEntities.get(position).fil_product_id.toString());
                    startActivity(intent);
//                }
            }
        });

    }

    private void getData(boolean showLoading) {
        HashMap<String, String> maps = new HashMap<>();
        if (showLoading) {
            showLoadding();
        }
        maps.put("page", mPageNo + "");
        maps.put("status", mOrderType + "");
        EasyHttp.post("/api/financial/orders")
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
                            mOrderEntities.clear();
                        }
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            FinancialOrderListResponse productListResponse = JSONObject.parseObject(string, FinancialOrderListResponse.class);
                            if (productListResponse.data != null && productListResponse.data.list != null) {
                                mOrderEntities.addAll(productListResponse.data.list);
                            }
                            mOrderAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }


}
