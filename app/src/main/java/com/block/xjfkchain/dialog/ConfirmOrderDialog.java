package com.block.xjfkchain.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.constant.Constants;
import com.block.xjfkchain.data.ProductEntity;
import com.block.xjfkchain.ui.CommonWebActivity;
import com.lxj.xpopup.core.BottomPopupView;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmOrderDialog extends BottomPopupView {

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.cb_protocol)
    CheckBox mCbProtocol;
    @BindView(R.id.tv_protocol)
    TextView mTvProtocol;
    @BindView(R.id.iv_sign)
    ImageView mIvSign;
    @BindView(R.id.iv_ok)
    ImageView mIvOk;

    private ConfirmClickListener mConfirmClickListener;

    private ProductEntity mProductEntity;
    private int mNum = 1;
    private Bitmap bitmap;

    public ConfirmOrderDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this);
        mTvProtocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mTvName.setText(mProductEntity.name);
        mTvNum.setText("产品数量  x" + mNum);
        mTvMoney.setText("产品总价  " + new BigDecimal(mProductEntity.price).multiply(new BigDecimal(mNum)).toString() + mProductEntity.unit);

    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    public void setProductEntity(ProductEntity productEntity, int num, ConfirmClickListener confirmClickListener) {
        mProductEntity = productEntity;
        mNum = num;
        mConfirmClickListener = confirmClickListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_confirm_order;
    }

    @OnClick(R.id.tv_protocol)
    public void onMTvProtocolClicked() {
        Intent intent = new Intent(getContext(), CommonWebActivity.class);
//        intent.putExtra("url", Constants.BASE_URL + "/api/buy_notice?id=" + App.getApplication().getUserEntity().user.union_uid + "&product_id=" + mProductEntity.id);
        intent.putExtra("url", mProductEntity.buy_notice_url+"?id=" + App.getApplication().getUserEntity().user.union_uid + "&product_id=" + mProductEntity.id);
        Log.e("url", mProductEntity.buy_notice_url+"?id=" + App.getApplication().getUserEntity().user.union_uid + "&product_id=" + mProductEntity.id);
        intent.putExtra("title", "购买协议");
        getContext().startActivity(intent);
    }

    public void setPrint(Bitmap b) {
        bitmap = b;
        mIvSign.setImageBitmap(b);
    }

    @OnClick(R.id.iv_ok)
    public void onMIvOkClicked() {
        if (!mCbProtocol.isChecked()) {
            ToastUtils.showShort("请阅读相关内容并同意");
            return;
        }
        if (bitmap == null) {
            ToastUtils.showShort("请签写签名");
            return;
        }
        if (mConfirmClickListener != null) {
            mConfirmClickListener.onConfirm();
        }

    }

    @OnClick(R.id.iv_sign)
    public void onMIvSignClicked() {
        if (mConfirmClickListener != null) {
            mConfirmClickListener.onPrint();
        }

    }

    public interface ConfirmClickListener {
        void onConfirm();

        void onPrint();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (ScreenUtils.getScreenHeight() * 0.75);
    }
}
