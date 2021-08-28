package com.block.xjfkchain.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.OrderAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseFragment;
import com.block.xjfkchain.data.CommonResponse;
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

public class OrderFragment extends BusinessBaseFragment {

    public static final String STATUS_ALL = "all";
    public static final String STATUS_NOPAY_STR = "wait";
    public static final String STATUS_CHECK_STR = "waitconfirm";
    public static final String STATUS_PROCEED_STR = "runing";
    public static final String STATUS_COMPLETE_STR = "complete";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private String mOrderType;

    private List<OrderEntity> mOrderEntities = new ArrayList<>();

    private OrderAdapter mOrderAdapter;

    private int mPageNo = 1;

    public static OrderFragment newInstance(String orderType) {
        OrderFragment orderFragment = new OrderFragment();
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
        mOrderAdapter = new OrderAdapter(mOrderEntities);
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
                    Intent intent = new Intent(getActivity(), PayActivity.class);
                    intent.putExtra("entity", mOrderEntities.get(position));
                    startActivity(intent);
//                }
            }
        });

        mOrderAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                if (mOrderEntities.get(position).status == OrderEntity.STATUS_NOPAY) {
                    new XPopup.Builder(getContext()).asConfirm("删除订单", "确定要删除该订单？",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    deleteOrder(position);
                                }
                            })
                            .show();
                    return true;
                }
                return false;
            }
        });

    }

    private void deleteOrder(final int position) {

        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", mOrderEntities.get(position).id + "");
        showLoadding();
        EasyHttp.post("/api/order/delete")
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
                            ToastUtils.showShort("删除成功");
                            mOrderEntities.remove(position);
                            mOrderAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }

    private void getData(boolean showLoading) {
        HashMap<String, String> maps = new HashMap<>();
        if (showLoading) {
            showLoadding();
        }
        maps.put("page", mPageNo + "");
        if (!STATUS_ALL.equals(mOrderType)) {
            maps.put("status", mOrderType + "");
        }
        EasyHttp.post("/api/order/list")
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
                            OrderListResponse productListResponse = JSONObject.parseObject(string, OrderListResponse.class);
                            if (productListResponse.data != null && productListResponse.data != null) {
                                mOrderEntities.addAll(productListResponse.data);
                            }
                            mOrderAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }


}
