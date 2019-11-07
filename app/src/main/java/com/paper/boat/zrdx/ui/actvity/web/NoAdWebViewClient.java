package com.paper.boat.zrdx.ui.actvity.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.paper.boat.zrdx.InitApp;
import com.paper.boat.zrdx.util.base.MyToast;

/**
 * Created by BrainWang on 05/01/2016.
 */
public class NoAdWebViewClient extends WebViewClient {
    private String homeurl;
    private Context context;
    private ProgressBar mProgressBar;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private Activity mActivity;
    private WebView mWebView;

    /** 视频全屏参数 */
    private static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
            new FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    NoAdWebViewClient(Context context, String homeurl, ProgressBar progressBar
            , Activity activity, WebView webView) {
        this.context = context;
        this.homeurl = homeurl;
        this.mProgressBar = progressBar;
        this.mActivity = activity;
        this.mWebView = webView;
    }

    /*广告过滤*/
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        url = url.toLowerCase();
        /*包含*/
        if (!url.contains( homeurl )) {
            if (!ADFilterTool.hasAd( context, url )) {
                MyToast.printLog( url );
                return super.shouldInterceptRequest( view, url );
            } else {
                return new WebResourceResponse( null, null, null );
            }
        } else {
            return super.shouldInterceptRequest( view, url );
        }
    }

    /*应该覆盖网址加载*/
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        mWebView.loadUrl( url );
        return true;
    }

    /**
     * 完成加载网页
     */
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onPageFinished(WebView view, String url) {
        view.getSettings().setJavaScriptEnabled( true );
        super.onPageFinished( view, url );
        mProgressBar.setVisibility( View.GONE );
    }

    /**
     * 开始加载网页
     */
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        view.getSettings().setJavaScriptEnabled( true );
        super.onPageStarted( view, url, favicon );
        mProgressBar.setVisibility( View.VISIBLE );
        /*加载进度条*/
        view.setWebChromeClient( new MyWebChromeClient( mProgressBar, context ) );
        super.shouldInterceptRequest( view, url );
    }


    public class MyWebChromeClient extends WebChromeClient {
        private ProgressBar mProgressBar;
        private Context mContext;

        MyWebChromeClient(ProgressBar progressBar, Context context) {
            this.mProgressBar = progressBar;
            this.mContext = context;
        }

        /**
         * 收到加载进度变化
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            InitApp.getHandler().post( () -> mProgressBar.setProgress( newProgress ) );
        }


        /*** 视频播放相关的方法 **/
        @Override
        public View getVideoLoadingProgressView() {
            FrameLayout frameLayout = new FrameLayout( mContext );
            frameLayout.setLayoutParams( new WindowManager.LayoutParams( WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT ) );
            return frameLayout;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            showCustomView( view, callback );
        }

        @Override
        public void onHideCustomView() {
            hideCustomView();
        }

    }

    /** 视频播放全屏 **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (WebActivity.customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        mActivity.getWindow().getDecorView();

        mActivity.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏

        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        fullscreenContainer =  new FullscreenHolder( mActivity);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        WebActivity.customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super( ctx );
            setBackgroundColor( ctx.getResources().getColor( android.R.color.black ) );
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    /*设置状态栏可见性*/
    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        mActivity.getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /** 隐藏视频全屏 */
    void hideCustomView() {
        if (WebActivity.customView == null) {
            return;
        }
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        mActivity.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        fullscreenContainer = null;
        WebActivity.customView = null;
        customViewCallback.onCustomViewHidden();
        mWebView.setVisibility(View.VISIBLE);
    }

}