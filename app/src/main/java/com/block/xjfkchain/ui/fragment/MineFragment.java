package com.block.xjfkchain.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseFragment;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.UserEntity;
import com.block.xjfkchain.data.UserResponse;
import com.block.xjfkchain.ui.DownLoadActivity;
import com.block.xjfkchain.ui.EarningActivity;
import com.block.xjfkchain.ui.FilEarningActivity;
import com.block.xjfkchain.ui.InComeRecordsActivity;
import com.block.xjfkchain.ui.InviteCodeActivity;
import com.block.xjfkchain.ui.LoginActivity;
import com.block.xjfkchain.ui.MainHomeActivity;
import com.block.xjfkchain.ui.MyNodeActivity;
import com.block.xjfkchain.ui.MyPointActivity;
import com.block.xjfkchain.ui.OrderActivity;
import com.block.xjfkchain.ui.SettingActivity;
import com.block.xjfkchain.ui.TxListActivity;
import com.block.xjfkchain.ui.UserInfoActivity;
import com.bumptech.glide.Glide;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BusinessBaseFragment {
    @BindView(R.id.iv_icon)
    ImageView mIvIamge;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_level)
    TextView mTvLevel;
//    @BindView(R.id.tv_award)
//    TextView mTvAward;
    @BindView(R.id.tv_residue_hashrate)
    TextView mTvResidueHashrate;
    @BindView(R.id.tv_point_hashrate)
    TextView mTvPointHashrate;
    @BindView(R.id.tv_earnings)
    TextView mTvEarnings;
    @BindView(R.id.ll_order)
    LinearLayout mLlOrder;
    @BindView(R.id.ll_invite)
    LinearLayout mLlInvite;
//    @BindView(R.id.ll_mypoint)
//    LinearLayout mLlMypoint;
    @BindView(R.id.ll_invite_code)
    LinearLayout mLlInviteCode;
    @BindView(R.id.ll_earn)
    LinearLayout mLlEarn;
    @BindView(R.id.ll_tx)
    LinearLayout mLlTx;
    @BindView(R.id.ll_setting)
    LinearLayout mLlSetting;
    Unbinder unbinder;
    @BindView(R.id.rl_tip)
    RelativeLayout mRlTip;
    Unbinder unbinder1;


    private UserEntity userEntity;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
//        getData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @OnClick(R.id.ll_top)
    public void onMLlTopClicked() {
        if (userEntity == null) {
            return;
        }
        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        intent.putExtra("icon", userEntity.avatar);
        intent.putExtra("entity", userEntity);
        startActivity(intent);
    }


    private void getData() {
        HashMap<String, String> maps = new HashMap<>();
        showLoadding();
        LogUtils.e("Bearer " + App.getApplication().getUserEntity().token);
        EasyHttp.post("/api/profile")
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
                            userEntity = JSONObject.parseObject(string, UserResponse.class).data;
                            if (userEntity != null) {
                                App.getApplication().setUserEntity(userEntity);
                                showDataView(userEntity);
                            }
                        } else if (returnResponse.code >= 9000 || returnResponse.code == 401) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }

    private void showDataView(UserEntity userEntity) {
        Glide.with(this).load(userEntity.avatar).into(mIvIamge);
        mTvName.setText(TextUtils.isEmpty(userEntity.name) ? userEntity.mobile : userEntity.name);
        App.getApplication().setUserName(userEntity.name);
        mTvLevel.setText(userEntity.level);
//        mTvAward.setText("奖励算力:" + userEntity.reward_power + "T");
        mTvResidueHashrate.setText(TextUtils.isEmpty(userEntity.team_power) ? "0" : userEntity.team_power + "T");
        mTvPointHashrate.setText(TextUtils.isEmpty(userEntity.power) ? "0" : userEntity.power + "T");
        mTvEarnings.setText(TextUtils.isEmpty(userEntity.total_usdt) ? "0" : userEntity.total_usdt + "U");
    }

    @OnClick(R.id.ll_order)
    public void onMLlOrderClicked() {
        Intent intent = new Intent(getContext(), OrderActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_invite)
    public void onMLlInviteClicked() {
        Intent intent = new Intent(getContext(), MyPointActivity.class);
        startActivity(intent);

    }

//    @OnClick(R.id.ll_mypoint)
//    public void onMLlMypointClicked() {
//        Intent intent = new Intent(getContext(), MyNodeActivity.class);
//        startActivity(intent);
//    }

    @OnClick(R.id.ll_invite_code)
    public void onMLlInviteCodeClicked() {
        if (userEntity == null) {
            return;
        }
        Intent intent = new Intent(getContext(), InviteCodeActivity.class);
        intent.putExtra("qrcode", userEntity.qrcode_url);
        intent.putExtra("shareurl", userEntity.share_url);
        intent.putExtra("entity", userEntity);
        startActivity(intent);
    }

    @OnClick(R.id.ll_earn)
    public void onMLlEarnClicked() {
        if (userEntity == null) {
            return;
        }
//        Intent intent = new Intent(getActivity(), InComeRecordsActivity.class);
//        intent.putExtra("entity", userEntity);
//        startActivity(intent);
        Intent intent = new Intent(getActivity(), MainHomeActivity.class);
        startActivity(intent);
//        Intent intent = new Intent(getActivity(), EarningActivity.class);
//        intent.putExtra("entity", userEntity);
//        startActivity(intent);
    }

    @OnClick(R.id.ll_wallet)
    public void onMLlWalletClicked() {
        if (userEntity == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), FilEarningActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_tx)
    public void onMLlTxClicked() {
        Intent intent = new Intent(getActivity(), TxListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_setting)
    public void onMLlSettingClicked() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.tv_download)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), DownLoadActivity.class);
        startActivity(intent);
    }
}
