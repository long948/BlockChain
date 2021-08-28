package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.constant.Constants;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.FinancialBuffEntity;
import com.block.xjfkchain.data.FinancialDeailResponse;
import com.block.xjfkchain.data.FinancialResponse;
import com.block.xjfkchain.data.ProductResponse;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * ProductDetailActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class FinancialDetailActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.iv_ok)
    ImageView iv_ok;
    @BindView(R.id.cb_protocol)
    CheckBox mCbProtocol;
    @BindView(R.id.et_amount)
    EditText et_amount;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_expected_income_time)
    TextView tv_expected_income_time;
    @BindView(R.id.tv_5)
    TextView tv_5;
    @BindView(R.id.tv_6)
    TextView tv_6;
    @BindView(R.id.tv_7)
    TextView tv_7;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_2)
    TextView tv_2;
    @BindView(R.id.tv_3)
    TextView tv_3;
    @BindView(R.id.tv_4)
    TextView tv_4;
    private long mId = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_financial_detail;
    }
    @OnClick(R.id.tv_protocol)
    public void onMTvProtocolClicked() {
        Intent intent = new Intent(this, CommonWebActivity.class);
        intent.putExtra("url", Constants.BASE_URL + "/api/buy_notice?id=" + App.getApplication().getUserEntity().user.union_uid + "&product_id=" + mId);
        intent.putExtra("title", "购买协议");
       startActivity(intent);
    }
    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mId = getIntent().getLongExtra("id", 0);
        getData();
//        PenConfig.THEME_COLOR = Color.parseColor("#2E315D");
//        PermissionUtils.permission(PermissionConstants.STORAGE)
//                .request();


        iv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCbProtocol.isChecked()) {
                    ToastUtils.showShort("请阅读相关内容并同意");
                    return;
                }
                String amount = et_amount.getText().toString();
                if (TextUtils.isEmpty(amount)) {
                    ToastUtils.showShort("请输入买入金额");
                    return;
                }
                HashMap<String, String> maps = new HashMap<>();
                maps.put("fil_product_id", mId + "");
                maps.put("amount", amount);
                showLoadding();
                EasyHttp.post("/api/financial/buy")
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
                                    ToastUtils.showShort("买入成功！");
                                    finish();
                                } else {
                                    ToastUtils.showShort(returnResponse.msg);
                                }
                            }
                        });
            }
        });
    }


    private void getData() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("fil_product_id", mId + "");
        showLoadding();
        EasyHttp.post("/api/financial/detail")
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
                            FinancialBuffEntity data = JSONObject.parseObject(string, FinancialDeailResponse.class).data;
                            if (data != null) {
                                tv_5.setText(data.duration + "（天）");
                                tv_6.setText(data.balance_fil);
                                tv_4.setText(data.rate);
                                tv_7.setText(data.name);
                                tv_title.setText(data.name);
                                tv_expected_income_time.setText("预计从"+data.expected_income_time+"开始产生收益，成交后不可撤退，到期前不可取出。");
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });


        HashMap<String, String> map_1 = new HashMap<>();
        map_1.put("fil_product_id", mId + "");
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
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            FinancialResponse mFinancialResponse = JSONObject.parseObject(string, FinancialResponse.class);
                            if (mFinancialResponse.data != null) {
                                tv_1.setText(changTVsize(mFinancialResponse.data.total_fil));
                                tv_2.setText(changTVsize(mFinancialResponse.data.last_day_income));
                                tv_3.setText(changTVsize(mFinancialResponse.data.total_income_fil));
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }

    private void showDataView() {
//        if (mProductEntity.content != null) {
//
//        }
//        mWebDetail.loadUrl(Constants.BASE_URL + "/api/product/detail/" + mId);
//        Glide.with(this)
//                .load(mProductEntity.cover).into(mIvImage);
//        mTvPrice.setText(mProductEntity.price);
//        mTvName.setText(mProductEntity.name);
////        mTvType.setText(mProductEntity.recommend);
//        mTvSelectNum.setText("数量*1 总价");
//        mTvKf.setText(mProductEntity.wechat_client);
//        mTvMoney.setText(mProductEntity.price + "USDT");
    }

    private void refreshpriceView() {
//        mTvNum.setText(mNum + "");
//        mTvSelectNum.setText("数量*" + mNum + " 总价");
//        mTvMoney.setText(new BigDecimal(mProductEntity.price).multiply(new BigDecimal(mNum)).toString() + "USDT");
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }


//    private void buy() {
//        HashMap<String, String> maps = new HashMap<>();
//        maps.put("product_id", mId + "");
//        maps.put("num", mNum + "");
//        maps.put("sign_url",mImageUrl);
//        showLoadding();
//        EasyHttp.post("/api/checkout/create_order")
//                .params(maps)
//                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
//                .execute(new SimpleCallBack<String>() {
//                    @Override
//                    public void onError(ApiException e) {
//                        dismissLoadding();
//                        e.printStackTrace();
//                        ToastUtils.showShort("获取信息错误");
//                    }
//
//                    @Override
//                    public void onSuccess(String string) {
//                        dismissLoadding();
//                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
//                        if (returnResponse.isSuc()) {
//                            OrderEntity orderEntity = JSONObject.parseObject(string, OrderDetailResponse.class).data;
//                            if (orderEntity != null) {
//                                confirmOrderDialog.dismiss();
//                                Intent intent = new Intent(FinancialDetailActivity.this, PayActivity.class);
//                                intent.putExtra("entity", orderEntity);
//                                startActivity(intent);
//                            }
//                        } else {
//                            ToastUtils.showShort(returnResponse.msg);
//                        }
//                    }
//                });
//    }

    public static SpannableString changTVsize(String value) {
        SpannableString spannableString = new SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(new RelativeSizeSpan(0.6f), value.indexOf("."), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
