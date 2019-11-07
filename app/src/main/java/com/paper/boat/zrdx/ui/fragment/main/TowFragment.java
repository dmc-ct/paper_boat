package com.paper.boat.zrdx.ui.fragment.main;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyLazyFragment;
import com.paper.boat.zrdx.ui.actvity.defray.PayDemoActivity;
import com.paper.boat.zrdx.util.HandlerTools;
import com.paper.boat.zrdx.util.base.XClickUtil;
import com.paper.boat.zrdx.util.image.GlideHelper;
import com.stx.xhb.androidx.XBanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/*轮播页*/
public class TowFragment extends MyLazyFragment {
    @BindView(R.id.banner2)
    XBanner banner;
    @BindView(R.id.use_recycler)
    RecyclerView use_recycler;

    public static TowFragment newInstance() {
        return new TowFragment();
    }

    @Override
    protected void initView() {
        List <String> imgesUrl = new ArrayList <>();
        imgesUrl.add( "第1张" );
        imgesUrl.add( "第2张" );
        imgesUrl.add( "第3张" );
        imgesUrl.add( "第4张" );
        // 初始化XBanner中展示的数据
        List <Integer> data = new ArrayList <>();
        data.add( R.mipmap.demo );
        data.add( R.mipmap.demo_two );
        data.add( R.mipmap.demo_three );
        data.add( R.mipmap.demo_four );

//        banner.setBannerData( R.layout-v2.layout_fresco_imageview, data );
        //刷新数据之后，需要重新设置是否支持自动轮播
//        mBanner.setAutoPlayAble( list.size() > 1 );
//        mBanner.setIsClipChildrenMode( true );
        // 为XBanner绑定数据
        banner.setData( data, imgesUrl );//第二个参数为提示文字资源集合
        /*适配屏幕大小*/
        banner.setLayoutParams( HandlerTools.getParams() );
        /*只有单张banner时是否展示指示器*/
        banner.setShowIndicatorOnlyOne( true );
        //设置自动播放功能
        banner.setAutoPlayAble( true );
        // XBanner适配数据
        banner.setmAdapter( new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
//                /*本地圆角加载*/
//                Bitmap image = BitmapFactory.decodeResource( getResources(), ((LocalImageInfo) model).getXBannerUrl() );
                Bitmap image = BitmapFactory.decodeResource( getResources(), data.get( position ) );
                ((ImageView) view).setImageDrawable( GlideHelper.rectRoundBitmap( image ) );
                /*网络圆角*/
//                GlideHelper.loadBanner( (ImageView) view, data.get( position ).url, true );
            }
        } );


        // 设置XBanner的页面切换特效，选择一个即可，总的大概就这么多效果啦，欢迎使用
//        banner.setPageTransformer( Transformer.Default );//横向移动
//        banner.setPageTransformer( Transformer.Alpha ); //渐变，效果不明显
//        banner.setPageTransformer( Transformer.Rotate );  //单页旋转
//        banner.setPageTransformer( Transformer.Cube );    //立体旋转
//        banner.setPageTransformer( Transformer.Flip );  // 反转效果
//        banner.setPageTransformer( Transformer.Accordion ); //三角换页
//        banner.setPageTransformer( Transformer.ZoomFade ); // 缩小本页，同时放大另一页
//        banner.setPageTransformer( Transformer.ZoomCenter ); //本页缩小一点，另一页就放大
//        banner.setPageTransformer( Transformer.ZoomStack ); // 本页和下页同事缩小和放大
//        banner.setPageTransformer( Transformer.Stack );  //本页和下页同时左移
//        banner.setPageTransformer( Transformer.Depth );  //本页左移，下页从后面出来
//        banner.setPageTransformer( Transformer.Zoom );  //本页刚左移，下页就在后面
        /*是否开启一屏多显模式*/
        banner.setIsClipChildrenMode( true );
        // 设置XBanner页面切换的时间，即动画时长
        banner.setPageChangeDuration( 1500 );


    }

    @Override
    protected void initData() {
        use_recycler.setLayoutManager( new GridLayoutManager( getContext(), 4 ) );
        use_recycler.setAdapter( adapter );
        Map <String, Object> map = new HashMap <>();
        map.put( "ico", R.mipmap.app_meng_pai );
        map.put( "text", "智慧门牌" );
        map.put( "packageName", "com.dream.zrdx.dream" );
        map.put( "className", "com.dream.zrdx.dream.ui.activity_v2.GuidanceActivity" );
        Map <String, Object> map1 = new HashMap <>();
        map1.put( "ico", R.mipmap.app_wang_ge );
        map1.put( "text", "智慧网格" );
        map1.put( "packageName", "com.dream.zrdx.wang_ge" );
        map1.put( "className", "com.dream.zrdx.wang_ge.ui.activity.GuidanceActivity" );
        List <Map> list = new ArrayList <>();
        list.add( map );
        list.add( map1 );

        // 禁用动画效果
        use_recycler.setItemAnimator( null );

        postDelayed( () -> {
            // 执行列表动画
            use_recycler.setLayoutAnimation( AnimationUtils.loadLayoutAnimation( getActivity(), R.anim.layout_animation_fall_down ) );
            use_recycler.scheduleLayoutAnimation();
            adapter.replaceData( list );
        }, 1000 );

    }

    BaseQuickAdapter <Map, BaseViewHolder> adapter = new BaseQuickAdapter <Map, BaseViewHolder>( R.layout.list_use ) {
        @Override
        protected void convert(BaseViewHolder helper, Map item) {

            helper.setImageResource( R.id.ico, (int) item.get( "ico" ) );
            helper.setText( R.id.use_text, item.get( "text" ) + "" );
            helper.setOnClickListener( R.id.list_item, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (XClickUtil.isFastDoubleClick()) {
                        return;
                    }
                    Intent intent = new Intent( Intent.ACTION_MAIN );
                    intent.addCategory( Intent.CATEGORY_LAUNCHER );
                    ComponentName cn = new ComponentName( item.get( "packageName" ) + "", item.get( "className" ) + "" );
                    intent.setComponent( cn );
                    startActivity( intent );
                }
            } );
        }
    };

    @Override
    public void onRightClick(View v) {
        super.onRightClick( v );
        startActivity( PayDemoActivity.class ).go();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ftag_ment;
    }


    /**
     * 为了更好的体验效果建议在下面两个生命周期中调用下面的方法
     **/
    @Override
    public void onResume() {
        super.onResume();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }
}
