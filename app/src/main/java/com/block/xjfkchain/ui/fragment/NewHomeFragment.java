package com.block.xjfkchain.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseFragment;
import com.block.xjfkchain.data.BannerEntity;
import com.block.xjfkchain.data.BannerResponse;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.GongGaoEntity;
import com.block.xjfkchain.data.GongGaoResponse;
import com.block.xjfkchain.data.HomeDataResponse;
import com.block.xjfkchain.data.NoticeResponse;
import com.block.xjfkchain.data.UserEntity;
import com.block.xjfkchain.ui.CommonWebActivity;
import com.block.xjfkchain.ui.FilEarningActivity;
import com.block.xjfkchain.ui.FilWithDrawActivity;
import com.block.xjfkchain.ui.InComeRecordsActivity;
import com.block.xjfkchain.ui.IncomeDetailsActivity;
import com.block.xjfkchain.ui.NoticeActivity;
import com.block.xjfkchain.ui.OrderActivity;
import com.block.xjfkchain.ui.RechargeActivity;
import com.block.xjfkchain.ui.SearchActivity;
import com.block.xjfkchain.widget.autoscrollviewpager.BGABanner;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.JsonObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NewHomeFragment extends BusinessBaseFragment implements OnTabSelectListener {

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.iv_unread)
    ImageView mIvUnRead;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_2)
    TextView tv_2;
    @BindView(R.id.tv_3)
    TextView tv_3;
    @BindView(R.id.tv_4)
    TextView tv_4;
    @BindView(R.id.tv_5)
    TextView tv_5;
    @BindView(R.id.tv_6)
    TextView tv_6;
    @BindView(R.id.tv_7)
    TextView tv_7;
    @BindView(R.id.tv_0)
    TextView tv_0;
    @BindView(R.id.tv_0_2)
    TextView tv_0_2;
    @BindView(R.id.tv_0_1)
    TextView tv_0_1;
    @BindView(R.id.tv_8)
    TextView tv_8;
    @BindView(R.id.tv_symx)
    TextView tv_symx;
    @BindView(R.id.ll_1)
    LinearLayout ll_1;
    @BindView(R.id.ll_2)
    LinearLayout ll_2;
    @BindView(R.id.ll_3)
    LinearLayout ll_3;
    @BindView(R.id.ll_4)
    LinearLayout ll_4;
    @BindView(R.id.ll_5)
    RelativeLayout ll_5;
    @BindView(R.id.ll_6)
    RelativeLayout ll_6;
    @BindView(R.id.ll_7)
    RelativeLayout ll_7;
    @BindView(R.id.btn_tx)
    Button btn_tx;
    @BindView(R.id.btn_cz)
    Button btn_cz;
    private UserEntity userEntity;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_new_home;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        tv_1.setText(changTVsize("0.00"));//实时价格
        tv_0.setText(changTVsize("0.00"));//累计收益
        tv_3.setText(changTVsize("0.00"));// 累计存储
        tv_4.setText(changTVsize("0.00"));// 今日新增收益
        tv_5.setText(changTVsize("0.00"));//// 累计锁仓收益
        tv_6.setText(changTVsize("0.00"));// 累计解锁收益
        tv_7.setText(changTVsize("0.00"));    // 累计垫付质押
        tv_0_2.setText(changTVsize("0.00"));    // 可用余额(fil)
        tv_0_1.setText(changTVsize( App.getApplication().getEntity().usdt));    // 可用余额(usdt)


        btn_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilWithDrawActivity.class);
                intent.putExtra("entity",  App.getApplication().getEntity());
                startActivity(intent);
            }
        });

        btn_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("address", App.getApplication().getEntity().wallet);
                startActivity(intent);
            }
        });

        tv_symx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IncomeDetailsActivity.class);
                startActivity(intent);
            }
        });
//        ll_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userEntity = App.getApplication().getEntity();
//                if (userEntity == null) {
//                    return;
//                }
//                Intent intent = new Intent(getActivity(), OrderActivity.class);
//                intent.putExtra("entity", userEntity);
//                startActivity(intent);
//            }
//        });
//        ll_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userEntity = App.getApplication().getEntity();
//                if (userEntity == null) {
//                    return;
//                }
//                Intent intent = new Intent(getContext(), InComeRecordsActivity.class);
//                startActivity(intent);
//            }
//        });
//        ll_3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userEntity = App.getApplication().getEntity();
//                if (userEntity == null) {
//                    return;
//                }
//                Intent intent = new Intent(getContext(), InComeRecordsActivity.class);
//                startActivity(intent);
//            }
//        });
//        ll_4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userEntity = App.getApplication().getEntity();
//                if (userEntity == null) {
//                    return;
//                }
//                Intent intent = new Intent(getContext(), InComeRecordsActivity.class);
//                startActivity(intent);
//            }
//        });
//        ll_5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userEntity = App.getApplication().getEntity();
//                if (userEntity == null) {
//                    return;
//                }
//                Intent intent = new Intent(getContext(), InComeRecordsActivity.class);
//                startActivity(intent);
//            }
//        });
//        ll_6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userEntity = App.getApplication().getEntity();
//                if (userEntity == null) {
//                    return;
//                }
//                Intent intent = new Intent(getContext(), InComeRecordsActivity.class);
//                startActivity(intent);
//            }
//        });
//        ll_7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userEntity = App.getApplication().getEntity();
//                if (userEntity == null) {
//                    return;
//                }
//                Intent intent = new Intent(getContext(), InComeRecordsActivity.class);
//                startActivity(intent);
//            }
//        });


        mRefreshLayout.setEnableLoadMore(false);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getHomeData();
            }
        });
    }

//    void refreshDot(int type, int num) {
//        if (num > 0) {
//            mSlLayout.showDot(type - 1);
//        } else {
//            mSlLayout.hideMsg(type - 1);
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        getTipUnRead();
        getHomeData();
//        getGongGao();
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
                                tv_1.setText(changTVsize(mHomeDataResponse.data.new_price));//实时价格
                                tv_0.setText(changTVsize(mHomeDataResponse.data.total_income));//累计收益
                                tv_3.setText(changTVsize(mHomeDataResponse.data.total_power));// 累计存储
                                tv_4.setText(changTVsize(mHomeDataResponse.data.today_increment));// 今日新增收益
                                tv_5.setText(changTVsize(mHomeDataResponse.data.total_lockup_fil));//// 累计锁仓收益
                                tv_6.setText(changTVsize(mHomeDataResponse.data.total_unlock_fil));// 累计解锁收益
                                tv_7.setText(changTVsize(mHomeDataResponse.data.total_company_pledge_fil));    // 累计垫付质押
                                tv_0_2.setText(changTVsize(mHomeDataResponse.data.avail_withdraw_fil));
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                        mRefreshLayout.finishRefresh();
                    }
                });
    }

    private void getTipUnRead() {
        HashMap<String, String> maps = new HashMap<>();
        EasyHttp.post("/api/notice/list")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                        ToastUtils.showShort("获取信息错误");
                    }

                    @Override
                    public void onSuccess(String string) {
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            NoticeResponse noticeResponse = JSONObject.parseObject(string, NoticeResponse.class);
                            if (noticeResponse != null && noticeResponse.data != null) {
                                refreshUnRead(noticeResponse.data.unread_notice_num);
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }

    public void refreshUnRead(int num) {
        if (num > 0) {
            mIvUnRead.setVisibility(View.VISIBLE);
        } else {
            mIvUnRead.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.rl_tip)
    public void onMRlTipClicked() {
        Intent intent = new Intent(getActivity(), NoticeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTabSelect(int position) {
    }

    @Override
    public void onTabReselect(int position) {
    }

    public static SpannableString changTVsize(String value) {
        SpannableString spannableString = new SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(new RelativeSizeSpan(0.6f), value.indexOf("."), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
