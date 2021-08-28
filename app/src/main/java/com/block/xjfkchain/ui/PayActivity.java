package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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
import com.block.xjfkchain.data.ImageUploadResponse;
import com.block.xjfkchain.data.OrderEntity;
import com.block.xjfkchain.data.UserEntity;
import com.block.xjfkchain.utils.ClipboardUtils;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Copyright (C) 2020, Relx
 * payActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class PayActivity extends BusinessBaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_code)
    ImageView mIvCode;
    @BindView(R.id.tv_wallet)
    TextView mTvWallet;
    @BindView(R.id.tv_copy)
    TextView mTvCopy;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.iv_certificate)
    ImageView mIvCertificate;
    @BindView(R.id.tv_wx)
    TextView mTvWx;
    @BindView(R.id.tv_buy)
    TextView mTvBuy;
    @BindView(R.id.tv_yue)
    TextView mTvYue;
    @BindView(R.id.ll_yue)
    LinearLayout mLlYue;
//    @BindView(R.id.et_use_yue)
//    EditText mEtUseYue;
//    @BindView(R.id.ll_use_yue)
//    LinearLayout mLlUseYue;
    @BindView(R.id.tv_pay)
    TextView mTvPay;
    @BindView(R.id.ll_pay_money)
    LinearLayout mLlPayMoney;
    @BindView(R.id.tv_certificate)
    TextView mTvCertificate;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.tv_order_no)
    TextView mTvOrderNo;
    @BindView(R.id.tv_product_name)
    TextView mTvProductName;
    @BindView(R.id.tv_product_price)
    TextView mTvProductPrice;
    @BindView(R.id.tv_product_num)
    TextView mTvProductNum;
    @BindView(R.id.tv_text1)
    TextView mTvText1;
    @BindView(R.id.ll_wallet)
    LinearLayout mLlWallet;

    private OrderEntity mOrderEntity;
    private String mImageUrl;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private UserEntity mUserEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_buy;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {

        mOrderEntity = (OrderEntity) getIntent().getSerializableExtra("entity");
        mUserEntity = App.getApplication().getEntity();
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("购买协议");
        if (mOrderEntity.status == OrderEntity.STATUS_NOPAY) {
            mTvTitle.setText("购买");
            mTvBuy.setText("确认购买");
            mTvBuy.setVisibility(View.VISIBLE);
            mIvCertificate.setEnabled(true);
            mTvText1.setVisibility(View.VISIBLE);
            mIvCode.setVisibility(View.VISIBLE);
            mLlWallet.setVisibility(View.VISIBLE);
            setPayYueShow();
        } else if (mOrderEntity.status == OrderEntity.STATUS_CHECK) {
            mTvTitle.setText("修改凭证");
            mTvBuy.setText("确认修改");
            mTvBuy.setVisibility(View.VISIBLE);
            mTvText1.setVisibility(View.VISIBLE);
            mIvCode.setVisibility(View.VISIBLE);
            mLlWallet.setVisibility(View.VISIBLE);
            mIvCertificate.setEnabled(true);
            setUpdateShow();
        } else {
            mTvTitle.setText("订单详情");
            mTvBuy.setVisibility(View.GONE);
            mIvCertificate.setEnabled(false);
            mTvText1.setVisibility(View.GONE);
            mIvCode.setVisibility(View.GONE);
            mLlWallet.setVisibility(View.GONE);
            setUpdateShow();
        }
        showView();
//        mEtUseYue.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                try {
//                    if (!TextUtils.isEmpty(charSequence.toString())) {
//                        Double d = Double.valueOf(charSequence.toString());
//                        Double usdt = Double.valueOf(mUserEntity.usdt);
//                        Double total = Double.valueOf(mOrderEntity.total);
//                        if (usdt < d) {
//                            ToastUtils.showShort("使用余额超出可用余额,请重新输入");
//                            mEtUseYue.setText("");
//                        } else if (d > total) {
//                            ToastUtils.showShort("使用余额超出商品总价");
//                            mEtUseYue.setText(mOrderEntity.total + "");
//                        } else {
//                            double f = total - d;
//                            BigDecimal bg = new BigDecimal(f);
//                            double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                            mTvPay.setText(f1 +mOrderEntity.price_unit);
//                            if (f1 > 0) {
//                                mIvCertificate.setVisibility(View.VISIBLE);
//                                mTvCertificate.setVisibility(View.VISIBLE);
//                            } else {
//                                mIvCertificate.setVisibility(View.GONE);
//                                mTvCertificate.setVisibility(View.GONE);
//                            }
//                        }
//                    } else {
//                        mTvPay.setText(mOrderEntity.total + mOrderEntity.price_unit);
//                        mIvCertificate.setVisibility(View.VISIBLE);
//                        mTvCertificate.setVisibility(View.VISIBLE);
//                    }
//                } catch (Exception e) {
//                    mEtUseYue.setText("");
//                    ToastUtils.showShort("使用余额输入错误,请重新输入");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    private void setPayYueShow() {
//        mEtUseYue.setEnabled(true);
        try {
            Double d = Double.valueOf(mUserEntity.usdt);
            if (d > 0) {
                mLlYue.setVisibility(View.VISIBLE);
                mTvYue.setText(" "+mOrderEntity.valid_power);
//                mLlUseYue.setVisibility(View.VISIBLE);
                mLlPayMoney.setVisibility(View.VISIBLE);
            } else {
                mLlYue.setVisibility(View.GONE);
//                mLlUseYue.setVisibility(View.GONE);
                mLlPayMoney.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            mLlYue.setVisibility(View.GONE);
//            mLlUseYue.setVisibility(View.GONE);
            mLlPayMoney.setVisibility(View.GONE);
        }

    }

    private void setUpdateShow() {
//        mEtUseYue.setEnabled(false);
        mLlYue.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mOrderEntity.use_usdt) || "0.00".equals(mOrderEntity.use_usdt)) {
//            mLlUseYue.setVisibility(View.GONE);
            mLlPayMoney.setVisibility(View.GONE);
        } else {
//            mLlUseYue.setVisibility(View.VISIBLE);
            mLlPayMoney.setVisibility(View.VISIBLE);
//            mEtUseYue.setText(mOrderEntity.use_usdt);
            mTvPay.setText(mOrderEntity.need_pay + mOrderEntity.price_unit);
        }
        if ("0.00".equals(mOrderEntity.need_pay) || "0".equals(mOrderEntity.need_pay)) {
            mIvCertificate.setVisibility(View.GONE);
            mTvCertificate.setVisibility(View.GONE);
        } else {
            mIvCertificate.setVisibility(View.VISIBLE);
            mTvCertificate.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_copy)
    public void onMTvCopyClicked() {
        ClipboardUtils.copyText(mOrderEntity.wallet_address);
        ToastUtils.showShort("复制成功");
    }

    @OnClick(R.id.iv_certificate)
    public void onMIvCertificateClicked() {
        getTakePhoto().onPickFromGallery();
    }

    @OnClick(R.id.tv_buy)
    public void onMTvBuyClicked() {
        commitBuy();
    }

    @OnClick(R.id.tv_wx)
    public void onMTvWxClicked() {
        ClipboardUtils.copyText(mOrderEntity.wechat_client);
        ToastUtils.showShort("复制成功");
    }


    @OnClick(R.id.tv_right)
    public void onViewClicked() {
        Intent intent = new Intent(this, CommonWebActivity.class);
        intent.putExtra("url", mOrderEntity.buy_notice_url+"?id=" + App.getApplication().getUserEntity().user.union_uid + "&order_id=" + mOrderEntity.id);
//        intent.putExtra("url", Constants.BASE_URL + "/api/buy_notice?id=" + App.getApplication().getUserEntity().user.union_uid + "&order_id=" + mOrderEntity.id);
        intent.putExtra("title", "购买协议");
        startActivity(intent);
    }

    private void showView() {
        Glide.with(this).load(mOrderEntity.wallet_qrcode).into(mIvCode);
        if (!TextUtils.isEmpty(mOrderEntity.pay_screenshot)) {
            Glide.with(PayActivity.this).load(mOrderEntity.pay_screenshot).into(mIvCertificate);
        }
        mTvWallet.setText("钱包地址:" + mOrderEntity.wallet_address);
        mTvMoney.setText(mOrderEntity.total + mOrderEntity.price_unit);
        mTvPay.setText(mOrderEntity.need_pay + mOrderEntity.price_unit);
        mTvWx.setText("点击复制微信号，添加您的专属客服:" + mOrderEntity.wechat_client);

        mTvOrderNo.setText(mOrderEntity.id + "");
        mTvProductName.setText(mOrderEntity.product_name);
        mTvProductPrice.setText(mOrderEntity.product_price + mOrderEntity.price_unit);
        mTvProductNum.setText(mOrderEntity.num + "");
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        if (!TextUtils.isEmpty(result.getImage().getCompressPath())) {
            uploadImage(result.getImage().getCompressPath());
        } else {
            uploadImage(result.getImage().getOriginalPath());
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
    }

    @Override
    public void takeCancel() {
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }


    private void uploadImage(final String filePath) {
        mImageUrl = "";
        List<File> files = new ArrayList<>();
        files.add(new File(filePath));
        HashMap<String, String> maps = new HashMap<>();
        maps.put("path", "path");
        maps.put("type", "type");
        showLoadding();
        EasyHttp.post("/api/upload")
                .params(maps)
                .addFileParams("file", files, null)
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
                            ImageUploadResponse imageUploadResponse = JSONObject.parseObject(string, ImageUploadResponse.class);
                            Glide.with(PayActivity.this).load(filePath).into(mIvCertificate);
                            mImageUrl = imageUploadResponse.data.src;
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });

    }

    private void commitBuy() {

        HashMap<String, String> maps = new HashMap<>();
//        String usdt = mEtUseYue.getText().toString();
//        if (!TextUtils.isEmpty(usdt)) {
//            maps.put("use_usdt", usdt);
//        }
        if (mIvCertificate.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(mImageUrl)) {
                ToastUtils.showShort("请上传付款凭证");
                return;
            }
        }
        maps.put("orders_id", mOrderEntity.id + "");
        if (!TextUtils.isEmpty(mImageUrl)) {
            maps.put("pay_screenshot", mImageUrl);
        }
        if (mOrderEntity.status == OrderEntity.STATUS_CHECK) {
            maps.put("re_pay", "1");
        }
        showLoadding();
        EasyHttp.post("/api/checkout/pay")
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
                            ToastUtils.showShort("支付成功，请等待审核");
                            Intent intent = new Intent(PayActivity.this, OrderActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }


}
