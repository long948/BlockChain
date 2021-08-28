package com.block.xjfkchain.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Copyright (C) 2020, Relx
 * UpdatePwdByCodeActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/2, muwenlei, Create file
 */
public class UpdatePwdByCodeActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.tv_getcode)
    TextView mTvGetcode;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.iv_register)
    ImageView mIvRegister;

    private int mType;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload_pwd_code;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mType = getIntent().getIntExtra("type", 1);
        mType = getIntent().getIntExtra("type", 1);
        if (mType == 1) {
            mTvTitle.setText("登录密码修改");
            mTvTip.setText("请获取短信验证码，并设置新的登录密码");
        } else {
            mTvTitle.setText("资金密码修改");
            mTvTip.setText("请获取短信验证码，并设置新的资金密码");
        }
        mEtName.setText(App.getApplication().getEntity().mobile);
        mEtName.setEnabled(false);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_getcode)
    public void onMTvGetcodeClicked() {
        getCode();
    }

    @OnClick(R.id.iv_register)
    public void onMIvRegisterClicked() {
        updatePwd();
    }

    private void getCode() {
        if (TextUtils.isEmpty(mEtName.getText().toString().trim())) {
            ToastUtils.showShort("手机号不能为空");
            return;
        }
        HashMap<String, String> maps = new HashMap<>();
        maps.put("mobile", mEtName.getText().toString().trim());
        maps.put("role", "member");
        showLoadding();
        EasyHttp.post("/api/requestcode")
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
                            startCountdown();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }

    private void startCountdown() {
        final long codeTimes = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(codeTimes - 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return codeTimes - aLong;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (mTvGetcode != null) {
                            mTvGetcode.setEnabled(false);
                        }

                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long value) {
                        if (mTvGetcode != null) {
                            mTvGetcode.setText(value + "秒");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (mTvGetcode != null) {
                            mTvGetcode.setEnabled(true);
                            mTvGetcode.setText("获取验证码");
                        }
                    }
                });
    }

    private void updatePwd() {
        if (TextUtils.isEmpty(mEtCode.getText().toString().trim())) {
            ToastUtils.showShort("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(mEtPwd.getText().toString().trim())) {
            ToastUtils.showShort("新密码不能为空");
            return;
        }
        update();

    }

    private void update() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("mobile", mEtName.getText().toString().trim());
        maps.put("password", mEtPwd.getText().toString().trim());
        maps.put("sms_code", mEtCode.getText().toString().trim());
        maps.put("role", "member");
        showLoadding();
        String url = (mType == 1) ? "/api/update_login_pass" : "/api/update_cash_pass";
        EasyHttp.post(url)
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
                            finish();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }
}
