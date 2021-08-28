package com.block.xjfkchain.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.king.signature.PaintActivity;
import android.king.signature.config.PenConfig;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.constant.Constants;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.ImageUploadResponse;
import com.block.xjfkchain.data.OrderDetailResponse;
import com.block.xjfkchain.data.OrderEntity;
import com.block.xjfkchain.data.ProductEntity;
import com.block.xjfkchain.data.ProductResponse;
import com.block.xjfkchain.dialog.ConfirmOrderDialog;
import com.block.xjfkchain.utils.ClipboardUtils;
import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.InputConfirmPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
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
 * ProductDetailActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/7, muwenlei, Create file
 */
public class ProductDetailActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.iv_image)
    ImageView mIvImage;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_sub)
    TextView mTvSub;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.tv_add)
    TextView mTvAdd;
    @BindView(R.id.web_detail)
    WebView mWebDetail;
    @BindView(R.id.tv_select_num)
    TextView mTvSelectNum;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.tv_unit)
    TextView tv_unit;
    @BindView(R.id.tv_kf)
    TextView mTvKf;
    @BindView(R.id.tv_stock)
    TextView tv_stock;
    @BindView(R.id.tv_revenue_time)
    TextView tv_revenue_time;
    @BindView(R.id.iv_shop_buy)
    ImageView mIvShopBuy;

    String mPath;

    private String mImageUrl;

    private ConfirmOrderDialog confirmOrderDialog;
    private long mId;
    private ProductEntity mProductEntity;

    private int mNum = 1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_produt_detail;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mId = getIntent().getLongExtra("id", 0);
        getData();
        PenConfig.THEME_COLOR = Color.parseColor("#2E315D");
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .request();
    }


    private void getData() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", mId + "");
        showLoadding();
        EasyHttp.post("/api/product/detail")
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
                            mProductEntity = JSONObject.parseObject(string, ProductResponse.class).data;
                            if (mProductEntity != null) {
                                showDataView();
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }

    private void showDataView() {
        if (mProductEntity.content != null) {

        }
        mWebDetail.loadUrl(Constants.BASE_URL + "/api/product/detail/" + mId);
        Glide.with(this)
                .load(mProductEntity.cover).into(mIvImage);
        mTvPrice.setText(mProductEntity.price_txt);
        mTvName.setText(mProductEntity.name);
        tv_stock.setText("库存："+mProductEntity.stock);
        tv_revenue_time.setText("预计收益时间："+mProductEntity.income_start_time);
//        mTvType.setText(mProductEntity.recommend);
        mTvSelectNum.setText("数量*1 总价");
        mTvKf.setText(mProductEntity.wechat_client);
        mTvMoney.setText(mProductEntity.price + mProductEntity.unit);
        tv_unit.setText(mProductEntity.unit);
    }

    private void refreshpriceView() {
        mTvNum.setText(mNum + "");
        mTvSelectNum.setText("数量*" + mNum + " 总价");
        mTvMoney.setText(new BigDecimal(mProductEntity.price).multiply(new BigDecimal(mNum)).toString() +mProductEntity.unit);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sub)
    public void onMTvSubClicked() {
        if (mNum > 1) {
            mNum--;
            refreshpriceView();
        }
    }

    @OnClick(R.id.tv_num)
    public void onMTvNumClicked() {
        InputConfirmPopupView popupView = new XPopup.Builder(this).asInputConfirm("输入购买数量", "当前选择数量" + mNum, "请输入数量",
                new OnInputConfirmListener() {
                    @Override
                    public void onConfirm(String text) {
                        try {
                            int n = Integer.valueOf(text);
                            if (n > 0) {
                                mNum = n;
                                refreshpriceView();
                            } else {
                                ToastUtils.showShort("输入的数量有误，请重新输入");
                            }
                        } catch (Exception e) {
                            ToastUtils.showShort("输入的数量有误，请重新输入");
                        }
                    }
                }).bindLayout(R.layout.popup_center_impl_confirm);
        popupView.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            mPath = data.getStringExtra(PenConfig.SAVE_PATH);
            Log.i("king", mPath);
            Bitmap bitmap = BitmapFactory.decodeFile(mPath);
            if (bitmap != null && confirmOrderDialog != null) {
                confirmOrderDialog.setPrint(bitmap);
            }
        }
    }

    @OnClick(R.id.tv_add)
    public void onMTvAddClicked() {
        mNum++;
        refreshpriceView();
    }

    @OnClick(R.id.iv_wx)
    public void onMIvWxClicked() {
        ClipboardUtils.copyText(mProductEntity.wechat_client);
        ToastUtils.showShort("复制成功");
    }

    @OnClick(R.id.iv_shop_buy)
    public void onMIvShopBuyClicked() {
        confirmOrderDialog = new ConfirmOrderDialog(this);
        confirmOrderDialog.setProductEntity(mProductEntity, mNum, new ConfirmOrderDialog.ConfirmClickListener() {
            @Override
            public void onConfirm() {
                uploadImage(mPath);
            }

            @Override
            public void onPrint() {
                Intent intent = new Intent(ProductDetailActivity.this, PaintActivity.class);
                intent.putExtra("crop", false);   //最终的图片是否只截取文字区域
                intent.putExtra("format", PenConfig.FORMAT_PNG); //图片格式
                startActivityForResult(intent, 100);
            }
        });
        new XPopup.Builder(this)
                .asCustom(confirmOrderDialog)
                .show();
    }

    private void buy() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("product_id", mId + "");
        maps.put("num", mNum + "");
        maps.put("sign_url",mImageUrl);
        showLoadding();
        EasyHttp.post("/api/checkout/create_order")
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
                            OrderEntity orderEntity = JSONObject.parseObject(string, OrderDetailResponse.class).data;
                            if (orderEntity != null) {
                                confirmOrderDialog.dismiss();
                                Intent intent = new Intent(ProductDetailActivity.this, PayActivity.class);
                                intent.putExtra("entity", orderEntity);
                                startActivity(intent);
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }

    private void uploadImage(final String filePath) {
        showLoadding();
        mImageUrl = "";
        List<File> files = new ArrayList<>();
        files.add(new File(filePath));
        HashMap<String, String> maps = new HashMap<>();
        maps.put("path", "path");
        maps.put("type", "type");
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
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            ImageUploadResponse imageUploadResponse = JSONObject.parseObject(string, ImageUploadResponse.class);
                            mImageUrl = imageUploadResponse.data.src;
                            buy();
                        } else {
                            dismissLoadding();
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });

    }
}
