package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.NoticeDetailResponse;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * TipDetailActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/2, muwenlei, Create file
 */
public class TipDetailActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.tv_sub)
    TextView mTvSub;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    private String id;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tip_detail;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("提醒详情");
        id = getIntent().getStringExtra("id");
        getData();
    }

    private void getData() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", id);
        showLoadding();
        EasyHttp.post("/api/notice/detail")
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
                            NoticeDetailResponse detailResponse = JSONObject.parseObject(string, NoticeDetailResponse.class);
                            if (detailResponse != null && detailResponse.data != null) {
                                mTvSub.setText(detailResponse.data.subject);
                                mTvTime.setText(detailResponse.data.created_at);
                                mTvContent.setText(detailResponse.data.content);
                            }
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
