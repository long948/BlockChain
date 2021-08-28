package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.constant.Constants;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.LoginResponse;
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

public class ForgetPwdActivity extends BusinessBaseActivity {
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_repwd)
    EditText mEtRepwd;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.tv_getcode)
    TextView mTvGetcode;
    @BindView(R.id.iv_forgetpwd)
    TextView mIvForgetpwd;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_forgetpwd;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {

    }

    @OnClick(R.id.tv_getcode)
    public void onMTvGetcodeClicked() {
        getCode();
    }

    @OnClick(R.id.iv_forgetpwd)
    public void onMIvForgetpwdClicked() {
        register();
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

    private void register() {
        if (TextUtils.isEmpty(mEtName.getText().toString().trim())) {
            ToastUtils.showShort("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(mEtPwd.getText().toString().trim())) {
            ToastUtils.showShort("密码不能为空");
            return;
        }
        if (!mEtRepwd.getText().toString().trim().equals(mEtPwd.getText().toString().trim())) {
            ToastUtils.showShort("两次输入的密码不一致");
            return;
        }
        if (TextUtils.isEmpty(mEtCode.getText().toString().trim())) {
            ToastUtils.showShort("验证码不能为空");
            return;
        }
        HashMap<String, String> maps = new HashMap<>();
        maps.put("mobile", mEtName.getText().toString().trim());
        maps.put("password", mEtPwd.getText().toString().trim());
        maps.put("sms_code", mEtCode.getText().toString().trim());
        maps.put("type", "username");
        maps.put("role", "member");
        showLoadding();
        EasyHttp.post("/api/forget_password")
                .params(maps)
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
                            LoginResponse userResponse = JSONObject.parseObject(string, LoginResponse.class);
                            SPUtils.getInstance().put(Constants.SP_USER, JSONObject.toJSONString(userResponse.data));
                            App.getApplication().setUserEntity(userResponse.data);
                            Intent intent = new Intent(ForgetPwdActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
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


}
