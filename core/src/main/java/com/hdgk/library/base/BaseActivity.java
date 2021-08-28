package com.hdgk.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.ToastUtils;
import com.hdgk.library.widget.ShapeLoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Copyright (C) 2018,
 * BaseActivity
 * <p>
 * Description
 *
 * @author mwl
 * @version 1.0
 * <p>
 * Ver 1.0, 2019-06-06, mwl, Create file
 */
public abstract class BaseActivity extends AppCompatActivity {


    private ShapeLoadingDialog shapeLoadingDialog;

    protected Unbinder mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.e("BaseActivity",getClass().getSimpleName());
        int layoutId;
        if ((layoutId = getContentViewId()) != 0) {
            setContentView(layoutId);
            mBinder = ButterKnife.bind(this);
        }
        initTitleBar();
        onViewCreated(savedInstanceState);

    }

    protected void initTitleBar() {
    }

    protected abstract int getContentViewId();



    protected abstract void onViewCreated(Bundle savedInstanceState);


    /**
     * 显示dialog
     */
    public void showLoadding() {
        showLoadding("请稍后...");
    }


    /**
     * 显示dialog
     */
    public void showLoadding(String title) {
        if (shapeLoadingDialog == null) {
            if (shapeLoadingDialog != null) {
                shapeLoadingDialog.show();
            } else {
                shapeLoadingDialog = new ShapeLoadingDialog.Builder(this)
                        .cancelable(true)
                        .canceledOnTouchOutside(false)
                        .loadText(title)
                        .build();
                shapeLoadingDialog.show();
            }
        }
    }

    /**
     * 隐藏dialog
     */
    public void dismissLoadding() {
        if (shapeLoadingDialog != null && shapeLoadingDialog.isShowing()) {
            shapeLoadingDialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        if (mBinder!=null){
            mBinder.unbind();
        }
        super.onDestroy();
    }

}
