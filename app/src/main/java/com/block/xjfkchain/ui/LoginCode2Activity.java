package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.jkb.vcedittext.VerificationAction;
import com.jkb.vcedittext.VerificationCodeEditText;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class LoginCode2Activity extends BusinessBaseActivity {

    @BindView(R.id.et_code)
    VerificationCodeEditText mEtCode;
    @BindView(R.id.tv_getcode)
    TextView mTvGetcode;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    private String mPhone;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login_code2;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mPhone = getIntent().getStringExtra("phone");
        mTvPhone.setText("请输入 +86 "+mPhone+"收到的验证码");
        mEtCode.setOnVerificationCodeChangedListener(new VerificationAction.OnVerificationCodeChangedListener() {
            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onInputCompleted(CharSequence s) {
                login();
            }
        });
    }

    @OnClick(R.id.tv_getcode)
    public void onViewClicked() {
        getCode();
    }


    @OnClick(R.id.iv_back)
    public void onBackClicked() {
        finish();
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
                            mTvGetcode.setText(value + "后重新获取验证码");
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


    private void getCode() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("mobile", mPhone);
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

    private void login() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("mobile", mPhone);
        maps.put("sms_code", mEtCode.getText().toString());
        maps.put("type", "mobile");
        maps.put("role", "member");
        showLoadding();
        EasyHttp.post("/api/login")
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
                            Intent intent = new Intent(LoginCode2Activity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
