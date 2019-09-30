package com.paper.boat.zrdx.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.paper.boat.zrdx.InitApp;
import com.paper.boat.zrdx.Url;
import com.paper.boat.zrdx.util.base.MyToast;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MyRetrofit {
    private static MyRetrofit myRetrofit;
    private static BlogService mBlogService;
    //是否设置缓存
    private static boolean Cache = true;

    public static BlogService getRetrofit() {

        if (myRetrofit == null) {
            myRetrofit = new MyRetrofit();
            //初始化网络请求
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure( true ) //链接失败时重试
                    .addInterceptor( sLoggingInterceptor )  //添加拦截器
                    .connectTimeout( 20, TimeUnit.SECONDS ) //链接超时
                    .readTimeout( 20, TimeUnit.SECONDS )  //设置读取超时
//                    .addInterceptor( new AddCookiesInterceptor( InitApp.getContext() ) ) //cookies
//                    .addInterceptor( new SaveCookiesInterceptor( InitApp.getContext() ) )
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client( okHttpClient )
                    .baseUrl( Url.URL )
                    .addConverterFactory( ScalarsConverterFactory.create() ) //标准类型
                    .addConverterFactory( GsonConverterFactory.create() ) //支持Gson解析
                    .addConverterFactory( FastJsonConverterFactory.create() )//阿里Gson解析
                    .addCallAdapterFactory( RxJavaCallAdapterFactory.create() ) //异步拓展
                    .client( GetCache() ) //客户机缓存
                    .build();
            mBlogService = retrofit.create( BlogService.class );
        }
        return mBlogService;
    }

    /**
     * Retrofit缓存适用于(Get请求)
     */
    private static OkHttpClient GetCache() {
        //设置缓存路径 内置存储
        //File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        //外部存储
        File httpCacheDirectory = new File( InitApp.getContext().getExternalCacheDir(), "responses" );
        //设置缓存 10M
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache( httpCacheDirectory, cacheSize );
        //日志拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel( HttpLoggingInterceptor.Level.BODY );
        /**
         * 获取缓存
         */
        Interceptor baseInterceptor = new Interceptor() {
            private String TAG = "dmc";

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!isNetWorkAvailable( InitApp.getContext() )) {
                    /**
                     * 离线缓存控制  总的缓存时间=在线缓存时间+设置离线缓存时间
                     */
                    int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周,单位:秒
                    CacheControl tempCacheControl = new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale( maxStale, TimeUnit.SECONDS )
                            .build();
                    request = request.newBuilder()
                            .cacheControl( tempCacheControl )
                            .build();
                    Log.i( TAG, "intercept:无网络" );
                }
                return chain.proceed( request );
            }
        };
        //只有 网络拦截器环节 才会写入缓存写入缓存,在有网络的时候 设置缓存时间
        Interceptor rewriteCacheControlInterceptor = chain -> {
            Request request = chain.request();
            Response originalResponse = chain.proceed( request );
            int maxAge = 60; // 在线缓存在1分钟内可读取 单位:秒
            return originalResponse.newBuilder()
                    .removeHeader( "Pragma" )// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .removeHeader( "Cache-Control" )
                    .header( "Cache-Control", "public, max-age=" + maxAge )
                    .build();
        };

        OkHttpClient client;
        //如果默认为 缓存数据
        if (Cache) { //设置缓存
            client = new OkHttpClient.Builder()
                    .cache( cache ) //缓存
                    .addInterceptor( logging )
                    .addInterceptor( baseInterceptor )
                    .retryOnConnectionFailure( true ) //链接失败时重试
                    .addInterceptor( new EnhancedCacheInterceptor() ) //Post缓存
                    .addNetworkInterceptor( rewriteCacheControlInterceptor ) //添加网络拦截器
                    .connectTimeout( 15, TimeUnit.SECONDS )
                    .build();
        } else {
            client = new OkHttpClient.Builder()
                    .addInterceptor( logging ) //添加拦截器
                    .connectTimeout( 15, TimeUnit.SECONDS )
                    .build();
        }
        return client;
    }

    /**
     * 打印返回的json数据拦截器
     */
    private static final Interceptor sLoggingInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            Buffer requestBuffer = new Buffer();
            if (request.body() != null) {
                request.body().writeTo( requestBuffer );
            }
            Response response = chain.proceed( request );
            assert response.body() != null;
            String content = response.body().string();

            String urlStr = (request.body() != null ? _parseParams( request.body(), requestBuffer ) : "");
            //地址 + 参数
            MyToast.printLog( "请求地址：    " + request.url() + "?" + urlStr );
            //内容
            MyToast.printLog( "返回结果：    " + content );
            //类型 application/json;  charset=utf-8;
            MediaType mediaType = response.body().contentType();
            return response.newBuilder()
                    .body( ResponseBody.create( mediaType, content ) )
                    .build();
        }
    };

    /**
     * 解析参数
     */
    @NonNull
    private static String _parseParams(RequestBody body, Buffer requestBuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !Objects.requireNonNull( body.contentType() ).toString().contains( "multipart" )) {
            return URLDecoder.decode( requestBuffer.readUtf8(), "UTF-8" );
        }
        return "null";
    }

    /**
     * 检查网络是否已连接
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService( Activity.CONNECTIVITY_SERVICE );
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

}
