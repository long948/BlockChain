package com.block.xjfkchain.base;

import com.block.xjfkchain.R;
import com.gyf.immersionbar.ImmersionBar;
import com.hdgk.library.base.BaseActivity;

public abstract class BusinessBaseActivity extends BaseActivity {

    @Override
    protected void initTitleBar() {
        ImmersionBar.with(this).statusBarColor(R.color.transparent).statusBarDarkFont(false).fitsSystemWindows(false).init();
    }
}
