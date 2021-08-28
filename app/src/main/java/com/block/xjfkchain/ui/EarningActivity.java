package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.EarningAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.EarnResponse;
import com.block.xjfkchain.data.EarningEntity;
import com.block.xjfkchain.data.UserEntity;
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
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * TbActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/1, muwenlei, Create file
 */
public class EarningActivity extends BusinessBaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private List<EarningEntity> mEarnEntities = new ArrayList<>();

    private EarningAdapter mEarningAdapter;

    private int mPageNo = 1;

    private UserEntity mUserEntity;

    private View mHeaderView;
    private ViewHolder mViewHolder;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tb;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("收益记录");

        mUserEntity = (UserEntity) getIntent().getSerializableExtra("entity");
        mEarningAdapter = new EarningAdapter(mEarnEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mEarningAdapter);
        initHeader();
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

    private void initHeader() {
        mHeaderView = View.inflate(this, R.layout.item_earning_header, null);
        mViewHolder = new ViewHolder(mHeaderView);
        mEarningAdapter.addHeaderView(mHeaderView);
        mViewHolder.mTvUstd.setText(mUserEntity.usdt);
        mViewHolder.mTvTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EarningActivity.this, TxActivity.class);
                startActivity(intent);
            }
        });
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
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

    static class ViewHolder {
        @BindView(R.id.tv_ustd)
        TextView mTvUstd;
        @BindView(R.id.tv_fil)
        TextView mTvFil;
        @BindView(R.id.tv_tx)
        TextView mTvTx;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
