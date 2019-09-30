package com.paper.boat.zrdx.network;

import android.text.TextUtils;
import android.util.Log;

import com.paper.boat.zrdx.InitApp;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.paper.boat.zrdx.network.Result.SUCCESSED;

public abstract class Converter<T extends Result> implements Callback <T> {
    // 是否使用缓存 默认开启
    private boolean mUseCache = true;
    private Class dataClassName;
    private Call<T> mCall;

    /**
     * Gson反序列化缓存时 需要获取到泛型的class类型
     */
    public Converter <T> dataClassName(Class className) {
        dataClassName = className;
        return this;
    }

    @Override
    public void onResponse(Call <T> call, Response <T> response) {
        try {
            T t = response.body();
            assert t != null;
            if (t.code == SUCCESSED) {
                onSuccess( t );
            } else {
                onError( t.message, response );
            }
        } catch (Exception e) {
            onError( "返回数据异常请联系管理员处理", response );
        }

    }

    @Override
    public void onFailure(Call <T> call, Throwable throwable) {
        if (!mUseCache || MyRetrofit.isNetWorkAvailable( InitApp.getContext() )) {
            //不使用缓存 或者网络可用 的情况下直接回调onFailure
            onError( "请求异常请联系管理员处理", null );
            return;
        }
        Request request = call.request();
        String url = request.url().toString();
        RequestBody requestBody = request.body();
        Charset charset = Charset.forName( "UTF-8" );
        StringBuilder sb = new StringBuilder();
        sb.append( url );
        if (request.method().equals( "POST" )) {
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset( Charset.forName( "UTF-8" ) );
            }
            Buffer buffer = new Buffer();
            try {
                requestBody.writeTo( buffer );
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append( buffer.readString( charset ) );
            buffer.close();
        }

        String cache = CacheManager.getInstance().getCache( sb.toString() );
        Log.d( CacheManager.TAG, "get cache->" + cache );

        if (!TextUtils.isEmpty( cache ) && dataClassName != null) {
            Object obj = new Gson().fromJson( cache, dataClassName );
            if (obj != null) {
                onSuccess( (T) obj );
                return;
            }
        }
        onError( "请求异常请联系管理员处理", null );
    }

    public abstract void onSuccess(T t);

    public abstract void onError(String err, Response <T> resp);
}
