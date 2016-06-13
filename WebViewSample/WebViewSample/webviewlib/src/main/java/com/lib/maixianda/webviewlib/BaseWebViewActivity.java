package com.lib.maixianda.webviewlib;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by maixianda on 16-6-12.
 * webActivity的基类,继承自AppCompatActivity
 */
public abstract class BaseWebViewActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayoutId());

        bindCtrl();
        initData(savedInstanceState);
        initView(null,savedInstanceState);
        afterCreate(savedInstanceState);
    }

    //region 要子类实现的方法

    /**
     * 父类中onCreate中setContentView后第二个调用
     * 使用savedInstanceState初始化数据
     * @param savedInstanceState
     */
    public abstract void initData(Bundle savedInstanceState);

    /**
     * 父类中onCreate中setContentView后第三个调用
     * 用于初始化视图
     * @param o
     * @param savedInstanceState
     */
    protected abstract void initView(Object o, Bundle savedInstanceState);

    /**
     * 父类中onCreate中setContentView后最后一个调用
     * @param savedInstanceState
     */
    protected abstract void afterCreate(Bundle savedInstanceState);

    /**
     * 获取setContentView的参数(R.Layout.XXX)
     * 由子类实现
     * @return 返回布局ID
     */
    public abstract int getContentViewLayoutId();
    //endregion

    /**
     * 设置webView头部的title
     * @param title
     */
    protected void setTitle(String title)
    {
        if (title!=null) {
            ((TextView) findViewById(R.id.tv_title)).setText(title);
        }
    }
    /**
     * 初始化头部视图,这里暂时先只设置头部视图中的title
     * @param title
     */
    protected void initTopView(String title) {
        // TODO: 16-6-12 扩展: 可以扩展设置头部视图的皮肤,设置回退的图片

        setTitle(title);

        //findViewById(R.id.title_blackBtn).setOnClickListener(finishListener());
        //((TextView) findViewById(R.id.title_titleTx)).setText(title);
    }


    /**
     * 父类中onCreate中setContentView后第一个调用
     * 使用findViewById绑定控件
     */
    protected void bindCtrl() {
        // TODO: 2016/6/13 16:01  扩展:以后扩展下吧顶部视图的控件绑定在这个类中
    }
}
