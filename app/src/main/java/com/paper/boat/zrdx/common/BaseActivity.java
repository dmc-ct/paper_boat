package com.paper.boat.zrdx.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paper.boat.zrdx.annotation.AutoArg;
import com.paper.boat.zrdx.util.base.GsonUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final Handler HANDLER = new Handler( Looper.getMainLooper() );
    public final Object mHandlerToken = hashCode();
    private Bundle mBundle = new Bundle();  /*跳转意图*/
    private Intent mIntent; /*跳转*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        initActivity();
    }

    protected void initActivity() {
        initLayout();
        initView();
        initData();
    }

    /**
     * 初始化布局
     */
    protected void initLayout() {
        if (getLayoutId() > 0) {
            setContentView( getLayoutId() );
            initSoftKeyboard();
        }
    }

    /**
     * 初始化软键盘
     */
    protected void initSoftKeyboard() {
        // 点击外部隐藏软键盘，提升用户体验
        getContentView().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        } );
    }

    /**
     * 获取布局 ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    public void finish() {
        hideSoftKeyboard();
        super.finish();
    }

    /**
     * 延迟执行
     */
    public final boolean post(Runnable r) {
        return postDelayed( r, 0 );
    }

    /**
     * 延迟一段时间执行
     */
    public final boolean postDelayed(Runnable r, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return postAtTime( r, SystemClock.uptimeMillis() + delayMillis );
    }

    /**
     * 在指定的时间执行
     */
    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        // 发送和这个 Activity 相关的消息回调
        return HANDLER.postAtTime( r, mHandlerToken, uptimeMillis );
    }

    @Override
    protected void onDestroy() {
        // 移除和这个 Activity 相关的消息回调
        HANDLER.removeCallbacksAndMessages( mHandlerToken );
        super.onDestroy();
    }

    /**
     * 如果当前的 Activity（singleTop 启动模式） 被复用时会回调
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent( intent );
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent( intent );
    }

    /**
     * 获取当前 Activity 对象
     */
    public BaseActivity getActivity() {
        return this;
    }

    /**
     * 和 setContentView 对应的方法
     */
    public ViewGroup getContentView() {
        return findViewById( Window.ID_ANDROID_CONTENT );
    }

    /**
     * startActivity 方法优化
     */
    public BaseActivity startActivity(Class cls) {
        mIntent  = new Intent( this, cls );
        return this;
    }

    public BaseActivity withObject(@Nullable String key, @Nullable Object value) {
        String json = GsonUtils.toJson( value );
        mBundle.putString( key, json );
        return this;
    }

    public BaseActivity withString(@Nullable String key, @Nullable String value) {
        mBundle.putString( key, value );
        return this;
    }

    public BaseActivity withDouble(@Nullable String key, @Nullable Double value) {
        mBundle.putDouble( key, value );
        return this;
    }

    public BaseActivity withBoolean(@Nullable String key, boolean value) {
        mBundle.putBoolean( key, value );
        return this;
    }

    public BaseActivity withInt(@Nullable String key, int value) {
        mBundle.putInt( key, value );
        return this;
    }

    public BaseActivity withFloat(@Nullable String key, float value) {
        mBundle.putFloat( key, value );
        return this;
    }

    public BaseActivity withIntegerArrayList(@Nullable String key, @Nullable ArrayList <Integer> value) {
        mBundle.putIntegerArrayList( key, value );
        return this;
    }

    public BaseActivity withStringArrayList(@Nullable String key, @Nullable ArrayList <String> value) {
        mBundle.putStringArrayList( key, value );
        return this;
    }

    public void goForResult(int requestCode) {
        if (mIntent != null) {
            mIntent.putExtras( mBundle );
            startActivityForResult( mIntent, requestCode );
        }
    }

    public void goSetResult(int resultCode) {
        mIntent = new Intent();
        mIntent.putExtras( mBundle );
        setResult( resultCode, mIntent );
        finish();
    }

    public void go() {
        if (mIntent != null) {
            mIntent.putExtras( mBundle );
            startActivity( mIntent );
        }
    }

    public void startActivityFinish(Class cls) {
        startActivityFinish( new Intent( this, cls ) );
    }

    public void startActivityFinish(Intent intent) {
        startActivity( intent );
        finish();
    }

    /**
     * startActivityForResult 方法优化
     */

    private ActivityCallback mActivityCallback;
    private int mActivityRequestCode;

    public void startActivityForResult(Class <? extends Activity> cls, ActivityCallback callback) {
        startActivityForResult( new Intent( this, cls ), null, callback );
    }

    public void startActivityForResult(Intent intent, ActivityCallback callback) {
        startActivityForResult( intent, null, callback );
    }

    public void startActivityForResult(Intent intent, @Nullable Bundle options, ActivityCallback callback) {
        // 回调还没有结束，所以不能再次调用此方法，这个方法只适合一对一回调，其他需求请使用原生的方法实现
        if (mActivityCallback == null) {
            mActivityCallback = callback;
            // 随机生成请求码，这个请求码在 0 - 255 之间
            mActivityRequestCode = new Random().nextInt( 255 );
            startActivityForResult( intent, mActivityRequestCode, options );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mActivityCallback != null && mActivityRequestCode == requestCode) {
            mActivityCallback.onActivityResult( resultCode, data );
            mActivityCallback = null;
        } else {
            super.onActivityResult( requestCode, resultCode, data );
        }
    }

    /**
     * 处理 Activity 多重跳转：https://www.jianshu.com/p/579f1f118161
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (startActivitySelfCheck( intent )) {
            hideSoftKeyboard();
            // 查看源码得知 startActivity 最终也会调用 startActivityForResult
            super.startActivityForResult( intent, requestCode, options );
        }
    }

    private String mStartActivityTag;
    private long mStartActivityTime;

    /**
     * 检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可
     *
     * @param intent 用于跳转的 Intent 对象
     * @return 检查通过返回true, 检查不通过返回false
     */
    protected boolean startActivitySelfCheck(Intent intent) {
        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) {
            // 显式跳转
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) {
            // 隐式跳转
            tag = intent.getAction();
        } else {
            // 其他方式
            return true;
        }

        if (tag.equals( mStartActivityTag ) && mStartActivityTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mStartActivityTag = tag;
        mStartActivityTime = SystemClock.uptimeMillis();
        return result;
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyboard() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
            if (manager != null) {
                manager.hideSoftInputFromWindow( view.getWindowToken(), 0 );
            }
        }
    }

    /**
     * Activity 回调接口
     */
    public interface ActivityCallback {

        /**
         * 结果回调
         *
         * @param resultCode 结果码
         * @param data       数据
         */
        void onActivityResult(int resultCode, @Nullable Intent data);
    }

    /**
     * TODO Roadmap
     * <p>
     * 1. ARoute 跳转
     * 2. Fragment 替换操作支持 ARoute uri地址
     */
    private void injectBundle(Bundle bundle) {
        if (bundle != null) {
            injectBundle( this, bundle );
        }
    }

    private void injectBundle(Object o, Bundle bundle) {
        try {
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean annotationPresent = field.isAnnotationPresent( AutoArg.class );
                if (annotationPresent) {
                    field.setAccessible( true );

                    Object value = bundle.get( field.getName() );
                    if (value instanceof String) {
                        String str = (String) value;
                        try {
                            Object obj = GsonUtils.fromJson( str, field.getType() );

                            field.set( o, obj );
                        } catch (Exception e) {
                            field.set( o, str );
                        }

                    } else {
                        field.set( o, value );
                    }

                }
            }
        } catch (Exception e) {
            Log.e( "injectBundleException", e.getMessage() );
        }
    }
}