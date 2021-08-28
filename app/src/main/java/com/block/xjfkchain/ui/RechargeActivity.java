package com.block.xjfkchain.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.utils.ClipboardUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RechargeActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_icon)
    ImageView mIvCode;
    @BindView(R.id.tv_address)
    TextView mTvAddress;

    private String mQrcode;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("充值");
        mQrcode = getIntent().getStringExtra("address");
        Glide.with(this).load(mQrcode).into(mIvCode);
        mTvAddress.setText(mQrcode);
        mCompositeDisposable.add(
                Observable.just(1)
                        .flatMap(new Function<Integer, ObservableSource<Bitmap>>() {
                            @Override
                            public ObservableSource<Bitmap> apply(Integer integer) throws Exception {
                                return Observable.just(QRCodeEncoder.syncEncodeQRCode("mQrcode", BGAQRCodeUtil.dp2px(RechargeActivity.this, 150)));
                            }
                        }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Bitmap>() {
                            @Override
                            public void accept(Bitmap bitmap) throws Exception {
                                if (bitmap != null) {
                                    mIvCode.setImageBitmap(bitmap);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        }));
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }


    @OnClick(R.id.tv_address)
    public void onViewAddressClicked() {
        ClipboardUtils.copyText(mQrcode);
        ToastUtils.showShort("复制成功");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
