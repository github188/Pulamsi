package com.pulamsi.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * 用于打开H5的带webview的activity
 *
 * @author WilliamChik on 15/8/20.
 */
public class CommonWebViewActivity extends BaseActivity {

    private static final String TAG = CommonWebViewActivity.class.getSimpleName();

    private WebView mWebView;

    /**
     * 需要用webview打开的url地址
     */
    private String mOriginalUrl;

    private String title;

    SmoothProgressBar progressbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_webview_activity);
        initWebViewParams();
        initUI();
        loadUrl();
    }

    @SuppressLint("JavascriptInterface")
    private void initUI() {
        mWebView = (WebView) findViewById(R.id.wv_common_webview_webview);
        progressbar = (SmoothProgressBar) findViewById(R.id.progress);
        progressbar.setIndeterminate(true);

//        mWebView.getSettings().setBuiltInZoomControls(true);
        if ("普兰氏用户协议".equals(title)) {
            mWebView.setInitialScale(100);
        }
        WebSettings webSet = mWebView.getSettings();
        webSet.setJavaScriptEnabled(true);
        webSet.setJavaScriptCanOpenWindowsAutomatically(true);
        webSet.setDefaultTextEncodingName("utf-8");

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressbar.setVisibility(View.VISIBLE);
                } else if (newProgress == 100) {
                    progressbar.setVisibility(View.GONE);
                    invalidateOptionsMenu();
                }
                progressbar.setProgress(newProgress);

                super.onProgressChanged(view, newProgress);
            }

        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 打开 https 页面时接受证书
                handler.proceed();
            }
        });

        mTitleHeaderBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBack();
            }
        });
    }

    private void isBack() {
        if (!mWebView.canGoBack()) {
            this.finish();
        } else {
            mWebView.goBack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 获取webview参数
     */
    private void initWebViewParams() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        try {
            mOriginalUrl = bundle.getString(Constants.WEBVIEW_LOAD_URL);
            title = bundle.getString(Constants.WEBVIEW_TITLE);

            if (TextUtils.isEmpty(title)) {
                title = "普兰氏";
            }
            setHeaderTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
            HaiLog.e(TAG, "解析bundle数据出错！错误原因是:" + e);
        }
    }

    @SuppressLint("JavascriptInterface")
    private void loadUrl() {
        if (TextUtils.isEmpty(mOriginalUrl)) {
            return;
        }
        mWebView.loadUrl(mOriginalUrl);
    }


    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }
}
