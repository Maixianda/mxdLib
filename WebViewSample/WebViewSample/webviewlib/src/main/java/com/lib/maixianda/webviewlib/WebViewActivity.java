package com.lib.maixianda.webviewlib;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lib.maixianda.webviewlib.view.LoadingLayout;


/**
 * Created by maixianda on 16-6-12.
 * 包含webview的activity
 * 使用静态方法 openWebActivity(Activity activity, String mTitle, String url)打开这个activity
 * 如果访问出现http错误码则显示LoadingLayout,否则隐藏LoadingLayout
 *
 *
 */
public class WebViewActivity extends BaseWebViewActivity {

    //region 成员变量
    private static final String WEB_TITLE = "title";
    private static final String WEB_URL = "url";

    /**
     * 要加载的url
     */
    private String url;
    /**
     * webview的标题
     */
    private String mTitle;
    private ImageView iv_closeWebView;

    private WebView idWebView;
    private LoadingLayout idWebLoadLayout;

    public boolean isLoading;
    private ProgressBar mProgressBar;
    //endregion 成员变量

    // TODO: 2016/6/13 10:39 添加一个接口,用于把该activity加入到activity管理器中

    /**
     * 使用webview打开一个网页
     * @param activity 从哪个activity打开这个webviewactivity,需要用这个参数调用startActivity
     * @param title 标题
     * @param url 要打开的网页的url
     */
    public static void openWebActivity(Activity activity, String title, String url) {
        Intent intent = new Intent(activity,WebViewActivity.class);
        intent.putExtra(WebViewActivity.WEB_TITLE,title);
        intent.putExtra(WebViewActivity.WEB_URL,url);
        activity.startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(WEB_TITLE, mTitle);
        outState.putString(WEB_URL,url);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (null == savedInstanceState)
        {
            url = intent.getStringExtra(WEB_URL);
            mTitle = intent.getStringExtra(WEB_TITLE);
        }
        else
        {
            url = savedInstanceState.getString(WEB_URL);
            mTitle = savedInstanceState.getString(WEB_TITLE);
        }

        if (null== mTitle)
        {
            mTitle ="";
        }
        if (null == url)
        {
            // TODO: 2016/6/13 11:07 需要设置一个默认的错误页面
            url="";
        }
    }

    /**
     * 通过findViewByID绑定控件
     */
    protected void bindCtrl() {
        super.bindCtrl();
        iv_closeWebView = (ImageView)findViewById(R.id.iv_back);
        idWebView = (WebView) findViewById(R.id.id_web_webView);
        idWebLoadLayout = (LoadingLayout) findViewById(R.id.id_web_loadLayout);
    }

    @Override
    protected void initTopView(String title) {
        super.initTopView(title);

        //region 设置progressBar
        mProgressBar = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,10);
        rlParams.addRule(RelativeLayout.BELOW,R.id.ll_topViewWithoutProgressBar);
        mProgressBar.setLayoutParams(rlParams);

        Drawable drawable = this.getResources().getDrawable(R.drawable.progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);

        ((RelativeLayout)findViewById(R.id.rl_topview)).addView(mProgressBar);
        //endregion 设置progressBar
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(idWebView.canGoBack())
        {
            idWebView.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void initView(Object o, Bundle savedInstanceState) {
        //初始化头部视图
        initTopView(mTitle);
        //region 初始化webView

        iv_closeWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置不显示浏览器滚动栏
        idWebView.setVerticalScrollBarEnabled(false);
        //设置启用javascript
        idWebView.getSettings().setJavaScriptEnabled(true);
        //设置优先从缓存中读取
        if (Build.VERSION.SDK_INT >= 19) {
            idWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        //设置webview的响应
        idWebView.setWebViewClient(new WebViewClient(){

            public boolean isSuccess;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isLoading = false;
                if (null!=idWebLoadLayout&&isSuccess)
                {
                    idWebLoadLayout.setLoadSucess(idWebView);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isLoading = true;
                isSuccess = true;
                if (null !=idWebLoadLayout)
                    idWebLoadLayout.setLoadStart();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isSuccess = false;
                isLoading = false;
                if (null!=idWebLoadLayout )
                {
                    idWebLoadLayout.setLoadFailure(getString(R.string.webViewAccessFailed));
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                isSuccess = false;
                if (null != idWebLoadLayout)
                {
                    idWebLoadLayout.setLoadFailure(getString(R.string.webViewAccessFailed));
                }
            }
        });

        //设置webview的chromeclient
        idWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitle = title;
                setTitle(mTitle);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (100 == newProgress)
                {
                    mProgressBar.setVisibility(View.GONE);
                }
                else
                {
                    if (mProgressBar.getVisibility() == View.GONE)
                    {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                }
                mProgressBar.setProgress(newProgress);
            }
        });
        //把webview绑定到idWebLoadLayout,从而让idWebLoadLayout在加载页面出错的时候能够隐藏webViw
        idWebLoadLayout.setWebView(idWebView);
        //设定出错的时候,显示的View里面的重试按钮能够重新加载页面
        idWebLoadLayout.setBtnRetry(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idWebView.loadUrl(url);
            }
        });
        //endregion 初始化webView
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        idWebView.loadUrl(url);
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_web;
    }
}
