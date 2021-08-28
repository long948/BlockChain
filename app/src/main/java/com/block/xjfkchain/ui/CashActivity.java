package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.UserEntity;
import com.block.xjfkchain.dialog.PayPwdDialog;
import com.lxj.xpopup.XPopup;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class CashActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.et_bank_no)
    EditText mEtBankNo;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_bank_address)
    EditText mEtBankAddress;
    @BindView(R.id.et_zfb)
    EditText mEtZfb;
    @BindView(R.id.et_qianbao)
    EditText mEtQianbao;
    @BindView(R.id.rb_bank)
    RadioButton mRbBank;
    @BindView(R.id.rb_zfb)
    RadioButton mRbZfb;
    @BindView(R.id.rb_qianbao)
    RadioButton mRbQianbao;
    @BindView(R.id.rg_select)
    RadioGroup mRgSelect;
    @BindView(R.id.tv_sign)
    TextView mTvSign;
    @BindView(R.id.ll_brank)
    LinearLayout mLlBrank;
    @BindView(R.id.ll_zfb)
    LinearLayout mLlZfb;
    @BindView(R.id.ll_qianbao)
    LinearLayout mLlQianbao;

    private UserEntity userEntity;

    private PayPwdDialog payPwdDialog;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_cash;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        userEntity = App.getApplication().getEntity();
        mTvTitle.setText("提现账户");
        mRgSelect.check(R.id.rb_bank);
        mRgSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = mRgSelect.getCheckedRadioButtonId();
                if (id == mRbBank.getId()) {
                    mLlBrank.setVisibility(View.VISIBLE);
                    mLlZfb.setVisibility(View.GONE);
                    mLlQianbao.setVisibility(View.GONE);
                } else if (id == mRbZfb.getId()) {
                    mLlBrank.setVisibility(View.GONE);
                    mLlZfb.setVisibility(View.VISIBLE);
                    mLlQianbao.setVisibility(View.GONE);
                } else {
                    mLlBrank.setVisibility(View.GONE);
                    mLlZfb.setVisibility(View.GONE);
                    mLlQianbao.setVisibility(View.VISIBLE);
                }

            }
        });
        mEtBankNo.setText(userEntity.bankcard_no);
        mEtName.setText(userEntity.bankcard_name);
        mEtBankAddress.setText(userEntity.bank_name);
        mEtZfb.setText(userEntity.alipay);
        mEtQianbao.setText(userEntity.wallet_usdt);
        if (TextUtils.isEmpty(userEntity.default_account) || "1".equals(userEntity.default_account)) {
            mRgSelect.check(R.id.rb_bank);
        } else if ("2".equals(userEntity.default_account)) {
            mRgSelect.check(R.id.rb_zfb);
        } else {
            mRgSelect.check(R.id.rb_qianbao);
        }

    }


    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign)
    public void onMTvSignClicked() {
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

    private void submit(String pwd) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("bankcard_no", mEtBankNo.getText().toString().trim());
        maps.put("bankcard_name", mEtName.getText().toString().trim());
        maps.put("bank_name", mEtBankAddress.getText().toString().trim());
        maps.put("alipay", mEtZfb.getText().toString().trim());
        maps.put("wallet_usdt", mEtQianbao.getText().toString().trim());
        maps.put("cash_pass", pwd);
        if (mRgSelect.getCheckedRadioButtonId() == R.id.rb_bank) {
            maps.put("default_account", "1");
        } else if (mRgSelect.getCheckedRadioButtonId() == R.id.rb_zfb) {
            maps.put("default_account", "2");
        } else {
            maps.put("default_account", "3");
        }
        showLoadding();
        EasyHttp.post("/api/update_account")
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
                            Intent intent = new Intent(CashActivity.this, MainActivity.class);
                            startActivity(intent);
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
