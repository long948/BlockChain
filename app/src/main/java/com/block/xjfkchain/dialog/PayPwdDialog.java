package com.block.xjfkchain.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.lxj.xpopup.core.CenterPopupView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * PayPwdDialog
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/2, muwenlei, Create file
 */
public class PayPwdDialog extends CenterPopupView {
    @BindView(R.id.et_bank_no)
    EditText mEtBankNo;

    public PayPwdDialog(@NonNull Context context) {
        super(context);
    }

    private ConfirmClickListener mConfirmClickListener;

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this);
    }

    public void setConfirmClickListener(ConfirmClickListener mConfirmClickListener) {
        this.mConfirmClickListener = mConfirmClickListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_pay_pwd;
    }


    @OnClick(R.id.iv_ok)
    public void onMIvOkClicked() {
        if (TextUtils.isEmpty(mEtBankNo.getText().toString().trim())) {
            ToastUtils.showShort("请输入支付密码");
            return;
        }
        if (mConfirmClickListener != null) {
            mConfirmClickListener.onConfirm(mEtBankNo.getText().toString().trim());
        }

    }

    @OnClick(R.id.iv_cancel)
    public void onMIvCancelClicked() {
        dismiss();
    }


    public interface ConfirmClickListener {
        void onConfirm(String pwd);
    }

    @Override
    protected int getPopupWidth() {
        return ConvertUtils.dp2px(329);
    }

    @Override
    protected int getPopupHeight() {
        return ConvertUtils.dp2px(260);
    }
}
