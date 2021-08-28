package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.FeeEntity;
import com.block.xjfkchain.data.FeeResponse;
import com.block.xjfkchain.data.UserEntity;
import com.block.xjfkchain.dialog.PayPwdDialog;
import com.lxj.xpopup.XPopup;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * FilWithDrawActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/10/22, muwenlei, Create file
 */
public class FilWithDrawActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_num)
    EditText mEtNum;
    @BindView(R.id.tv_all)
    TextView mTvAll;
    @BindView(R.id.tv_fee)
    TextView mTvFee;
    @BindView(R.id.tv_total)
    TextView mTvTotal;
    @BindView(R.id.tv_into_num)
    TextView mTvIntoNum;
    @BindView(R.id.ll_next)
    LinearLayout mLlNext;
    @BindView(R.id.tv_ok)
    TextView mTvOk;
    @BindView(R.id.tv_send_code)
    TextView tv_send_code;
    @BindView(R.id.et_code)
    TextView et_code;
    @BindView(R.id.tv_bz)
    TextView tv_bz;
    @BindView(R.id.ll_bz)
    LinearLayout ll_bz;
    @BindView(R.id.ll_address)
    LinearLayout ll_address;
    @BindView(R.id.et_bz)
    EditText et_bz;
    @BindView(R.id.cb_fil)
    CheckBox cb_fil;
    @BindView(R.id.cb_usdt)
    CheckBox cb_usdt;
    @BindView(R.id.rb_bank)
    RadioButton rb_bank;
    @BindView(R.id.rb_zfb)
    RadioButton rb_zfb;
    @BindView(R.id.rb_qianbao)
    RadioButton rb_qianbao;

    private int step = 1;

    private PayPwdDialog payPwdDialog;
    private String filFee ="0";
    private String usdtFee ="0";
    private UserEntity userEntity;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fil_withdraw;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("提现");
        mTvRight.setText("扫一扫");
        mTvRight.setVisibility(View.VISIBLE);
        userEntity = (UserEntity) getIntent().getSerializableExtra("entity");
        mTvTotal.setText("(可用 "+userEntity.fil+" FIL)" );

        tv_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });

        cb_fil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_fil.setChecked(true);
                cb_usdt.setChecked(false);
                ll_bz.setVisibility(View.GONE);
                ll_address.setVisibility(View.VISIBLE);
                mTvTotal.setText("(可用 "+userEntity.fil+" FIL)" );
                mEtNum.setText("");
                step=1;
                mEtNum.setEnabled(true);
                mEtAddress.setEnabled(true);
                mTvAll.setEnabled(true);
            }
        });

        cb_usdt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cb_fil.setChecked(false);
                cb_usdt.setChecked(true);
                ll_bz.setVisibility(View.VISIBLE);
                ll_address.setVisibility(View.GONE);
                mTvTotal.setText("(可用 "+userEntity.usdt+" USDT)" );
                mEtNum.setText("");
                step=1;
                mEtNum.setEnabled(true);
                mEtAddress.setEnabled(true);
                mTvAll.setEnabled(true);
            }
        });

        mEtNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str="USDT";
                String fee =usdtFee;
                if (cb_fil.isChecked()){
                    str="FIL";
                    fee=filFee;
                }
                //手续费5%
                BigDecimal bigDecimalbuff = new BigDecimal("0.05");
                BigDecimal zero = new BigDecimal(BigInteger.ZERO);
                String mAmount = mEtNum.getText().toString();
                if (TextUtils.isEmpty(mAmount)){
                    mAmount="0";
                }
                BigDecimal mAmountBuff = new BigDecimal(mAmount);
                if (mAmountBuff.compareTo(BigDecimal.ZERO)!=1){
                    mTvFee.setText("手续费 "+0+" "+str);
                    mTvIntoNum.setText("到账数量 "+0+" "+str);
                }else {
                    BigDecimal bigDecimalmAmount = new BigDecimal(mAmount);
//                    String fee = bigDecimalmAmount.multiply(bigDecimalbuff).setScale(2, BigDecimal.ROUND_HALF_DOWN).toPlainString();

                    BigDecimal bigDecimalfee = new BigDecimal(fee);
                    String intoNum=bigDecimalmAmount.subtract(bigDecimalfee).toPlainString();

                    mTvFee.setText("手续费 "+fee+" "+str);
                    mTvIntoNum.setText("到账数量 "+intoNum+" "+str);
                }
            }
        });
        mEtNum.setText("");

        mCountDownTimer = new CountDownTimer(60*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (tv_send_code!=null){
                    tv_send_code.setEnabled(false);
                    tv_send_code.setText(millisUntilFinished/1000+"秒后重新获取");
                }
            }

            @Override
            public void onFinish() {
                if (tv_send_code!=null){
                    tv_send_code.setEnabled(true);
                    tv_send_code.setText("发送");
                }
            }
        };
        getFee();
    }
    CountDownTimer mCountDownTimer ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimer.onFinish();
        mCountDownTimer.cancel();
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void onMTvRightClicked() {
        PermissionUtils.permission(PermissionConstants.CAMERA)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(FilWithDrawActivity.this, ScanActivity.class);
                        startActivityForResult(intent, 500);
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort("请开启拍照权限");
                    }
                })
                .request();
    }

    @OnClick(R.id.tv_all)
    public void onMTvAllClicked() {
        if (cb_fil.isChecked()){
            mEtNum.setText(userEntity.fil + "");
        }else {
            mEtNum.setText(userEntity.usdt + "");
        }
    }

    @OnClick(R.id.tv_ok)
    public void onMTvOkClicked() {
        if (cb_fil.isChecked()){
            if (TextUtils.isEmpty(mTvAll.getText().toString())) {
                ToastUtils.showShort("请输入提现地址");
                return;
            }
        }
        if (TextUtils.isEmpty(mEtNum.getText().toString())) {
            ToastUtils.showShort("请输入提现数量");
            return;
        }
        if (TextUtils.isEmpty(et_code.getText().toString())) {
            ToastUtils.showShort("请输入短信验证码");
            return;
        }

        payPwdDialog = new PayPwdDialog(this);
        payPwdDialog.setConfirmClickListener(new PayPwdDialog.ConfirmClickListener() {
            @Override
            public void onConfirm(String pwd) {
                submit(pwd);
            }
        });
        new XPopup.Builder(this)
                .asCustom(payPwdDialog)
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == 500 && resultCode == 1000) {
            String result = data.getStringExtra("result");
            mEtAddress.setText(result);
        }
    }

    private void sendCode() {
        HashMap<String, String> maps = new HashMap<>();
        showLoadding();
        LogUtils.e("Bearer " + App.getApplication().getUserEntity().token);
        EasyHttp.post("/api/cashout/sendCode")
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
                            mCountDownTimer.start();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }
    private void getFee() {
        HashMap<String, String> maps = new HashMap<>();
        showLoadding();
        LogUtils.e("Bearer " + App.getApplication().getUserEntity().token);
        EasyHttp.get("/api/cashoutFee")
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
                             filFee = jsonObject.getJSONObject("data").getString("fil");
                             usdtFee = jsonObject.getJSONObject("data").getString("usdt");
                        } else {
                            dismissLoadding();
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }

    private void setNextView() {
        //手续费5%
        BigDecimal bigDecimalbuff = new BigDecimal("0.05");
        String mAmount = mEtNum.getText().toString();
        BigDecimal bigDecimalmAmount = new BigDecimal(mAmount);
        String fee = bigDecimalmAmount.multiply(bigDecimalbuff).setScale(2, BigDecimal.ROUND_HALF_DOWN).toPlainString();

        BigDecimal bigDecimalfee = new BigDecimal(fee);
        String intoNum=bigDecimalmAmount.subtract(bigDecimalfee).toPlainString();

        mTvFee.setText(fee);
        mTvIntoNum.setText(intoNum);
        mLlNext.setVisibility(View.VISIBLE);
        mTvOk.setText("确认提现");
        mEtNum.setEnabled(false);
        mEtAddress.setEnabled(false);
        mTvAll.setEnabled(false);
        step = 2;
    }

    private void submit(String pwd) {
        HashMap<String, String> maps = new HashMap<>();
        if (cb_fil.isChecked()){
            maps.put("type", "fil");
        }else {
            maps.put("type", "usdt");
        }
        maps.put("fil_addr", mEtAddress.getText().toString());
        maps.put("remark", et_bz.getText().toString());
        maps.put("amount", mEtNum.getText().toString());
        maps.put("sms_code", et_code.getText().toString());
        maps.put("cash_pass", pwd);
       if (rb_bank.isChecked()){
           maps.put("account", "1");
       }else if (rb_zfb.isChecked()){
           maps.put("account", "2");
       }else {
           maps.put("account", "3");
       }
        showLoadding();
        EasyHttp.post("/api/cashout/submit_apply")
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
                            if (payPwdDialog != null) {
                                payPwdDialog.dismiss();
                            }
                            finish();
                            ToastUtils.showShort("提交成功");
                        } else {
                            dismissLoadding();
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });

    }
}
