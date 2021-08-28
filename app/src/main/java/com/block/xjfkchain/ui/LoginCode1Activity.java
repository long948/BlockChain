package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginCode1Activity extends BusinessBaseActivity {
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.iv_login)
    ImageView mIvLogin;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login_code1;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {

    }

    @OnClick(R.id.iv_login)
    public void onMIvLoginClicked() {
        getCode();
    }

    @OnClick(R.id.iv_register)
    public void onMIvRegisterClicked() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_pwd_login)
    public void onMTvPwdLoginClicked() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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
                            Intent intent = new Intent(LoginCode1Activity.this, LoginCode2Activity.class);
                            intent.putExtra("phone",mEtName.getText().toString().trim());
                            startActivity(intent);
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }
}
