package com.hdgk.library.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

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
public abstract class BaseFragment extends Fragment {


    private ShapeLoadingDialog shapeLoadingDialog;

    protected Unbinder mBinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId;
        if ((layoutId = getContentViewId()) != 0) {
            View view = inflater.inflate(layoutId, null);
            mBinder = ButterKnife.bind(this, view);
            return view;
        }
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewCreated(getArguments());
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
                shapeLoadingDialog = new ShapeLoadingDialog.Builder(getContext())
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
    public void onDestroy() {
        if (mBinder != null) {
            mBinder.unbind();
        }
        super.onDestroy();
    }

}
