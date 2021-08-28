package com.block.xjfkchain.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.FinancialAdapter;
import com.block.xjfkchain.adapter.ProductAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseFragment;
import com.block.xjfkchain.constant.Constants;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.FinancialBuffEntity;
import com.block.xjfkchain.data.FinancialListResponse;
import com.block.xjfkchain.data.FinancialResponse;
import com.block.xjfkchain.data.ProductEntity;
import com.block.xjfkchain.data.ProductListResponse;
import com.block.xjfkchain.ui.FinancialCapitalDetailsActivity;
import com.block.xjfkchain.ui.FinancialDetailActivity;
import com.block.xjfkchain.ui.FinancialCapitalDetailsActivity;
import com.block.xjfkchain.ui.FinancialOrderActivity;
import com.block.xjfkchain.ui.FinancialTransferOutActivity;
import com.block.xjfkchain.ui.ProductDetailActivity;
import com.bumptech.glide.Glide;
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
import butterknife.ButterKnife;

public class FinancialFragment extends BusinessBaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private FinancialAdapter mFinancialAdapter;
    private List<FinancialBuffEntity> mProductEntities = new ArrayList<>();

    private View mHeaderView;
    private ViewHolder mViewHolder;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_financial;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mRefreshLayout.setEnableLoadMore(false);
        mFinancialAdapter = new FinancialAdapter(mProductEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mFinancialAdapter);
        initHeader();
        mFinancialAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if ( mProductEntities.get(position).is_sell_out==0){
                    Intent intent = new Intent(getContext(), FinancialDetailActivity.class);
                    intent.putExtra("id", mProductEntities.get(position).id);
                    startActivity(intent);
                }
            }
        });
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData(false);
            }
        });
        getData(true);
    }

    private void initHeader() {
        mHeaderView = View.inflate(getContext(), R.layout.item_financcial_header, null);
        mViewHolder = new ViewHolder(mHeaderView);
        mFinancialAdapter.addHeaderView(mHeaderView);
        mViewHolder.tv_1.setText(changTVsize("345.34"));
        mViewHolder.tv_2.setText(changTVsize("345.34"));
        mViewHolder.tv_3.setText(changTVsize("345.34"));
        mViewHolder.tv_transferout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FinancialOrderActivity.class);
                startActivity(intent);
            }
        });
        mViewHolder.tv_mingxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FinancialCapitalDetailsActivity.class);
                startActivity(intent);
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


    private void getData(boolean showLoading) {
        HashMap<String, String> maps = new HashMap<>();
        if (showLoading) {
            showLoadding();
        }
        EasyHttp.post("/api/financial/list")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        dismissLoadding();
                        mRefreshLayout.finishRefresh();
                        e.printStackTrace();
                        ToastUtils.showShort("获取信息错误");
                    }

                    @Override
                    public void onSuccess(String string) {
                        dismissLoadding();
                        mRefreshLayout.finishRefresh();
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            mProductEntities.clear();
                            FinancialListResponse productListResponse = JSONObject.parseObject(string, FinancialListResponse.class);
                            if (productListResponse.data != null && productListResponse.data.list != null) {
                                mProductEntities.addAll(productListResponse.data.list);
                            }
                            mFinancialAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
        HashMap<String, String> map_1 = new HashMap<>();
        map_1.put("fil_product_id", "0");
        EasyHttp.post("/api/financial/getfil")
                .params(map_1)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                        ToastUtils.showShort("获取信息错误");
                    }

                    @Override
                    public void onSuccess(String string) {
                        dismissLoadding();
                        mRefreshLayout.finishRefresh();
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            FinancialResponse mFinancialResponse = JSONObject.parseObject(string, FinancialResponse.class);
                            if (mFinancialResponse.data != null) {
                                mViewHolder.tv_1.setText(changTVsize(mFinancialResponse.data.total_fil));
                                mViewHolder.tv_2.setText(changTVsize(mFinancialResponse.data.total_income_fil));
                                mViewHolder.tv_3.setText(changTVsize(mFinancialResponse.data.last_day_income));
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }

    static class ViewHolder {

        @BindView(R.id.tv_1)
        TextView tv_1;

        @BindView(R.id.tv_2)
        TextView tv_2;

        @BindView(R.id.tv_3)
        TextView tv_3;

        @BindView(R.id.tv_transferout)
        TextView tv_transferout;

        @BindView(R.id.tv_mingxi)
        TextView tv_mingxi;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
