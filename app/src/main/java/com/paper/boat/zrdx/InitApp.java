package com.paper.boat.zrdx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.hjq.toast.ToastInterceptor;
import com.hjq.toast.ToastUtils;
import com.paper.boat.zrdx.ui.actvity_utill.CrashActivity;

import java.util.HashSet;
import java.util.Set;

import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 * 初始化应用程序
 */
@SuppressLint("Registered")
public class InitApp extends MultiDexApplication {

    private static Handler mainHandler;
    private static Context AppContext;
    private static Application application;
    //实例变量
    private static InitApp instance;
    private Set <Activity> allActivities;

    public static final String TAG = "Tinker.SampleApplicationLike";

    public static synchronized InitApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install( this );
        AppContext = this;
        instance = this;
        mainHandler = new Handler();
        application = this;
        initSDK(this);
    }

    private void initSDK(Application application) {
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        // Crash 捕捉界面
        CaocConfig.Builder.create()
                .backgroundMode( CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM )
                .enabled( true )
                .trackActivities( true )
                .minTimeBetweenCrashesMs( 2000 )
                // 重启的 Activity
                .restartActivity( MainActivity.class )
                // 错误的 Activity
                .errorActivity( CrashActivity.class )
                // 设置监听器
                //.eventListener(new YourCustomEventListener())
                .apply();
        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });
        // 吐司工具类
        ToastUtils.init(application);
    }

    public static Context getContext() {
        return AppContext;
    }

    public static Handler getHandler() {
        return mainHandler;
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet <>();
        }
        allActivities.add( act );
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove( act );
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess( android.os.Process.myPid() );
        System.exit( 0 );
    }

}
