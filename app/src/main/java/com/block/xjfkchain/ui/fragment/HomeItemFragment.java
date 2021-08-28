package com.block.xjfkchain.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.HomeAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseFragment;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.NewsEntity;
import com.block.xjfkchain.data.NewsResponse;
import com.block.xjfkchain.ui.CommonWebActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2020, Relx
 * HomeItemFragment
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/7/26, muwenlei, Create file
 */
public class HomeItemFragment extends BusinessBaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private HomeAdapter mHomeAdapter;

    private List<NewsEntity> mNewsEntities = new ArrayList<>();

    private int mPageNo = 1;

    private RefreshListener mRefreshListener;

    private int mType;

    private UnreadNumListener mUnreadNumListener;

    public static HomeItemFragment getInstance(int type) {
        HomeItemFragment homeItemFragment = new HomeItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        homeItemFragment.setArguments(bundle);
        return homeItemFragment;
    }

    public void setRefreshListener(RefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }

    public void refreshData() {
        mPageNo = 1;
        getData(false);
    }

    public void loadMore() {
        mPageNo++;
        getData(false);
    }

    public void setUnreadNumListener(UnreadNumListener mUnreadNumListener) {
        this.mUnreadNumListener = mUnreadNumListener;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home_item_fragment;
    }


    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mType = savedInstanceState.getInt("type", 0);
        mHomeAdapter = new HomeAdapter(mNewsEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mHomeAdapter);
        mHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mNewsEntities.get(position).is_read = 1;
                mHomeAdapter.notifyItemChanged(position);
                read(mNewsEntities.get(position).id);

                Intent intent = new Intent(getContext(), CommonWebActivity.class);
                intent.putExtra("url", mNewsEntities.get(position).url);
                intent.putExtra("title", "行业行情");
                startActivity(intent);
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
        maps.put("category_id", mType + "");
        EasyHttp.post("/api/article/list")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        if (mRecyclerView == null) {
                            return;
                        }
                        if (mRefreshListener != null) {
                            mRefreshListener.onFinish();
                        }
                        dismissLoadding();
                        e.printStackTrace();
                        ToastUtils.showShort("获取信息错误");
                    }

                    @Override
                    public void onSuccess(String string) {
                        if (mRecyclerView == null) {
                            return;
                        }
                        if (mRefreshListener != null) {
                            mRefreshListener.onFinish();
                        }
                        dismissLoadding();
                        if (mPageNo == 1) {
                            mNewsEntities.clear();
                        }
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            NewsResponse newsResponse = JSONObject.parseObject(string, NewsResponse.class);
                            if (newsResponse.data != null && newsResponse.data != null) {
                                if (mUnreadNumListener != null) {
                                    mUnreadNumListener.refreshNum(mType, newsResponse.data.unread_num);
                                }
                                mNewsEntities.addAll(newsResponse.data.list);
                            }
                            mHomeAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }

    private void read(long id) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", id + "");
        EasyHttp.post("/api/article/detail")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(String string) {
                    }
                });
    }


    public interface RefreshListener {
        void onFinish();
    }

    public interface UnreadNumListener {
        void refreshNum(int type, int num);
    }

}
