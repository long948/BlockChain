//package com.block.blockchain.ui;
//
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSONObject;
//import com.blankj.utilcode.util.ToastUtils;
//import com.block.blockchain.R;
//import com.block.blockchain.adapter.EarnAdapter;
//import com.block.blockchain.adapter.NodeAdapter;
//import com.block.blockchain.app.App;
//import com.block.blockchain.base.BusinessBaseActivity;
//import com.block.blockchain.data.CommonResponse;
//import com.block.blockchain.data.EarnEntity;
//import com.block.blockchain.data.EarnResponse;
//import com.block.blockchain.data.NodeEntity;
//import com.block.blockchain.data.NodeResponse;
//import com.block.blockchain.data.OrderListResponse;
//import com.scwang.smartrefresh.layout.SmartRefreshLayout;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
//import com.zhouyou.http.EasyHttp;
//import com.zhouyou.http.callback.SimpleCallBack;
//import com.zhouyou.http.exception.ApiException;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * Copyright (C) 2020, Relx
// * MyPointActivity
// * <p>
// * Description
// *
// * @author muwenleiØ
// * @version 1.0
// * <p>
// * Ver 1.0, 2020/6/7, muwenlei, Create file
// */
//public class EarnListActivity extends BusinessBaseActivity {
//    @BindView(R.id.tv_title)
//    TextView mTvTitle;
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.refresh_layout)
//    SmartRefreshLayout mRefreshLayout;
//
//    private List<EarnEntity> mEarnEntities = new ArrayList<>();
//    private EarnAdapter mEarnAdapter;
//
//
//    private int mPageNo = 1;
//
//
//    @Override
//    protected int getContentViewId() {
//        return R.layout.activity_my_earn;
//    }
//
//    @Override
//    protected void onViewCreated(Bundle savedInstanceState) {
//        mTvTitle.setText("我的收益");
//        mEarnAdapter = new EarnAdapter(mEarnEntities, false);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter(mEarnAdapter);
//
//        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshLayout) {
//                mPageNo++;
//                getData(false);
//            }
//
//            @Override
//            public void onRefresh(RefreshLayout refreshLayout) {
//                mPageNo = 1;
//                getData(false);
//            }
//        });
//        getData(true);
//    }
//
//
//    private void getData(boolean showLoading) {
//        HashMap<String, String> maps = new HashMap<>();
//        if (showLoading) {
//            showLoadding();
//        }
//        maps.put("page", mPageNo + "");
//        EasyHttp.post("/api/reward/list")
//                .params(maps)
//                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
//                .execute(new SimpleCallBack<String>() {
//                    @Override
//                    public void onError(ApiException e) {
//                        if (mRefreshLayout == null) {
//                            return;
//                        }
//                        dismissLoadding();
//                        mRefreshLayout.finishRefresh();
//                        mRefreshLayout.finishLoadMore();
//                        e.printStackTrace();
//                        ToastUtils.showShort("获取信息错误");
//                    }
//
//                    @Override
//                    public void onSuccess(String string) {
//                        if (mRefreshLayout == null) {
//                            return;
//                        }
//                        dismissLoadding();
//                        mRefreshLayout.finishRefresh();
//                        mRefreshLayout.finishLoadMore();
//                        if (mPageNo == 1) {
//                            mEarnEntities.clear();
//                        }
//                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
//                        if (returnResponse.isSuc()) {
//                            EarnResponse productListResponse = JSONObject.parseObject(string, EarnResponse.class);
//                            if (productListResponse.data != null && productListResponse.data != null) {
//                                mEarnEntities.addAll(productListResponse.data.list);
//                            }
//                            mEarnAdapter.notifyDataSetChanged();
//                        } else {
//                            ToastUtils.showShort(returnResponse.msg);
//                        }
//
//                    }
//                });
//    }
//
//    @OnClick(R.id.iv_back)
//    public void onViewClicked() {
//        finish();
//    }
//
//}
