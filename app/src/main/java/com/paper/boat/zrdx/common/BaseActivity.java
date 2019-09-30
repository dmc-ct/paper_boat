package com.paper.boat.zrdx.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.liulishuo.filedownloader.FileDownloader;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.InitApp;
import com.paper.boat.zrdx.annotation.AutoArg;
import com.paper.boat.zrdx.util.base.GsonUtils;
import com.paper.boat.zrdx.util.base.MyToast;
import com.paper.boat.zrdx.util.interfaces.OnLeft;
import com.paper.boat.zrdx.util.interfaces.OnRight;
import com.paper.boat.zrdx.util.interfaces.OnTitle;
import com.paper.boat.zrdx.view.dialog.StateNoticeDialog;
import com.zl.weilu.saber.api.Saber;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity extends SupportActivity {
    private static final String TAG = "BaseActivity";
    private Unbinder unbind;
    private Bundle mBundle = new Bundle();
    private Intent mIntent;
    /* 状态栏沉浸*/
    protected ImmersionBar mImmersionBar;
    /* 标题栏对象*/
    private TitleBar mTitleBar;
    private StateNoticeDialog.Builder mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        /*初始化布局*/
        initLayout();
        unbind = ButterKnife.bind( this );
        InitApp.getInstance().addActivity( this );
        Saber.bind( this ); // <--这里绑定ViewModel
        /*Dialog*/
        mDialog = new StateNoticeDialog.Builder( getBaseContext() );
        /*状态栏初始化*/
        ImmersionBar.with( this ).init();
        /* 仅仅是缓存Application的Context，不耗时*/
        FileDownloader.init( getBaseContext() );
        //文件下载初始化
        FileDownloader.setup( getBaseContext() );
        //初始化页面
        initView();
        //初始化数据
        initData();
        /*状态栏*/
        initImmersion();
    }

    private void initLayout() {
        if (getLayout() > 0) {
            setContentView( getLayout() );
            initSoftKeyboard();
            mTitleBar = findViewById( R.id.title );
            leftBar( this::finish );
        }
    }

    public void dialog(String string) {
        mDialog.showNotice( string, 3, false, 0 );
    }

    public void onDismiss() {
        mDialog.onDismiss();
    }

    /**
     * 是否使用沉浸式状态栏
     */
    public boolean isStatusBarEnabled() {
        return true;
    }

    public void titleBar(OnTitle onTitle) {
        if (mTitleBar != null) {
            /*标题栏监听*/
            mTitleBar.setOnTitleBarListener( new OnTitleBarListener() {
                @Override
                public void onLeftClick(View v) {
                    onTitle.Left();
                }
                @Override
                public void onTitleClick(View v) {
                    onTitle.Title();
                }
                @Override
                public void onRightClick(View v) {
                    onTitle.Right();
                }
            } );
        }
    }

    public void leftBar(OnLeft onLeft) {
        if (mTitleBar != null) {
            /*标题栏监听*/
            mTitleBar.setOnTitleBarListener( new OnTitleBarListener() {
                @Override
                public void onLeftClick(View v) {
                    onLeft.Left();
                }
                @Override
                public void onTitleClick(View v) {

                }
                @Override
                public void onRightClick(View v) {

                }
            } );
        }
    }

    public void rightBar(OnRight onRight) {
        if (mTitleBar != null) {
            /*标题栏监听*/
            mTitleBar.setOnTitleBarListener( new OnTitleBarListener() {
                @Override
                public void onLeftClick(View v) {
                }
                @Override
                public void onTitleClick(View v) {
                }
                @Override
                public void onRightClick(View v) {
                    onRight.Right();
                }
            } );
        }
    }

    /* 初始化沉浸式*/
    protected void initImmersion() {
        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            statusBarConfig().init();
            ImmersionBar.with(this)
                    .statusBarDarkFont(true) /*状态栏深色字体*/
                    .navigationBarDarkIcon(true) /*导航栏深色图标*/
                    .init();
            // 设置标题栏沉浸
//            if (getLayout() > 0) {
//                ImmersionBar.setTitleBar( this, getWindow().getDecorView(), null  );
//            }
            if (mTitleBar != null) {
                ImmersionBar.setTitleBar( this, mTitleBar );
            }
        }
    }

    /**
     * 初始化沉浸式状态栏
     */
    protected ImmersionBar statusBarConfig() {
        // 在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with( this )
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont( true );
        return mImmersionBar;
    }

    /*true黑色false白色*/
    public void TitleColor(Boolean bool){
        InitApp.getHandler().post( new Runnable() {
            @Override
            public void run() {
                mImmersionBar.statusBarDarkFont(bool).init();
            }
        } );
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
     * 和 setContentView 对应的方法
     */
    public ViewGroup getContentView() {
        return findViewById( Window.ID_ANDROID_CONTENT );
    }

    /**
     * 显示吐司
     */
    public void Toast(String text) {
        MyToast.showToast( text );
    }

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 设置数据
     */
    public abstract void initData();

    @Override
    public void finish() {
        hideSoftKeyboard();
        super.finish();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressedSupport();
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onBackPressedSupport() {
        //fragment逐个退出
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressedSupport();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InitApp.getInstance().removeActivity( this );
        unbind.unbind();
    }

    //离开
    @Override
    protected void onStop() {
        super.onStop();
    }

    protected abstract int getLayout();

    public BaseActivity startActivity(Class activity) {
        mIntent = new Intent( this, activity );
        return this;
    }

    public void go() {
        if (mIntent != null) {
            mIntent.putExtras( mBundle );
            startActivity( mIntent );
        }
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
