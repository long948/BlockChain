package com.block.xjfkchain.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.math.BigDecimal;
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
public class FinancialTransferOutActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.ll_yue)
    LinearLayout ll_yue;
    @BindView(R.id.ll_fil)
    LinearLayout ll_fil;
    @BindView(R.id.ll_fil_layout)
    LinearLayout ll_fil_layout;
    @BindView(R.id.ll_yue_layout)
    LinearLayout ll_yue_layout;
    @BindView(R.id.tv_fil)
    TextView tv_fil;
    @BindView(R.id.tv_yue)
    TextView tv_yue;
    @BindView(R.id.tv_yue_tip)
    TextView tv_yue_tip;
    @BindView(R.id.tv_fee)
    TextView tv_fee;
    @BindView(R.id.tv_all_fil)
    TextView tv_all_fil;
    @BindView(R.id.tv_all_yue)
    TextView tv_all_yue;
    @BindView(R.id.tv_paste)
    TextView tv_paste;
    @BindView(R.id.img_fil)
    ImageView img_fil;
    @BindView(R.id.img_yue)
    ImageView img_yue;
    @BindView(R.id.et_yue_amount)
    EditText et_yue_amount;
    @BindView(R.id.et_amount_fil)
    EditText et_amount_fil;
    @BindView(R.id.et_fil_address)
    EditText et_fil_address;
    @BindView(R.id.iv_ok_fil)
    ImageView iv_ok_fil;
    @BindView(R.id.iv_ok)
    ImageView iv_ok;
    String fil_product_id = "";
    String buy_record_id = "";
    String avail_fil = "0";
    String fil_cashout_fee = "0";
    int financial_withdraw_percent = 0;
    //剪切板管理工具类
    private ClipboardManager mClipboardManager;
    //剪切板Data对象
    private ClipData mClipData;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_financial_transfer_out;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        getData();
        et_amount_fil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = et_amount_fil.getText().toString();
                if (TextUtils.isEmpty(s1)) {
                    s1 = "0";
                }
                BigDecimal bigDecimal1 = new BigDecimal(financial_withdraw_percent);
                BigDecimal bigDecimal2 = new BigDecimal("100");
                BigDecimal bigDecimal3 = bigDecimal1.divide(bigDecimal2, 6, BigDecimal.ROUND_HALF_DOWN);
                BigDecimal bigDecimal4 = new BigDecimal(s1);
                String fee =fil_cashout_fee;
                BigDecimal bigDecimal5 = new BigDecimal(fee);
                String shiji = bigDecimal4.subtract(bigDecimal5).setScale(3, BigDecimal.ROUND_HALF_DOWN).toPlainString();
                tv_fee.setText("手续费（" + fee + "FIL，实际到账" + shiji + "FIL");
            }
        });

        ll_yue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_yue_layout.setVisibility(View.VISIBLE);
                ll_fil_layout.setVisibility(View.GONE);
                tv_yue.setTextColor(Color.parseColor("#FFFFFF"));
                tv_fil.setTextColor(Color.parseColor("#8390C3"));
                img_yue.setVisibility(View.VISIBLE);
                img_fil.setVisibility(View.INVISIBLE);
            }
        });
        ll_fil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_yue_layout.setVisibility(View.GONE);
                ll_fil_layout.setVisibility(View.VISIBLE);
                tv_yue.setTextColor(Color.parseColor("#8390C3"));
                tv_fil.setTextColor(Color.parseColor("#FFFFFF"));
                img_yue.setVisibility(View.INVISIBLE);
                img_fil.setVisibility(View.VISIBLE);
            }
        });
        tv_paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              doPaste();
            }
        });
        tv_all_yue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_yue_amount.setText(avail_fil);
            }
        });
        tv_all_fil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_amount_fil.setText(avail_fil);
            }
        });
        iv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_yue_amount.getText().toString();
                boolean empty = TextUtils.isEmpty(s);
                if (empty) {
                    return;
                }
                HashMap<String, String> maps = new HashMap<>();
                maps.put("fil_product_id", fil_product_id);
                maps.put("buy_record_id", buy_record_id);
                maps.put("amount", s);
                maps.put("target", "1");
                showLoadding();
                EasyHttp.post("/api/financial/cashout")
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
                                    ToastUtils.showShort("转出成功");
                                    finish();
                                } else {
                                    ToastUtils.showShort(returnResponse.msg);
                                }
                            }
                        });

            }
        });
        iv_ok_fil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_amount_fil.getText().toString();
                boolean empty = TextUtils.isEmpty(s);
                if (empty) {
                    return;
                }
                String s1 = et_fil_address.getText().toString();
                boolean empty1 = TextUtils.isEmpty(s1);
                if (empty1) {
                    return;
                }
                HashMap<String, String> maps = new HashMap<>();
                maps.put("fil_product_id", fil_product_id);
                maps.put("buy_record_id", buy_record_id);
                maps.put("amount", s);
                maps.put("target", "2");
                maps.put("fil_addr", s1);
                showLoadding();
                EasyHttp.post("/api/financial/cashout")
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
                                    ToastUtils.showShort("转出成功");
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
        buy_record_id = getIntent().getStringExtra("buy_record_id");
        fil_product_id = getIntent().getStringExtra("fil_product_id");
        HashMap<String, String> maps = new HashMap<>();
        maps.put("buy_record_id", buy_record_id);
        showLoadding();
        EasyHttp.post("/api/financial/cashoutdetail")
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
                            JSONObject jsonObject = JSONObject.parseObject(string);
                            JSONObject data = jsonObject.getJSONObject("data");
                            avail_fil = data.getString("avail_fil");
                            fil_cashout_fee = data.getString("fil_cashout_fee");
                            String unavail_fil = data.getString("unavail_fil");
                            financial_withdraw_percent = Integer.parseInt(data.getString("financial_withdraw_percent"));
                            et_amount_fil.setHint("可转出" + avail_fil + "FIL");
                            et_yue_amount.setHint("可转出" + avail_fil + "FIL");
                            tv_yue_tip.setText("有" + unavail_fil + "FIL不可转出");
                            tv_fee.setText("手续费 0.00FIL，实际到账0.00FIL");
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }


    private void doPaste() {
        mClipData = mClipboardManager.getPrimaryClip();
        ClipData.Item item = mClipData.getItemAt(0);
        String text = item.getText().toString();
        et_fil_address.setText(text);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }


}
