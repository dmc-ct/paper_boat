package com.paper.boat.zrdx.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.andview.refreshview.XRefreshView;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.InitApp;
import com.paper.boat.zrdx.util.interfaces.XRefresh;
import com.paper.boat.zrdx.view.ScreenUtil;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.entity.LocalImageInfo;

import java.util.ArrayList;
import java.util.List;

//处理应用程序
public class HandlerTools {
    public static long lastRefreshTime;

    /**
     * 初始化轮播选择器
     */
    public static LinearLayout.LayoutParams getParams() {
        return new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenWidth( InitApp.getContext() ) / 2 );
    }

    /**
     * 初始化适配器RecycleView
     */
    public static LinearLayoutManager getManager() {
        LinearLayoutManager manager = new LinearLayoutManager( InitApp.getContext() );
        manager.setOrientation( LinearLayoutManager.VERTICAL );
        return manager;
    }

    /**
     * 初始化XRefreshView
     */
    public static void XRefreshView(XRefreshView xRefreshView) {
        xRefreshView.setPullLoadEnable( true );
        xRefreshView.setPullRefreshEnable( true ); //下拉
        xRefreshView.setPinnedContent( false ); //列表不滚动
        xRefreshView.setAutoLoadMore( false ); //自动加载
        //设置刷新完成以后，headerview固定的时间
        xRefreshView.setPinnedTime( 200 );
        // 设置上次刷新的时间
        xRefreshView.restoreLastRefreshTime( HandlerTools.lastRefreshTime );
        //设置加载完成
//        xRefreshView.setLoadComplete(true);
    }

    /**
     * 刷新暂停
     */
    public static void RefreshUI(XRefreshView xRefreshView) {
        HandlerTools.lastRefreshTime = xRefreshView.getLastRefreshTime();
        xRefreshView.stopLoadMore();
        xRefreshView.stopRefresh();
    }

    /**
     * 标题全局初始化
     */
    public static void title(Activity activity, View view, String string) {
        ImageView backImg = view.findViewById( R.id.backImg );
        TextView title_text = view.findViewById( R.id.title_text );
        title_text.setText( string );
        backImg.setOnClickListener( v -> activity.finish() );
    }

    /**
     * 加载本地图片(测试demo)
     */
    public static void initLocalImage(XBanner mBanner) {
        // 初始化XBanner中展示的数据
        List <LocalImageInfo> data = new ArrayList <>();
//        data.add( new LocalImageInfo( R.mipmap.demo ) );
//        data.add( new LocalImageInfo( R.mipmap.demo_two ) );
//        data.add( new LocalImageInfo( R.mipmap.demo_three ) );
//        data.add( new LocalImageInfo( R.mipmap.demo_four ) );
        mBanner.setBannerData( data );
        //设置自动播放功能
        mBanner.setAutoPlayAble( true );
    }

    /**
     * 上拉下拉刷新
     *
     * @param pull_up      是否支持下拉
     * @param xRefreshView 当前刷新对象
     * @param XRefresh     实现刷新接口
     */
    public static void Refresh(Boolean pull_up, XRefreshView xRefreshView, final XRefresh XRefresh) {
        xRefreshView.setPullLoadEnable( pull_up ); //上拉
        xRefreshView.setPullRefreshEnable( true ); //下拉
        xRefreshView.setPinnedContent( false ); //列表不滚动
        xRefreshView.setAutoLoadMore( false ); //自动加载
        //设置刷新完成以后，headerview固定的时间
        xRefreshView.setPinnedTime( 200 );
        // 设置上次刷新的时间
        xRefreshView.restoreLastRefreshTime( HandlerTools.lastRefreshTime );
        xRefreshView.setXRefreshViewListener( new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {  //下拉回调
                InitApp.getHandler().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        RefreshUI( xRefreshView );
                        XRefresh.onRefresh();
                    }
                }, 500 );
            }

            //下拉回调
            @Override
            public void onRefresh(boolean isPullDown) {

            }

            @Override
            public void onLoadMore(boolean isSilence) {  //上拉回调
                InitApp.getHandler().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        RefreshUI( xRefreshView );
                        XRefresh.onLoadMore( isSilence );
                    }
                }, 500 );
            }

            //释放
            @Override
            public void onRelease(float direction) {

            }

            //移动
            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {

            }
        } );
    }

    /**
     * 获取版本号名称
     */
    public static String getVerName() {
        String verName = "";
        try {
            verName = InitApp.getContext().getPackageManager().  //得到包管理器(对象)
                    getPackageInfo( InitApp.getContext().getPackageName(), 0 ).versionName;  //获取包信息
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 版本号比较
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals( version2 )) {
            return 0;
        }
        String[] version1Array = version1.split( "\\." );
        String[] version2Array = version2.split( "\\." );
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min( version1Array.length, version2Array.length );
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen && (diff = Integer.parseInt( version1Array[index] )
                - Integer.parseInt( version2Array[index] )) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt( version1Array[i] ) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt( version2Array[i] ) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
}
