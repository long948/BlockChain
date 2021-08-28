package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.constant.Constants;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.LoginEntity;
import com.block.xjfkchain.data.UserEntity;
import com.block.xjfkchain.data.UserResponse;
import com.block.xjfkchain.ui.fragment.FinancialFragment;
import com.block.xjfkchain.ui.fragment.HomeFragment;
import com.block.xjfkchain.ui.fragment.MarketFragment;
import com.block.xjfkchain.ui.fragment.MineFragment;
import com.block.xjfkchain.ui.fragment.NewHomeFragment;
import com.block.xjfkchain.ui.fragment.ProductFragment;
import com.block.xjfkchain.widget.bottomnavigation.BadgeItem;
import com.block.xjfkchain.widget.bottomnavigation.BottomNavigationBar;
import com.block.xjfkchain.widget.bottomnavigation.BottomNavigationItem;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;

import butterknife.BindView;

public class MainActivity extends BusinessBaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @BindView(R.id.main_container)
    FrameLayout mMainContainer;
    private HomeFragment mHomeFragment;
    private ProductFragment mProductFragment;
    private FinancialFragment mFinancialFragment;
    private MarketFragment mMarketFragment;
    private MineFragment mMineFragment;
    private FragmentManager mFragmentManager;
    private int is_show_financial=0;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    private UserEntity userEntity;

    private void getData() {
        HashMap<String, String> maps = new HashMap<>();
        LogUtils.e("Bearer " + App.getApplication().getUserEntity().token);
        EasyHttp.post("/api/profile")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                        ToastUtils.showShort("获取信息错误");
                    }

                    @Override
                    public void onSuccess(String string) {
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            userEntity = JSONObject.parseObject(string, UserResponse.class).data;
                            if (userEntity != null) {
                                App.getApplication().setUserEntity(userEntity);
                                is_show_financial=userEntity.is_show_financial;
                                runOnUiThread(() -> initFragment());
                                Log.e("LoGis_show_financial", String.valueOf(is_show_financial));
                            }
                        }
                    }
                });
    }

    /**
     * 初始化Fragment
     */
    private void initFragment(){
        mFragmentManager = getSupportFragmentManager();
        mHomeFragment = new HomeFragment();
        mProductFragment = new ProductFragment();
        mMarketFragment = new MarketFragment();
        mMineFragment = new MineFragment();
        addFragment(R.id.main_container, mHomeFragment, "home");
        if (is_show_financial==1) {
            mFinancialFragment = new FinancialFragment();
            addFragment(R.id.main_container, mFinancialFragment, "financial");
        }

        addFragment(R.id.main_container, mProductFragment, "product");
        addFragment(R.id.main_container, mMarketFragment, "market");
        addFragment(R.id.main_container, mMineFragment, "mine");
        if (is_show_financial==1){
            mFragmentManager.beginTransaction()
                    .show(mHomeFragment)
                    .hide(mProductFragment)
                    .hide(mFinancialFragment)
                    .hide(mMarketFragment)
                    .hide(mMineFragment)
                    .commitAllowingStateLoss();
        }else {
            mFragmentManager.beginTransaction()
                    .show(mHomeFragment)
                    .hide(mProductFragment)
                    .hide(mMarketFragment)
                    .hide(mMineFragment)
                    .commitAllowingStateLoss();
        }
        initBottomNavigation();
    }
    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        LoginEntity userEntity = JSONObject.parseObject(SPUtils.getInstance().getString(Constants.SP_USER), LoginEntity.class);
        if (userEntity == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            App.getApplication().setUserEntity(userEntity);
            getData();
        }


    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        L.i("MainActivity onSaveInstanceState");
//        //super.onSaveInstanceState(outState);
//    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
    }


    private void initBottomNavigation() {
        BadgeItem numberBadgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.colorAccent)
                .setText("99+")
                .setHideOnSelect(false);

        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        //bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        //bottomNavigationBar.setAutoHideEnabled(true);


        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.tab_home_checked, "首页").setInactiveIconResource(R.mipmap.tab_home)
                        .setActiveColor("#D2D7EA")
                        .setInActiveColor("#A88390C3"));
        if (is_show_financial == 1) {
            mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.tab_licai_checked, "理财").setInactiveIconResource(R.mipmap.tab_licai)
                    .setActiveColor("#D2D7EA")
                    .setInActiveColor("#A88390C3"));
        }
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.tab_product_checked, "产品").setInactiveIconResource(R.mipmap.tab_product)
                        .setActiveColor("#D2D7EA")
                        .setInActiveColor("#A88390C3"))
                .addItem(new BottomNavigationItem(R.mipmap.tab_market_checked, "行情").setInactiveIconResource(R.mipmap.tab_market)
                        .setActiveColor("#D2D7EA")
                        .setInActiveColor("#A88390C3"))
                .addItem(new BottomNavigationItem(R.mipmap.tab_mine_cheked, "我的").setInactiveIconResource(R.mipmap.tab_mine)
                        .setActiveColor("#D2D7EA")
                        .setInActiveColor("#A88390C3"))
                .setFirstSelectedPosition(0)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {
        onTab(position);
    }

    private void onTab(int position){
        if (is_show_financial==1){
            if (position == 0) {
                mFragmentManager.beginTransaction()
                        .show(mHomeFragment)
                        .hide(mProductFragment)
                        .hide(mFinancialFragment)
                        .hide(mMarketFragment)
                        .hide(mMineFragment)
                        .commitAllowingStateLoss();
            } else if (position == 1) {
                mFragmentManager.beginTransaction()
                        .show(mFinancialFragment)
                        .hide(mHomeFragment)
                        .hide(mProductFragment)
                        .hide(mMarketFragment)
                        .hide(mMineFragment)
                        .commitAllowingStateLoss();

            }else if (position == 2) {
                mFragmentManager.beginTransaction()
                        .show(mProductFragment)
                        .hide(mFinancialFragment)
                        .hide(mHomeFragment)
                        .hide(mMarketFragment)
                        .hide(mMineFragment)
                        .commitAllowingStateLoss();

            }
            else if (position == 3) {
                mFragmentManager.beginTransaction()
                        .show(mMarketFragment)
                        .hide(mHomeFragment)
                        .hide(mFinancialFragment)
                        .hide(mProductFragment)
                        .hide(mMineFragment)
                        .commitAllowingStateLoss();

            }
            else if (position == 4) {
                mFragmentManager.beginTransaction()
                        .show(mMineFragment)
                        .hide(mHomeFragment)
                        .hide(mFinancialFragment)
                        .hide(mProductFragment)
                        .hide(mMarketFragment)
                        .commitAllowingStateLoss();
            }
        }else {
            if (position == 0) {
                mFragmentManager.beginTransaction()
                        .show(mHomeFragment)
                        .hide(mProductFragment)
                        .hide(mMarketFragment)
                        .hide(mMineFragment)
                        .commitAllowingStateLoss();
            } else if (position == 1) {
                mFragmentManager.beginTransaction()
                        .show(mProductFragment)
                        .hide(mHomeFragment)
                        .hide(mMarketFragment)
                        .hide(mMineFragment)
                        .commitAllowingStateLoss();

            }
            else if (position == 2) {
                mFragmentManager.beginTransaction()
                        .show(mMarketFragment)
                        .hide(mHomeFragment)
                        .hide(mProductFragment)
                        .hide(mMineFragment)
                        .commitAllowingStateLoss();

            }
            else if (position == 3) {
                mFragmentManager.beginTransaction()
                        .show(mMineFragment)
                        .hide(mHomeFragment)
                        .hide(mProductFragment)
                        .hide(mMarketFragment)
                        .commitAllowingStateLoss();
            }
        }

    }
    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }





}
