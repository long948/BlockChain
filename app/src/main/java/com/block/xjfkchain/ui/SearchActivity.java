package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.SearchAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.NewsEntity;
import com.block.xjfkchain.data.NewsResponse;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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
 * SearchActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/7/26, muwenlei, Create file
 */
public class SearchActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_cancel)
    ImageView mIvCancel;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.ll_search)
    RelativeLayout mLlSearch;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private List<NewsEntity> mNewsEntities = new ArrayList<>();

    private SearchAdapter mSearchAdapter;

//    private int mPage = 1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mSearchAdapter = new SearchAdapter(mNewsEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSearchAdapter);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        mSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mNewsEntities.get(position).is_read = 1;
                mSearchAdapter.notifyItemChanged(position);
                read(mNewsEntities.get(position).id);
                Intent intent = new Intent(SearchActivity.this, CommonWebActivity.class);
                intent.putExtra("url", mNewsEntities.get(position).url);
                intent.putExtra("title", "行业行情");
                startActivity(intent);
            }
        });
        mEtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getData(true);
                    KeyboardUtils.hideSoftInput(SearchActivity.this);
//                    Toast.makeText(MainActivity.this, "搜索！", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }


    @OnClick(R.id.iv_cancel)
    public void onMIvCancelClicked() {
        mNewsEntities.clear();
        mEtContent.setText("");
        KeyboardUtils.hideSoftInput(this);
        mSearchAdapter.notifyDataSetChanged();

    }

    @OnClick(R.id.tv_cancel)
    public void onMTvCancelClicked() {
        finish();
    }

    private void getData(boolean showLoading) {
        HashMap<String, String> maps = new HashMap<>();
        if (showLoading) {
            showLoadding();
        }
        maps.put("page", 1 + "");
        maps.put("search_key", mEtContent.getText().toString().trim());
        EasyHttp.post("/api/article/list")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        if (mRecyclerView == null) {
                            return;
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
                        dismissLoadding();
                        mNewsEntities.clear();
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            NewsResponse newsResponse = JSONObject.parseObject(string, NewsResponse.class);
                            if (newsResponse.data != null && newsResponse.data != null) {
                                mNewsEntities.addAll(newsResponse.data.list);
                            }
                            mSearchAdapter.notifyDataSetChanged();
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
}
