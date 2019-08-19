/**
 * Copyright (C), 1995-2019, DUSKY
 * FileName: SetActivity
 * Author: dusky
 * Date: 2019/1/6 19:36
 * Description: 对app进行各种设置
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.dusky.world.Module.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dusky.world.Base.BaseActivity;
import com.dusky.world.R;
import com.dusky.world.Web.X5WebView;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.net.URL;

import butterknife.BindView;

/**
 * @ClassName: ArticleActivity
 * @Description: 文章详情页面
 * @Author: dusky
 * @Date: 2019/7/8 12:25
 */
public class ArticleActivity extends BaseActivity {
    @BindView(R.id.webview)
    X5WebView webview;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar1)
    ProgressBar mPageLoadingProgressBar;

    private URL mIntentUrl;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_article;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        initWebView();
    }

    public void initWebView() {

//创建一个WebViewClient
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                super.onReceivedError(view, request, error);

            }
        });

        webview.setWebChromeClient(new WebChromeClient(){

        });
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    mPageLoadingProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    mPageLoadingProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mPageLoadingProgressBar.setProgress(newProgress);//设置进度值
                }

            }
        });
        WebSettings webSetting = webview.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);//如果是true，在x5加载失败启用系统内核时就会闪退，解决方法在下面finish覆写
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        if (mIntentUrl == null) {
            webview.loadUrl("");
        } else {
            webview.loadUrl(mIntentUrl.toString());
        }
        webview.setBackgroundColor(0); // 设置背景色
        webview.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    //进行销毁
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    /**
     * 防止webview缩放时退出崩溃。
     */
    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }
    //处理网页中back键返回逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview != null && webview.canGoBack()) {
                webview.goBack();
                return true;
            } else
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
