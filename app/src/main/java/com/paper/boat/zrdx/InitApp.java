package com.paper.boat.zrdx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.hjq.toast.ToastInterceptor;
import com.hjq.toast.ToastUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.ui.actvity.util.CrashActivity;
import com.paper.boat.zrdx.util.base.DynamicTimeFormat;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.zl.weilu.saber.api.Saber;

import java.util.Locale;
import java.util.Set;

import cat.ereza.customactivityoncrash.activity.DefaultErrorActivity;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.external.ExternalAdaptInfo;
import me.jessyan.autosize.external.ExternalAdaptManager;
import me.jessyan.autosize.internal.CustomAdapt;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.LogUtils;

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
        /*初始化SDK*/
        initSDK( this );
        /*适配初始化*/
        screenAdaptation();
    }

    private void initSDK(Application application) {
        // 内存泄漏检测
        LeakCanary.install( application );
        // <--这里绑定ViewModel
        Saber.bind( this );
        /* 仅仅是缓存Application的Context，不耗时*/
        FileDownloader.init( getBaseContext() );
        //文件下载初始化
        FileDownloader.setup( getBaseContext() );
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
        ToastUtils.setToastInterceptor( new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept( toast, text );
                if (intercept) {
                    Log.e( "Toast", "空 Toast" );
                } else {
                    Log.i( "Toast", text.toString() );
                }
                return intercept;
            }
        } );
        // 吐司工具类
        ToastUtils.init( application );
        ToastUtils.setGravity( Gravity.BOTTOM, 0, 150 );
        /*智能刷新*/
        SmartRefreshLayout.setDefaultRefreshHeaderCreator( (context, layout) -> {
            //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
            return new ClassicsHeader( context ).setTimeFormat( new DynamicTimeFormat( "更新于 %s" ) );
        } );
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer( new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置（优先级最低）
                layout.setEnableAutoLoadMore( false ); /*设置启用自动加载更多*/
                layout.setEnableOverScrollDrag( false ); /*设置启用滚动拖动*/
                layout.setEnableOverScrollBounce( true ); /*设置“启用滚动弹跳”*/
                layout.setEnableLoadMoreWhenContentNotFull( true ); /*设置当内容不完整时启用更多加载*/
                layout.setEnableScrollContentWhenRefreshed( true ); /*设置刷新后启用滚动内容*/
                layout.setPrimaryColorsId( R.color.colorPrimary, android.R.color.white ); /*设置原色ID*/
            }
        } );

    }

    private void screenAdaptation() {
        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        //在 Demo 中跳转的三方库中的 DefaultErrorActivity 就是在另外一个进程中, 所以要想适配这个 Activity 就需要调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess( this );
        /*
         * 以下是 AndroidAutoSize 可以自定义的参数, {@link AutoSizeConfig} 的每个方法的注释都写的很详细
         * 使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!
         */
        AutoSizeConfig.getInstance()

                //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
                //如果没有这个需求建议不开启
                .setCustomFragment( true )
                //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
                //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
                .setExcludeFontScale( false )
                //屏幕适配监听器
                .setOnAdaptListener( new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                        //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
//                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
//                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                        LogUtils.d( String.format( Locale.ENGLISH, "%s onAdaptBefore!", target.getClass().getName() ) );
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {
                        LogUtils.d( String.format( Locale.ENGLISH, "%s onAdaptAfter!", target.getClass().getName() ) );
                    }
                } )
                //是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
                .setLog( true )
                //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
                //AutoSize 会将屏幕总高度减去状态栏高度来做适配
                //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
                .setUseDeviceSize( false )
                //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
                .setBaseOnWidth( true )
        //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
//                .setAutoAdaptStrategy(new AutoAdaptStrategy());
        ;
        customAdaptForExternal();
    }

    /**
     * 给外部的三方库 {@link Activity} 自定义适配参数, 因为三方库的 {@link Activity} 并不能通过实现
     * {@link CustomAdapt} 接口的方式来提供自定义适配参数 (因为远程依赖改不了源码)
     * 所以使用 {@link ExternalAdaptManager} 来替代实现接口的方式, 来提供自定义适配参数
     */
    private void customAdaptForExternal() {
        /*
         * {@link ExternalAdaptManager} 是一个管理外部三方库的适配信息和状态的管理类, 详细介绍请看 {@link ExternalAdaptManager} 的类注释
         */
        AutoSizeConfig.getInstance().getExternalAdaptManager()
                //加入的 Activity 将会放弃屏幕适配, 一般用于三方库的 Activity, 详情请看方法注释
                //如果不想放弃三方库页面的适配, 请用 addExternalAdaptInfoOfActivity 方法, 建议对三方库页面进行适配, 让自己的 App 更完美一点
//                .addCancelAdaptOfActivity(DefaultErrorActivity.class)

                //为指定的 Activity 提供自定义适配参数, AndroidAutoSize 将会按照提供的适配参数进行适配, 详情请看方法注释
                //一般用于三方库的 Activity, 因为三方库的设计图尺寸可能和项目自身的设计图尺寸不一致, 所以要想完美适配三方库的页面
                //就需要提供三方库的设计图尺寸, 以及适配的方向 (以宽为基准还是高为基准?)
                //三方库页面的设计图尺寸可能无法获知, 所以如果想让三方库的适配效果达到最好, 只有靠不断的尝试
                //由于 AndroidAutoSize 可以让布局在所有设备上都等比例缩放, 所以只要您在一个设备上测试出了一个最完美的设计图尺寸
                //那这个三方库页面在其他设备上也会呈现出同样的适配效果, 等比例缩放, 所以也就完成了三方库页面的屏幕适配
                //即使在不改三方库源码的情况下也可以完美适配三方库的页面, 这就是 AndroidAutoSize 的优势
                //但前提是三方库页面的布局使用的是 dp 和 sp, 如果布局全部使用的 px, 那 AndroidAutoSize 也将无能为力
                //经过测试 DefaultErrorActivity 的设计图宽度在 380dp - 400dp 显示效果都是比较舒服的
                .addExternalAdaptInfoOfActivity( DefaultErrorActivity.class, new ExternalAdaptInfo( true, 400 ) );
    }

    public static Context getContext() {
        return AppContext;
    }

    public static Handler getHandler() {
        return mainHandler;
    }

}
