package com.block.xjfkchain.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.ImageUploadResponse;
import com.block.xjfkchain.data.UserEntity;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.InputConfirmPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Copyright (C) 2020, Relx
 * UserInfoActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/6/16, muwenlei, Create file
 */
public class UserInfoActivity extends BusinessBaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_iamge)
    CircleImageView mIvIamge;
    @BindView(R.id.llyt_member_icon)
    LinearLayout mLlytMemberIcon;
    @BindView(R.id.line_member_icon)
    View mLineMemberIcon;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.llyt_member_name)
    LinearLayout mLlytMemberName;


    private String image;
    private String mImageUrl;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private UserEntity entity;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("个人信息");
        image = getIntent().getStringExtra("icon");
        entity = (UserEntity) getIntent().getSerializableExtra("entity");
        Glide.with(UserInfoActivity.this).load(image).into(mIvIamge);
        mTvName.setText(App.getApplication().getUserName());
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }


    @OnClick(R.id.llyt_member_icon)
    public void onMLlytMemberIconClicked() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);
        CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);

    }

    @OnClick(R.id.llyt_member_name)
    public void onMLlytMemberNameClicked() {
        InputConfirmPopupView popupView = new XPopup.Builder(this).asInputConfirm("修改姓名", App.getApplication().getUserName(), "请输入要修改的姓名",
                new OnInputConfirmListener() {
                    @Override
                    public void onConfirm(String text) {
                        if (TextUtils.isEmpty(text)) {
                            ToastUtils.showShort("姓名不能为空");
                            return;
                        }
                        updateInfo(text, "", true);

                    }

                });

        popupView.show();
    }

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
            updateInfo("", mImageUrl, false);
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
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            ImageUploadResponse imageUploadResponse = JSONObject.parseObject(string, ImageUploadResponse.class);
                            mImageUrl = imageUploadResponse.data.src;
                            image = imageUploadResponse.link;
                            updateInfo("", mImageUrl, false);
                        } else {
                            dismissLoadding();
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });

    }

    private void updateInfo(final String name, final String iconUrl, boolean isLoading) {
        if (isLoading) {
            showLoadding();
        }
        HashMap<String, String> maps = new HashMap<>();
        if (!TextUtils.isEmpty(name)) {
            maps.put("name", name);
        }
        if (!TextUtils.isEmpty(iconUrl)) {
            maps.put("avatar", iconUrl);
        }
        EasyHttp.post("/api/update_profile")
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
                            if (!TextUtils.isEmpty(name)) {
                                mTvName.setText(name);
                                App.getApplication().setUserName(name);
                            }
                            if (!TextUtils.isEmpty(iconUrl)) {
                                Glide.with(UserInfoActivity.this).load(image).into(mIvIamge);
                            }
                            ToastUtils.showShort("修改成功");
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }
}
