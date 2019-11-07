package com.paper.boat.zrdx.ui.fragment.main;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.flyco.roundview.RoundTextView;
import com.hjq.bar.TitleBar;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyLazyFragment;
import com.paper.boat.zrdx.ui.actvity.util.SqlActivity;
import com.paper.boat.zrdx.ui.actvity.web.WebActivity;
import com.paper.boat.zrdx.util.File.IntentKey;
import com.paper.boat.zrdx.util.File.MyShared;
import com.paper.boat.zrdx.util.refresh.StatusBarUtil;
import com.paper.boat.zrdx.view.layout.GuideView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.SmartUtil;

import butterknife.BindView;
import butterknife.OnClick;

/*折叠工具栏布局*/
public class CollapsingFragment extends MyLazyFragment {
    @BindView(R.id.title)
    TitleBar titleBar;
    @BindView(R.id.header)
    ClassicsHeader header;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.parallax)
    ImageView parallax;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.leaveword)
    RoundTextView leaveWord;
    @BindView(R.id.attention)
    RoundTextView attention;

    private int mOffset = 0;
    private int mScrollY = 0;
    private GuideView guideView;
    private GuideView guideView2;

    public static CollapsingFragment newInstance() {
        return new CollapsingFragment();
    }

    @Override
    protected void initView() {
        //状态栏透明和间距处理
//        StatusBarUtil.immersive( getAttachActivity() );
//        StatusBarUtil.setPaddingSmart( getContext(), titleBar );
        StatusBarUtil.setMargin( getContext(), header );

        /**获取参数，根据不同的参数播放不同的视频**/
//        int index = getArguments().getInt("index");

        refreshLayout.setOnMultiPurposeListener( new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh( 1500 );
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore( 1500 );
            }

            /*在标题移动*/
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY( mOffset - mScrollY );
                titleBar.setAlpha( 1 - Math.min( percent, 1 ) );
            }
        } );

        /*在滚动更改侦听器上设置*/
        scrollView.setOnScrollChangeListener( new NestedScrollView.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = SmartUtil.dp2px( 170 );
            private int color = ContextCompat.getColor( getContext(), R.color.colorPrimary ) & 0x00ffffff;

            /*滚动更改*/
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min( h, scrollY );
                    mScrollY = scrollY > h ? h : scrollY;
//                    buttonBar.setAlpha(1f * mScrollY / h);
                    titleBar.setBackgroundColor( ((255 * mScrollY / h) << 24) | color );
//                    titleBar.setBackgroundDrawable( getResources().getDrawable( R.drawable.shape_gradient ) );
                    /*图片高宽度*/
                    parallax.setTranslationY( mOffset - mScrollY );
                }
                lastScrollY = scrollY;
            }
        } );
//        buttonBar.setAlpha(0);
        titleBar.setBackgroundColor( 0 );
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_practice_weibo;
    }

    @OnClick({R.id.leaveword, R.id.attention})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.leaveword:
                startActivity( WebActivity.class ).go();
                break;
            case R.id.attention:
                startActivity( SqlActivity.class ).go();
                break;
        }
    }

    @Override
    public void onResume() {
        if ((Boolean) MyShared.getInstance().get( IntentKey.GUIDE, true )) {
            setGuideView();
            MyShared.getInstance().put( IntentKey.GUIDE, false );
        }
        super.onResume();
    }

    /*
         * GuideView.Builder
         .newInstance(this)      // 必须调用
         .setTargetView(view)    // 必须调用，设置需要Guide的View
         .setCustomTipsView(iv)  // 必须调用，设置GuideView，可以使任意View的实例，比如ImageView 或者TextView
         .setDirction(GuideView.Direction.LEFT_BOTTOM)   // 设置GuideView 相对于TargetView的位置，有八种，不设置则默认在屏幕左上角,其余的可以显示在右上，右下等等
         .setShape(GuideView.MyShape.RECTANGULAR)   // 设置显示形状，支持圆形，椭圆，矩形三种样式，矩形可以是圆角矩形，
         .setBackGround(getResources().getColor(R.color.shadow)) // 设置背景颜色，默认透明
         .setOnclickExit(null)   // 设置点击消失，可以传入一个Callback，执行被点击后的操作
         .setRadius(32)          // 设置圆形或矩形透明区域半径，默认是targetView的显示矩形的半径，如果是矩形，这里是设置矩形圆角大小
         .setCenter(300, 300)    // 设置圆心，默认是targetView的中心
         .setOffset(200, 60)     // 设置偏移，一般用于微调GuideView的位置
         .showOnce()             // 设置首次显示，设置后，显示一次后，不再显示
         .build()                // 必须调用，Buider模式，返回GuideView实例
         .show();                // 必须调用，显示GuideView
          */
    private void setGuideView() {

        // 使用图片
        final ImageView iv = new ImageView( getContext() );
        iv.setImageResource( R.drawable.img_new_task_guide );
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        iv.setLayoutParams( params );

        // 使用文字
        TextView tv = new TextView( getContext() );
        tv.setText( "欢迎使用" );
        tv.setTextColor( getResources().getColor( R.color.white ) );
        tv.setTextSize( 30 );
        tv.setGravity( Gravity.CENTER );

        // 使用文字
        final TextView tv2 = new TextView( getContext() );
        tv2.setText( "欢迎使用2" );
        tv2.setTextColor( getResources().getColor( R.color.white ) );
        tv2.setTextSize( 30 );
        tv2.setGravity( Gravity.CENTER );


        guideView = GuideView.Builder
                .newInstance( getAttachActivity() )
                .setTargetView( leaveWord )//设置目标
                .setCustomGuideView( iv )
                .setDirction( GuideView.Direction.LEFT_BOTTOM )
                .setShape( GuideView.MyShape.CIRCULAR )   // 设置圆形显示区域，
                .setBgColor( getResources().getColor( R.color.shadow ) )
                .setOnclickListener( new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        guideView.hide();
                        guideView2.show();
                    }
                } )
                .build();


        guideView2 = GuideView.Builder
                .newInstance( getAttachActivity() )
                .setTargetView( attention )
                .setCustomGuideView( tv )
                .setDirction( GuideView.Direction.LEFT_BOTTOM )
                .setShape( GuideView.MyShape.CIRCULAR )   // 设置椭圆形显示区域，
                .setBgColor( getResources().getColor( R.color.shadow ) )
                .setOnclickListener( new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        guideView2.hide();
                    }
                } )
                .build();

        guideView.show();
    }
}
