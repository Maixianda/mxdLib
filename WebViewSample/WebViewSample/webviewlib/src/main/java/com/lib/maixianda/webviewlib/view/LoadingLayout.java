package com.lib.maixianda.webviewlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lib.maixianda.webviewlib.R;

/**
 * Created by maixianda on 16-6-12.
 */
public class LoadingLayout extends FrameLayout {
    private WebView webView;
    private Button mRetry;
    private TextView mTextView;
    public LoadingLayout(Context context) {
        this(context,null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //webView = (WebView) ((View)(getParent())).findViewById(R.id.id_web_webView);

        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.view_web_fail_view,this);
        mRetry = (Button) view.findViewById(R.id.btn_retry);
        mTextView = (TextView) view.findViewById(R.id.tv_msg);
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public void setLoadSucess(WebView idWebView) {
        idWebView.setVisibility(View.VISIBLE);
        this.setVisibility(View.GONE);
    }

    public void setLoadStart() {
        this.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
    }

    public void setLoadFailure(String s) {
        webView.setVisibility(View.GONE);
        this.setVisibility(View.VISIBLE);
        mTextView.setText(s);
    }

    public void setBtnRetry(OnClickListener onClickListener) {
        mRetry.setOnClickListener(onClickListener);
    }
}
