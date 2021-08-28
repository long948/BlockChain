package com.block.xjfkchain.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.base.BusinessBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonWebActivity extends BusinessBaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.wb_content)
    WebView mWbContent;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private String mUrl;
    private String mTitle;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_commonweb;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        initWebView();
        mTvTitle.setText(mTitle);
        showLoadding();
        mWbContent.loadUrl(mUrl);
    }

    void initWebView() {
        mWbContent.setHorizontalScrollBarEnabled(false);//水平不显示
        mWbContent.setVerticalScrollBarEnabled(true); //垂直不显示
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dismissLoadding();
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, final HttpAuthHandler handler, final String host, final String realm) {

            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }


        };
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }

        };

        mWbContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWbContent.getSettings().setLoadWithOverviewMode(true);
        mWbContent.setWebViewClient(webViewClient);
        mWbContent.setWebChromeClient(webChromeClient);
        mWbContent.getSettings().setDomStorageEnabled(true);
        mWbContent.getSettings().setJavaScriptEnabled(true);
        mWbContent.getSettings().setSupportZoom(true);
//        mWbContent.setWebViewClient(new WebViewClient());
        mWbContent.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWbContent.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWbContent.getSettings().setUseWideViewPort(true);
        mWbContent.getSettings().setAllowFileAccess(true);
        mWbContent.clearCache(true);
        mWbContent.setBackgroundColor(Color.argb(0, 0, 0, 0));
        mWbContent.getSettings().setCacheMode(mWbContent.getSettings().LOAD_NO_CACHE); // 设置
        mWbContent.requestFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWbContent.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
