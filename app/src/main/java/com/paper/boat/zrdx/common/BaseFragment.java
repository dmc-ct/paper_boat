package com.paper.boat.zrdx.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.InitApp;
import com.paper.boat.zrdx.annotation.AutoArg;
import com.paper.boat.zrdx.util.CommonUtil;
import com.paper.boat.zrdx.util.base.GsonUtils;
import com.paper.boat.zrdx.util.base.MyToast;
import com.paper.boat.zrdx.util.interfaces.OnTitle;
import com.paper.boat.zrdx.view.dialog.StateNoticeDialog;
import com.paper.boat.zrdx.widget.ContentPage;
import com.zl.weilu.saber.api.Saber;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public abstract class BaseFragment extends SupportFragment {
    public ContentPage contentPage;
    public ProgressDialog pdLoading;
    protected Activity mActivity;
    protected FragmentActivity fActivity;
    protected Context mContext;
    private Unbinder mUnBinder;
    /* 状态栏沉浸*/
    private ImmersionBar mImmersionBar;
    /* 标题栏对象*/
    private TitleBar mTitleBar;
    private Intent mIntent;
    private Bundle mBundle = new Bundle();
    private StateNoticeDialog.Builder mDialog;
    private View view;
    private Boolean BarConfig = true;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        fActivity = getActivity();
        mContext = context;
        super.onAttach( context );
        mDialog = new StateNoticeDialog.Builder( getContext() );
        Saber.bind( this ); // <--这里绑定ViewModel
        /*初始化界面View*/
        if (getSuccessView() > 0) {
            view = View.inflate( getContext(), getSuccessView(), null );
            mTitleBar = view.findViewById( R.id.title );
        }
        /*状态栏*/
        initImmersion();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        mUnBinder = ButterKnife.bind( this, view );
        init();
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

    public void dialog(String string) {
        mDialog.showNotice( string, 3, false, 0 );
    }

    public void onDismiss() {
        mDialog.onDismiss();
    }

    /*显示吐司*/
    public void toast(String text) {
        MyToast.showToast( text );
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
            if (getSuccessView() > 0) {
                ImmersionBar.setTitleBar( this, View.inflate( getContext(), getSuccessView(), null ) );
            }
            if (mTitleBar != null) {
                ImmersionBar.setTitleBar( this, mTitleBar );
            }
        }
    }

    /**
     * 是否使用沉浸式状态栏
     */
    public boolean isStatusBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式状态栏
     */
    protected ImmersionBar statusBarConfig() {
        // 在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with( this )
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont( BarConfig );
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* 进度对话框*/
        pdLoading = new ProgressDialog( getActivity() );
        pdLoading.setProgressStyle( ProgressDialog.STYLE_SPINNER );
        pdLoading.setMessage( "请稍后" );
        /*设置“在外部触摸时取消”*/
        pdLoading.setCanceledOnTouchOutside( false );
        /*设置可取消*/
        pdLoading.setCancelable( true );

        if (contentPage == null) {
            //页面请求(请求状态)
            contentPage = new ContentPage( getActivity() ) {
                @Override
                public Object loadData() {
                    //返回请求服务器的数据
                    return requestData();  //失败返回提示
                }

                @Override
                public View createSuccessView() {
                    //返回据的fragment填充的具体View
                    return view;  //成功返回页面
                }
            };
        } else {
            //从父亲布局中移除自己
            CommonUtil.removeSelfFromParent( contentPage );
        }
        return contentPage;
    }

    protected abstract void init();

    /**
     * 刷新状态
     */
    public void refreshPage(Object o) {
        contentPage.refreshPage( o );
    }

    /**
     * 返回据的fragment填充的具体View
     */
    protected abstract int getSuccessView();

    /**
     * 返回请求服务器的数据
     */
    protected abstract Object requestData();


    //销毁页面
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    public BaseFragment startActivity(Class activity) {
        mIntent = new Intent( getContext(), activity );
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

    public BaseFragment withObject(@Nullable String key, @Nullable Object value) {
        String json = GsonUtils.toJson( value );
        mBundle.putString( key, json );
        return this;
    }

    public BaseFragment withString(@Nullable String key, @Nullable String value) {
        mBundle.putString( key, value );
        return this;
    }

    public BaseFragment withBoolean(@Nullable String key, boolean value) {
        mBundle.putBoolean( key, value );
        return this;
    }

    public BaseFragment withInt(@Nullable String key, int value) {
        mBundle.putInt( key, value );
        return this;
    }

    public BaseFragment withFloat(@Nullable String key, float value) {
        mBundle.putFloat( key, value );
        return this;
    }

    public BaseFragment withIntegerArrayList(@Nullable String key, @Nullable ArrayList <Integer> value) {
        mBundle.putIntegerArrayList( key, value );
        return this;
    }

    public BaseFragment withStringArrayList(@Nullable String key, @Nullable ArrayList <String> value) {
        mBundle.putStringArrayList( key, value );
        return this;
    }

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
            MyToast.printLog( e.getMessage() );
        }
    }
}