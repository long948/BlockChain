package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.constant.Constants;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.LoginResponse;
import com.block.xjfkchain.data.UserEntity;
import com.block.xjfkchain.data.UserResponse;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;


import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BusinessBaseActivity {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_no)
    EditText etNo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }


    @Override
    protected void onViewCreated(Bundle savedInstanceState) {

    }

//    @OnClick(R.id.tv_getcode)
//    public void onTvGetCodeClicked() {
//        getCode();
//    }

//    @OnClick(R.id.tv_replace_login)
//    public void onTvReplaceLoginClicked() {
//        mIsPassword = !mIsPassword;
//        changeView();
//
//    }

    @OnClick(R.id.iv_login)
    public void onTvSignClicked() {
        login();
    }

    @OnClick(R.id.tv_code_login)
    public void onTvCodeLoginClicked() {
        Intent intent = new Intent(this, LoginCode1Activity.class);
        startActivity(intent);
    }

    private void login() {
        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            ToastUtils.showShort("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(etNo.getText().toString().trim())) {
            ToastUtils.showShort("密码不能为空");
            return;
        }
        HashMap<String, String> maps = new HashMap<>();
        maps.put("mobile", etName.getText().toString().trim());
        maps.put("password", etNo.getText().toString().trim());
//        maps.put(mIsPassword ? "password" : "sms_code", etNo.getText().toString().trim());
//        maps.put("type", mIsPassword ? "username" : "mobile");
        maps.put("type", "username");
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
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }


//


    @OnClick(R.id.iv_register)
    public void onTvRegisterClicked() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.tv_forgetpwd)
    public void onTvForgetPwdClicked() {
        Intent intent = new Intent(this, ForgetPwdActivity.class);
        startActivity(intent);
    }

//    void changeView() {
//        etName.setText("");
//        etNo.setText("");
//        if (mIsPassword) {
//            etNo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            etNo.setHint("请输入登录密码");
//            tvGetcode.setVisibility(View.GONE);
//            tvReplaceLogin.setText("短信验证码登录");
//        } else {
//            etNo.setHint("请输入验证码");
//            etNo.setInputType(InputType.TYPE_CLASS_NUMBER);
//            tvGetcode.setVisibility(View.VISIBLE);
//            tvReplaceLogin.setText("账户密码登录");
//        }
//    }


}
