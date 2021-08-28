package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.NodeAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.NodeEntity;
import com.block.xjfkchain.data.NodeResponse;
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
 * @author muwenleiØ
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class MyNodeActivity extends BusinessBaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private long mMemberId;

    private long mCurParentId;

    private List<NodeEntity> mNodeEntities = new ArrayList<>();
    private NodeAdapter mNodeAdapter;

    private boolean isMobileSearch = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_node;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("我的节点");
        mMemberId = getIntent().getLongExtra("id", 0);
        mNodeAdapter = new NodeAdapter(mNodeEntities, false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mNodeAdapter);
        mCurParentId = App.getApplication().getUserEntity().user.union_uid;
        getData(null);
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
                            mNodeAdapter.notifyDataSetChanged();
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
