package com.paper.boat.zrdx.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.InitApp;
import com.paper.boat.zrdx.util.interfaces.XRefresh;
import com.paper.boat.zrdx.view.util.ScreenUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.stx.xhb.androidx.XBanner;
import com.stx.xhb.androidx.entity.LocalImageInfo;

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
        data.add( new LocalImageInfo( R.mipmap.demo ) );
        data.add( new LocalImageInfo( R.mipmap.demo_two ) );
        data.add( new LocalImageInfo( R.mipmap.demo_three ) );
        data.add( new LocalImageInfo( R.mipmap.demo_four ) );
        mBanner.setBannerData( data );
        //设置自动播放功能
        mBanner.setAutoPlayAble( true );
    }


    /**
     * 上拉下拉刷新
     *
     * @param pull_up       是否支持下拉
     * @param refreshLayout 当前刷新对象
     * @param XRefresh      实现刷新接口
     */
    public static void Refresh(Boolean pull_up, RefreshLayout refreshLayout, final XRefresh XRefresh) {
        /*下拉刷新*/
        refreshLayout.setEnableLoadMore( pull_up );
        refreshLayout.setOnMultiPurposeListener( new SimpleMultiPurposeListener() {
            /*下拉*/
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishRefresh( 500 );
                refreshLayout.getLayout().post( new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();/*完成刷新*/
                        refreshLayout.resetNoMoreData(); /*重置(恢复)无更多数据*/
                        XRefresh.onRefresh();
                    }
                } );
            }

            /*上拉*/
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishRefresh(); /*完成刷新*/
//                refreshLayout.resetNoMoreData(); /*重置无更多数据*/
//                refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
//                refreshLayout.finishLoadMore( 500 );

                refreshLayout.getLayout().post( new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadMore(); /*完成刷新*/
                        XRefresh.onLoadMore();
                    }
                } );
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
