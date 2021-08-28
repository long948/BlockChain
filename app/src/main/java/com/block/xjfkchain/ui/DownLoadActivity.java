package com.block.xjfkchain.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.utils.ClipboardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

import static android.os.Environment.DIRECTORY_DCIM;

/**
 * Copyright (C) 2020, Relx
 * DownLoadActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/8/1, muwenlei, Create file
 */
public class DownLoadActivity extends BusinessBaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_save)
    TextView mTvSave;
    @BindView(R.id.tv_copy_address)
    TextView mTvCopyAddress;

    private Bitmap mBitmap;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_download;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("下载链接");
        Glide.with(this)
                .asBitmap()
                .load(R.mipmap.download)
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        mBitmap = resource;
                        return false;
                    }
                }).into(mIvIcon);

    }


    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }

    @OnClick(R.id.iv_icon)
    public void onMIvIconClicked() {
    }

    @OnClick(R.id.tv_save)
    public void onMTvSaveClicked() {
        if (mBitmap == null) {
            return;
        }
        String filepath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).getAbsolutePath() + File.separator + "";
        String fileName = System.currentTimeMillis() + "jpg";
        saveBitmap(filepath, fileName, mBitmap);
    }

    @OnClick(R.id.tv_copy_address)
    public void onMTvCopyAddressClicked() {
        ClipboardUtils.copyText("http://api.xifk.com.cn/api/download_app");
        ToastUtils.showShort("复制成功");
    }


    public void saveBitmap(String filePath, String fileName, Bitmap b) {
        File file = new File(filePath + "/" + fileName);
        try {
            FileOutputStream fout = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 上面的先转file再保存，不知道为啥，本来可以直接调用保存Bitmap的方法，上面方法在华为Mate30的Android Q崩溃
        MediaStore.Images.Media.insertImage(getContentResolver(), b, fileName, null);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        ToastUtils.showShort("保存成功");
    }

}
