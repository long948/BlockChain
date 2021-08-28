package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.TxResponse;
import com.block.xjfkchain.data.UserEntity;
import com.block.xjfkchain.dialog.PayPwdDialog;
import com.lxj.xpopup.XPopup;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class TxActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.et_money)
    EditText mEtMoney;
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
    @BindView(R.id.et_remark)
    EditText mEtRemark;

    private PayPwdDialog payPwdDialog;

    private UserEntity userEntity;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tx;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("提现");
        userEntity = App.getApplication().getEntity();
        mTvBalance.setText(userEntity.usdt + " USDT");
        mRgSelect.check(R.id.rb_bank);

        Log.e("----","Bearer " + App.getApplication().getUserEntity().token);

    }


    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign)
    public void onMTvSignClicked() {
        double amount = 0;
        String sAmount = mEtMoney.getText().toString().trim();
        if (TextUtils.isEmpty(sAmount)) {
            ToastUtils.showShort("请输入提现金额");
            return;
        }
        try {
            amount = Double.valueOf(sAmount);
            Double usdt = Double.valueOf(userEntity.usdt);
            if (usdt < amount) {
                ToastUtils.showShort("提现金额不能大于余额");
                return;
            }
        } catch (Exception e) {
            ToastUtils.showShort("请输入正确的提现金额");
            return;
        }
        payPwdDialog = new PayPwdDialog(this);
        payPwdDialog.setConfirmClickListener(new PayPwdDialog.ConfirmClickListener() {
            @Override
            public void onConfirm(String pwd) {
                KeyboardUtils.hideSoftInput(TxActivity.this);
                payPwdDialog.dismiss();
                submitApply(pwd);
            }
        });
        new XPopup.Builder(this)
                .asCustom(payPwdDialog)
                .show();
        KeyboardUtils.hideSoftInput(this);
    }


    private void submitApply(String pwd) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("type", "usdt");
        maps.put("amount", mEtMoney.getText().toString().trim());
        maps.put("cash_pass", pwd);
        maps.put("remark", mEtRemark.getText().toString().trim());
        if (mRgSelect.getCheckedRadioButtonId() == R.id.rb_bank) {
            maps.put("account", "1");
        } else if (mRgSelect.getCheckedRadioButtonId() == R.id.rb_zfb) {
            maps.put("account", "2");
        } else {
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
                            TxResponse txResponse = JSONObject.parseObject(string, TxResponse.class);
                            if (txResponse != null && txResponse.data != null) {
                                ToastUtils.showShort("提交成功");
                                Intent intent = new Intent(TxActivity.this, TxDetailActivity.class);
                                intent.putExtra("entity", txResponse.data);
                                startActivity(intent);
                                finish();
                            }else{
                                ToastUtils.showShort(returnResponse.msg);
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }


}
