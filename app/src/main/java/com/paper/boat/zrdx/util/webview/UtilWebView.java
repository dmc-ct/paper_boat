package com.paper.boat.zrdx.util.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.paper.boat.zrdx.InitApp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UtilWebView {

    @SuppressLint("SetJavaScriptEnabled")
    public static void Init(WebView contentWebView){
        /*滚动条*/
        contentWebView.setHorizontalScrollBarEnabled( false );//水平不显示
        contentWebView.setVerticalScrollBarEnabled( false ); //垂直不显示
        /*设置Java脚本已启用*/
        contentWebView.getSettings().setJavaScriptEnabled( true );
        /*设置App Cache Enabled*/
        contentWebView.getSettings().setAppCacheEnabled( true );
        /*设置数据库已启用*/
        contentWebView.getSettings().setDatabaseEnabled( true );
        /*设置Dom存储已启用*/
        contentWebView.getSettings().setDomStorageEnabled( true );
        // 允许文件访问
        contentWebView.getSettings().setAllowFileAccess( true );
        // 允许网页定位
        contentWebView.getSettings().setGeolocationEnabled( true );
        // 允许保存密码
        contentWebView.getSettings().setSavePassword( true );
        // 允许网页弹对话框
        contentWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically( true );
        // 加快网页加载完成的速度，等页面完成再加载图片
        contentWebView.getSettings().setLoadsImagesAutomatically( true );
        // 本地 DOM 存储（解决加载某些网页出现白板现象）
        contentWebView.getSettings().setDomStorageEnabled( true );
        /*设置使用广角端口*/
        contentWebView.getSettings().setUseWideViewPort(true);
        /*设置支持缩放*/
        contentWebView.getSettings().setSupportZoom(true);
        /*设置加载总览模式*/
        contentWebView.getSettings().setLoadWithOverviewMode(true);
        /*不加载缓存内容*/
        contentWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 解决 Android 5.0 上 WebView 默认不允许加载 Http 与 Https 混合内容
            contentWebView.getSettings().setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public static void HtmlInit(Context context, WebView contentWebView, String html, ProgressBar mProgressBar) {
        /*初始化*/
        Init( contentWebView );
        /*图片优化*/
        Document doc = Jsoup.parse( html );
        Elements elements = doc.getElementsByTag( "img" ); /*通过标签获取Elements*/
        for (Element element : elements) {
            element.attr( "width", "100%" ).attr( "height", "auto" );
            element.attr( "style", "max-width:100%;height:auto" );
        }
        /*链接访问，或h5文本展示*/
        String str = doc.html();
        int a = str.indexOf( "<body>" );
        String str2 = str.substring( a + 9, str.length() );
        if (str2.startsWith( "<a" )) {
            int b = str2.indexOf( "href=\"" );
            int c = str2.indexOf( "</a>" );
            contentWebView.loadUrl( str2.substring( b + 6, c ) );
        } else if (html.startsWith( "http" )) {
            contentWebView.loadUrl( html );
        } else {
            contentWebView.loadDataWithBaseURL( null, doc.toString(), "text/html", "utf-8", null );
        }
        /*添加Javascript接口*/
        contentWebView.addJavascriptInterface( new MJavascriptInterface( context
                , StringUtils.returnImageUrlsFromHtml( doc.toString() ) ), "imagelistener" );
        /*设置Web View Client*/
        contentWebView.setWebViewClient( new MyWebViewClient( mProgressBar ) );
        /*加载进度条*/
        contentWebView.setWebChromeClient( new MyWebChromeClient( mProgressBar ) );
    }


    public static class MyWebChromeClient extends WebChromeClient {
        ProgressBar mProgressBar;

        public MyWebChromeClient(ProgressBar progressBar) {
            this.mProgressBar = progressBar;
        }

        /**
         * 收到加载进度变化
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            InitApp.getHandler().post( () -> mProgressBar.setProgress( newProgress ) );
        }
    }
}
