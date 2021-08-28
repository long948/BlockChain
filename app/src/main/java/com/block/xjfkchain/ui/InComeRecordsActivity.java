package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.EarningAdapter;
import com.block.xjfkchain.adapter.InComeListAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.EarnResponse;
import com.block.xjfkchain.data.EarningEntity;
import com.block.xjfkchain.data.ExamCourseSection;
import com.block.xjfkchain.data.HomeDataResponse;
import com.block.xjfkchain.data.InComeEntity;
import com.block.xjfkchain.data.InComeListResponse;
import com.block.xjfkchain.data.Level0Item;
import com.block.xjfkchain.data.Level1Item;
import com.block.xjfkchain.data.UserEntity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.entity.MultiItemEntity;
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

public class InComeRecordsActivity extends BusinessBaseActivity {
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_2)
    TextView tv_2;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.iv_icon)
    ImageView mIvIamge;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private List<MultiItemEntity> mInComeEntityBuff = new ArrayList<>();
    private InComeListAdapter mInComeListAdapter;

    private UserEntity userEntity;
    private int mPageNo = 1;

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
    @Override
    protected int getContentViewId() {
        return R.layout.activity_in_come_records;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mInComeListAdapter = new InComeListAdapter(mInComeEntityBuff);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mInComeListAdapter);

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getInComeRecords(false, true);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageNo = 1;
                getInComeRecords(false, false);
            }
        });
        tv_2.setText(changTVsize("0.00"));//累计收益
        tv_1.setText(changTVsize("0.00"));    // 累计垫付质押
        getInComeRecords(true, false);
        getHomeData();
        getData();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void getData() {
        userEntity = App.getApplication().getEntity();
        tv_name.setText(userEntity.name);
        Glide.with(this).load(userEntity.avatar).into(mIvIamge);
    }

    private void getHomeData() {
        HashMap<String, String> maps = new HashMap<>();
        EasyHttp.post("/api/dayincome/incomecount")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                        ToastUtils.showShort("获取信息错误");
                        mRefreshLayout.finishRefresh();
                    }

                    @Override
                    public void onSuccess(String string) {
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            HomeDataResponse mHomeDataResponse = JSONObject.parseObject(string, HomeDataResponse.class);
                            if (mHomeDataResponse != null && mHomeDataResponse.data != null) {
                                tv_2.setText(changTVsize(mHomeDataResponse.data.total_income));//累计收益
                                tv_1.setText(changTVsize(mHomeDataResponse.data.total_company_pledge_fil));    // 累计垫付质押
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                        mRefreshLayout.finishRefresh();
                    }
                });
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

    public static SpannableString changTVsize(String value) {
        SpannableString spannableString = new SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(new RelativeSizeSpan(0.6f), value.indexOf("."), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}