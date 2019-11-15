package com.paper.boat.zrdx.ui.fragment.main;


import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.immersionbar.ImmersionBar;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyLazyFragment;
import com.paper.boat.zrdx.util.refresh.StatusBarUtil;
import com.paper.boat.zrdx.widget.XCollapsingToolbarLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import java.util.Arrays;

import butterknife.BindView;


/**
 * Created by yu on 2016/11/11.
 */

public class TestFragment extends MyLazyFragment
        implements View.OnClickListener, XCollapsingToolbarLayout.OnScrimsListener {
    @BindView(R.id.t_test_title)
    Toolbar mToolbar;
    @BindView(R.id.ctl_test_bar)
    XCollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.tv_test_address)
    TextView mAddressView;
    @BindView(R.id.tv_test_search)
    TextView mSearchView;

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    @BindView(R.id.backdrop)
    View backdrop;
    @BindView(R.id.title)
    View title;
    @BindView(R.id.header)
    ClassicsHeader header;

    @BindView(R.id.Coordinator)
    CoordinatorLayout Coordinator;

    private int mOffset = 0;
    private int mScrollY = 0;

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐
        ImmersionBar.setTitleBar( getAttachActivity(), mToolbar );
        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener( this );

        //状态栏透明和间距处理
        StatusBarUtil.immersive( getAttachActivity() );
//        StatusBarUtil.setPaddingSmart( mContext, mToolbar );
        /** 增加View上边距（MarginTop）一般是给高度为 WARP_CONTENT 的小控件用的*/
        StatusBarUtil.setMargin( getContext(), header );

        refreshLayout.setOnMultiPurposeListener( new SimpleMultiPurposeListener() {

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh( 1500 );
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore( 2000 );
            }

            /*在标题移动*/
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                backdrop.setTranslationY( mOffset - mScrollY );
                title.setAlpha( 1 - Math.min( percent, 1 ) );
            }

        } );

//        Coordinator.setOnScrollChangeListener( new View.OnScrollChangeListener() {
//            private int lastScrollY = 0;
//            private int h = SmartUtil.dp2px( 170 );
//            private int color = ContextCompat.getColor( getContext(), R.color.colorPrimary ) & 0x00ffffff;
//
//            /*滚动更改*/
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (lastScrollY < h) {
//                    scrollY = Math.min( h, scrollY );
//                    mScrollY = scrollY > h ? h : scrollY;
////                    buttonBar.setAlpha(1f * mScrollY / h);
//                    title.setBackgroundColor( ((255 * mScrollY / h) << 24) | color );
//                    backdrop.setTranslationY( mOffset - mScrollY );
//                }
//                lastScrollY = scrollY;
//            }
//        } );
        title.setBackgroundColor( 0 );

        recyclerView.setAdapter( adapter );
        adapter.replaceData( Arrays.asList( getResources().getStringArray( R.array.mz ) ) );
    }

    @Override
    protected void initData() {

    }

    private BaseQuickAdapter <String, BaseViewHolder> adapter = new BaseQuickAdapter <String, BaseViewHolder>( R.layout.item_menu ) {
        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText( R.id.tv_menu_name, item );
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.frag_base;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * CollapsingToolbarLayout 渐变回调
     * <p>
     * {@link XCollapsingToolbarLayout.OnScrimsListener}
     */
    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        if (shown) {
            mAddressView.setTextColor( ContextCompat.getColor( getAttachActivity(), R.color.black ) );
            mSearchView.setBackgroundResource( R.drawable.bg_home_search_bar_gray );
        } else {
            mAddressView.setTextColor( ContextCompat.getColor( getAttachActivity(), R.color.white ) );
            mSearchView.setBackgroundResource( R.drawable.bg_home_search_bar_transparent );
        }
    }
}
