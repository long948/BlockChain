package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.NodeAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.NodeEntity;
import com.block.xjfkchain.data.NodeListResponse;
import com.block.xjfkchain.data.NodeResponse;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
public class NodeActivity extends BusinessBaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_buy)
    TextView mTvBuy;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_search)
    EditText mEtSearch;

    private long mMemberId;

    private long mCurParentId;

    private List<NodeEntity> mNodeEntities = new ArrayList<>();
    private NodeAdapter mNodeAdapter;

    private boolean isMobileSearch = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_node;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("搜索节点");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("回到根节点");
        mMemberId = getIntent().getLongExtra("id", 0);
        mNodeAdapter = new NodeAdapter(mNodeEntities, true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mNodeAdapter);
        mCurParentId = App.getApplication().getUserEntity().user.union_uid;
        mTvName.setText(App.getApplication().getUserName());
        mNodeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NodeEntity nodeEntity = mNodeEntities.get(position);
                mCurParentId = nodeEntity.id;
                mTvName.setText(nodeEntity.name);
                mEtSearch.setText("");
                mNodeEntities.clear();
                mNodeAdapter.notifyDataSetChanged();
                getData(null);
            }
        });

        mNodeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                NodeEntity nodeEntity = mNodeEntities.get(position);
                mCurParentId = nodeEntity.id;
                mTvName.setText(nodeEntity.name);
                mEtSearch.setText("");
                mNodeEntities.clear();
                mNodeAdapter.notifyDataSetChanged();
                getData(null);
            }
        });

        getData(null);

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(mEtSearch.getText().toString().trim())) {
                        ToastUtils.showShort("请输入要搜索的手机号");
                        return false;
                    }
                    mNodeEntities.clear();
                    mNodeAdapter.notifyDataSetChanged();
                    getData(mEtSearch.getText().toString().trim());
                }
                return false;
            }
        });


    }

    private void bindPoint(long id) {
        HashMap<String, String> maps = new HashMap<>();
        showLoadding();
        maps.put("member_id", mMemberId + "");
        maps.put("node_id", id + "");
        showLoadding();
        EasyHttp.post("/api/node/mount")
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
                            ToastUtils.showShort("挂载成功");
                            Intent intent = new Intent(NodeActivity.this, MyPointActivity.class);
                            startActivity(intent);
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }


    private void getData(String mobile) {
        HashMap<String, String> maps = new HashMap<>();
        showLoadding();
        maps.put("pre_page", "all");
        if (TextUtils.isEmpty(mobile)) {
            maps.put("node_id", mCurParentId + "");
            isMobileSearch = false;
        } else {
            maps.put("mobile", mobile);
            isMobileSearch = true;
        }
        EasyHttp.post("/api/node/search")
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
                            NodeResponse nodeListResponse = JSONObject.parseObject(string, NodeResponse.class);
                            if (nodeListResponse.data != null && nodeListResponse.data != null) {
                                mNodeEntities.addAll(nodeListResponse.data.child);
                            }
//                            if (mNodeEntities.size() < 3) {
                            mTvBuy.setVisibility(View.VISIBLE);
//                            } else {
//                                mTvBuy.setVisibility(View.INVISIBLE);
//                            }
                            mCurParentId = nodeListResponse.data.id;
                            mTvName.setText(nodeListResponse.data.name);
                            mNodeAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }

    private void getBrother() {
        HashMap<String, String> maps = new HashMap<>();
        showLoadding();
        maps.put("id", mCurParentId + "");
        EasyHttp.post("/api/node/brother")
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
                            NodeListResponse nodeListResponse = JSONObject.parseObject(string, NodeListResponse.class);
                            if (nodeListResponse.data != null && nodeListResponse.data != null && nodeListResponse.data.size() > 0) {
                                mNodeEntities.addAll(nodeListResponse.data);
                                mCurParentId = mNodeEntities.get(0).node_id;
                                mTvName.setText(mNodeEntities.get(0).node_name);
                            }
                            if (mNodeEntities.size() < 3) {
                                mTvBuy.setVisibility(View.VISIBLE);
                            } else {
                                mTvBuy.setVisibility(View.INVISIBLE);
                            }
                            mNodeAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }


    @OnClick(R.id.tv_right)
    public void onViewRightClicked() {
        mCurParentId = App.getApplication().getUserEntity().user.union_uid;
        mTvName.setText(App.getApplication().getUserName());
        mEtSearch.setText("");
        mNodeEntities.clear();
        mNodeAdapter.notifyDataSetChanged();
        getData(null);
    }

    @OnClick(R.id.tv_buy)
    public void onViewBuyClicked() {
        bindPoint(mCurParentId);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        if (isMobileSearch) {
            mCurParentId = App.getApplication().getUserEntity().user.union_uid;
            mTvName.setText(App.getApplication().getUserName());
            mEtSearch.setText("");
            mNodeEntities.clear();
            mNodeAdapter.notifyDataSetChanged();
            getData(null);
        } else if (mCurParentId == 0 || App.getApplication().getUserEntity().user.union_uid == mCurParentId) {
            finish();
        } else {
            mEtSearch.setText("");
            mNodeEntities.clear();
            mNodeAdapter.notifyDataSetChanged();
            getBrother();
        }

    }

    @Override
    public void onBackPressed() {
        if (KeyboardUtils.isSoftInputVisible(this)) {
            super.onBackPressed();
            return;
        }
        if (isMobileSearch) {
            mCurParentId = App.getApplication().getUserEntity().user.union_uid;
            mTvName.setText(App.getApplication().getUserName());
            mEtSearch.setText("");
            mNodeEntities.clear();
            mNodeAdapter.notifyDataSetChanged();
            getData(null);
        }
        if (mCurParentId == 0 || App.getApplication().getUserEntity().user.union_uid == mCurParentId) {
            super.onBackPressed();
        } else {
            mEtSearch.setText("");
            mNodeEntities.clear();
            mNodeAdapter.notifyDataSetChanged();
            getBrother();
        }
    }
}
