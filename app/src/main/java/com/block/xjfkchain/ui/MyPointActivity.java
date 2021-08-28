package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.PointAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.PointEntity;
import com.block.xjfkchain.data.PointResponse;
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
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * MyPointActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class MyPointActivity extends BusinessBaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private List<PointEntity> mPointEntities = new ArrayList<>();
    private PointAdapter mPointAdapter;

    private int mPageNo = 1;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_mypoint;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("我的邀请");
        mPointAdapter = new PointAdapter(mPointEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mPointAdapter);

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

        mPointAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PointEntity pointEntity = mPointEntities.get(position);
                if (pointEntity.node_id == null) {
                    Intent intent = new Intent(MyPointActivity.this, NodeActivity.class);
                    intent.putExtra("id", pointEntity.id);
                    startActivity(intent);
                }
            }
        });
        getData(true);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPageNo = 1;
        getData(true);
    }

    private void getData(boolean showLoading) {
        HashMap<String, String> maps = new HashMap<>();
        if (showLoading) {
            showLoadding();
        }
        Log.e("EasyHttp", "Bearer " + App.getApplication().getUserEntity().token);
        maps.put("page", mPageNo + "");
        EasyHttp.post("/api/node/list")
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
                            mPointEntities.clear();
                        }
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            PointResponse productListResponse = JSONObject.parseObject(string, PointResponse.class);
                            if (productListResponse.data != null && productListResponse.data.list != null) {
                                mPointEntities.addAll(productListResponse.data.list);
                            }
                            mPointAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
