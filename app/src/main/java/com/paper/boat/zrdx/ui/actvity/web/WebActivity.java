package com.paper.boat.zrdx.ui.actvity.web;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.util.HandlerTools;
import com.paper.boat.zrdx.util.interfaces.XRefresh;
import com.paper.boat.zrdx.util.webview.UtilWebView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/*project*/
public class WebActivity extends MyActivity {
        @BindView(R.id.webView)
        WebView webView;
        @BindView(R.id.pb_web_progress)
        ProgressBar mProgressBar;
        @BindView(R.id.refreshLayout)
        SmartRefreshLayout refreshLayout;
        @SuppressLint("StaticFieldLeak")
        public static View customView;
    private NoAdWebViewClient noAdWebViewClient;

    @Override
    public void initView() {
        HandlerTools.Refresh( false, refreshLayout, new XRefresh() {
            @Override
            public void onRefresh() {
                webView.reload();
            }

            @Override
            public void onLoadMore() {

            }
        } );
    }

    /*停止*/
    @Override
    protected void onStop() {
        super.onStop();
        webView.reload();
    }

    @Override
    public void initData() {
        /*初始化*/
        UtilWebView.Init( webView );
        //加载需要显示的网页
        initWebView();
    }

    /**
     * 展示网页界面
     **/
    public void initWebView() {
        noAdWebViewClient = new NoAdWebViewClient( getBaseContext(), "http://m.bumimi66.com", mProgressBar, WebActivity.this, webView );
        webView.setWebViewClient( noAdWebViewClient );
        // 加载Web地址
        webView.loadUrl( "http://m.bumimi66.com" );
    }

    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    /*
    * mWebView.goBack(); //后退
      mWebView.goForward();//前进
      mWebView.reload(); //刷新*/
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {/* 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
            if (customView != null) {
                noAdWebViewClient.hideCustomView();
            } else if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyUp( keyCode, event );
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }
}
