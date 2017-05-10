package com.pulamsi.gooddetail;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseFragment;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.myinfo.order.entity.Product;
import com.squareup.otto.Subscribe;

/**
 * 图文详情界面
 */
public class GoodDetailGoodImageDetailFragment extends BaseFragment {

    /** 图文详情 WebView */
    private WebView mDescWebView;

    /** 产品参数 WebView */
    private WebView mProductParamWebView;

    /** 售后保障 WebView */
    private WebView mshowHelpWebView;

    /** 产品参数 WebView 已经加载过 */
    private boolean isProductParamWebViewLoad;

    /** FAQ WebView 已经加载过 */
    private boolean isshowHelpWebViewLoad;

    /**
     * 商品对象
     */
    private Product product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.good_detail_good_image_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDescWebView = (WebView) view.findViewById(R.id.wv_good_detail_desc_webview);
        mProductParamWebView = (WebView) view.findViewById(R.id.wv_good_detail_product_param_webview);
        mshowHelpWebView = (WebView) view.findViewById(R.id.wv_good_detail_showhelp_faq_webview);
        String mDescurl = getString(R.string.serverbaseurl)+ getString(R.string.showIntroduction)+ "?sn=" + product.getSn();
        initWebView(mDescWebView, mDescurl);
    }

    //把商品信息传递进来，给按钮
    public void setData(Product product){
        if (null == product){
            return;
        }
        this.product = product;
    }

    @Override
    public void onResume() {
        BusProvider.getInstance().register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }

    private void initWebView(WebView webView, String originalUrl) {
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDefaultTextEncodingName("utf-8");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 打开 https 页面时接受证书
                handler.proceed();
            }
        });
        if (originalUrl.startsWith("http") || originalUrl.startsWith("https")) {
            // url是http或https链接
            webView.loadUrl(originalUrl);
        }
    }


    @Subscribe
    public void onNotifyImgDetailChangeWebViewEvent(GoodDetailImgDetailTabFragment.NotifyImgDetailChangeWebViewEvent event) {
        int tabPos = event.tabPos;
        if (tabPos == 0) {
            mDescWebView.setVisibility(View.VISIBLE);
            mProductParamWebView.setVisibility(View.GONE);
            mshowHelpWebView.setVisibility(View.GONE);
        } else if (tabPos == 1) {
            if (!isProductParamWebViewLoad && product != null) {
                String productParamurl = getString(R.string.serverbaseurl)+ getString(R.string.showParameter)+ "?sn=" + product.getSn();
                initWebView(mProductParamWebView, productParamurl);
                isProductParamWebViewLoad = true;
            }

            mProductParamWebView.setVisibility(View.VISIBLE);
            mDescWebView.setVisibility(View.GONE);
            mshowHelpWebView.setVisibility(View.GONE);
        } else if (tabPos == 2) {
            if (!isshowHelpWebViewLoad && product != null) {
                String showHelpurl = getString(R.string.serverbaseurl)+ getString(R.string.showHelp);
                initWebView(mshowHelpWebView, showHelpurl);
                isshowHelpWebViewLoad = true;
            }

            mshowHelpWebView.setVisibility(View.VISIBLE);
            mProductParamWebView.setVisibility(View.GONE);
            mDescWebView.setVisibility(View.GONE);
        }
    }

}

