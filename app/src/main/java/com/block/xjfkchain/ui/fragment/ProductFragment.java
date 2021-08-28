package com.block.xjfkchain.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.ProductAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseFragment;
import com.block.xjfkchain.constant.Constants;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.ProductEntity;
import com.block.xjfkchain.data.ProductListResponse;
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

public class ProductFragment extends BusinessBaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private ProductAdapter mProductAdapter;
    private List<ProductEntity> mProductEntities = new ArrayList<>();

    private View mHeaderView;
    private ViewHolder mViewHolder;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_product;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mRefreshLayout.setEnableLoadMore(false);
        mProductAdapter = new ProductAdapter(mProductEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mProductAdapter);
        initHeader();
        mProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mProductEntities.get(position).stock.equals("0")){
                    return;
                }
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("id", mProductEntities.get(position).id);
                startActivity(intent);
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
        mHeaderView = View.inflate(getContext(), R.layout.item_product_header, null);
        mViewHolder = new ViewHolder(mHeaderView);
        mProductAdapter.addHeaderView(mHeaderView);
        long t = System.currentTimeMillis() / (1000 * 60 * 60 * 24);
        Glide.with(this)
                .load(Constants.BASE_URL + "/public_res/product_index_bg.jpg?t=" + t)
                .into(mViewHolder.mBanner);
    }


    private void getData(boolean showLoading) {
        HashMap<String, String> maps = new HashMap<>();
        if (showLoading) {
            showLoadding();
        }
        EasyHttp.post("/api/product/list")
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
                            ProductListResponse productListResponse = JSONObject.parseObject(string, ProductListResponse.class);
                            if (productListResponse.data != null && productListResponse.data.list != null) {
                                mProductEntities.addAll(productListResponse.data.list);
                            }
                            mProductAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }

    static class ViewHolder {
        @BindView(R.id.banner)
        ImageView mBanner;
        @BindView(R.id.tv_tip)
        TextView mTvTip;
        @BindView(R.id.ll_tip)
        LinearLayout mLlTip;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
