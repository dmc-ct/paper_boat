package com.paper.boat.zrdx.util.webview;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by Administrator on 2017/2/10.
 */

public class MyWebViewClient extends WebViewClient {
    ProgressBar mProgressBar;

    public MyWebViewClient(ProgressBar progressBar){
        this.mProgressBar = progressBar;
    }

    /**
     * 完成加载网页
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        view.getSettings().setJavaScriptEnabled(true);
        super.onPageFinished(view, url);
        addImageClickListener(view);//待网页加载完全后设置图片点击的监听方法
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * 开始加载网页
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        view.getSettings().setJavaScriptEnabled(true);
        super.onPageStarted(view, url, favicon);
        mProgressBar.setVisibility( View.VISIBLE);
    }

    private void addImageClickListener(WebView webView) {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                "    }  " +
                "}" +
                "})()");
    }
}
