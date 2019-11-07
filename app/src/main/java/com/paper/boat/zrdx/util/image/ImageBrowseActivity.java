package com.paper.boat.zrdx.util.image;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import com.github.chrisbanes.photoview.PhotoView;
import com.gyf.immersionbar.BarHide;
import com.jaeger.library.StatusBarUtil;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.util.base.MyToast;
import com.paper.boat.zrdx.util.webview.FileUtils;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Kevin on 2017/8/29.
 */
/*图片预览*/
public class ImageBrowseActivity extends MyActivity {
    private ViewPager viewPager;
    private List <View> views;
    private RelativeLayout container;
    private CircleIndicator indicator;
    private PhotoView photo_view, url_photo_view, res_id_photo_view;

    //类型枚举标志
    public static int[] FLAG_ENUM = new int[]{0, 1, 2, 3};
    private int position = 0;

    @Override
    public void initView() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
//        setContentView( R.layout-v2.activity_image_browse);

        // 设置状态栏和导航栏参数
        statusBarConfig()
                // 有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .fullScreen( true )
                // 隐藏状态栏
                .hideBar( BarHide.FLAG_HIDE_STATUS_BAR )
                // 透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .transparentNavigationBar()
                .init();

        StatusBarUtil.setColor( this, Color.BLACK, 0 );
        container = findViewById( R.id.container );
        initDatas();
    }

    private void savePhotoToLocal(Bitmap bitmap) {
        FileUtils.savePhoto( this, bitmap, new FileUtils.SaveResultCallback() {
            @Override
            public void onSavedSuccess() {
                runOnUiThread( () -> MyToast.showToast( "保存成功" ) );
            }

            @Override
            public void onSavedFailed() {
                runOnUiThread( () -> MyToast.showToast( "保存失败" ) );
            }
        } );
    }

    @Override
    public void initData() {

    }

    @Override
    public void onRightClick(View v) {
        ((BitmapDrawable) photo_view.getDrawable()).getBitmap();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDatas() {
        indicator = findViewById( R.id.indicator );
        viewPager = findViewById( R.id.viewPager );
        // 1.设置幕后item的缓存数目
        viewPager.setOffscreenPageLimit( 3 );
        // 2.设置页与页之间的间距
        viewPager.setPageMargin( 10 );

        views = new ArrayList <>();

        Bundle bundle = getIntent().getExtras();
        switch (bundle.getInt( ImageBrowseIntent.PARAM_FLAG_ENUM )) {
            case 0://Url组
                ArrayList <String> imageList = (ArrayList <String>) bundle.get( ImageBrowseIntent.PARAM_URL_GROUP );
                //动态添加View
                for (int i = 0; i < imageList.size(); i++) {
                    View view = LayoutInflater.from( this ).inflate( R.layout.adapter_image, null );
                    photo_view = view.findViewById( R.id.photo_view );
                    GlideHelper.load( this, imageList.get( i ), photo_view, true );
                    views.add( view );
                }
                indicator.setVisibility( View.VISIBLE );
                position = bundle.getInt( ImageBrowseIntent.PARAM_POSITION );
                break;
            case 1://Url单
                View urlView = LayoutInflater.from( this ).inflate( R.layout.adapter_image, null );
                url_photo_view = urlView.findViewById( R.id.photo_view );
                GlideHelper.load( this, bundle.get( ImageBrowseIntent.PARAM_URL_SINGLE ), url_photo_view, true );
                views.add( urlView );
                indicator.setVisibility( View.GONE );
                break;
            case 2://本地资源组
                ArrayList <Integer> imageResIds = bundle.getIntegerArrayList( ImageBrowseIntent.PARAM_RES_ID_GROUP );
                //动态添加View
                for (int i = 0; i < imageResIds.size(); i++) {
                    View view = LayoutInflater.from( this ).inflate( R.layout.adapter_image, null );
                    photo_view = view.findViewById( R.id.photo_view );
                    GlideHelper.load( this, imageResIds.get( i ), photo_view, true );
                    views.add( view );
                }
                indicator.setVisibility( View.VISIBLE );
                position = bundle.getInt( ImageBrowseIntent.PARAM_POSITION );
                break;
            case 3://本地资源单
                View resIdView = LayoutInflater.from( this ).inflate( R.layout.adapter_image, null );
                res_id_photo_view = resIdView.findViewById( R.id.photo_view );
                GlideHelper.load( this, bundle.get( ImageBrowseIntent.PARAM_RES_ID_SINGLE ), res_id_photo_view, true );
                views.add( resIdView );
                indicator.setVisibility( View.GONE );
                break;
            default:
                break;
        }
        viewPager.setAdapter( new MyPagerAdapter( views ) ); // 为viewpager设置adapter
        indicator.setViewPager( viewPager );
        container = findViewById( R.id.container );
        viewPager.setCurrentItem( position );
        // 3.将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
        container.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent( event );
            }
        } );
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_browse;
    }
}