package com.paper.boat.zrdx.ui.actvity.apply;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.util.base.MyToast;
import com.paper.boat.zrdx.widget.XCollapsingToolbarLayout;
import com.scwang.smartrefresh.layout.util.SmartUtil;

import java.util.Arrays;

import butterknife.BindView;

public class ApplicationDetailsActivity extends MyActivity {
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    //    @BindView(R.id.scrollView)
//    NestedScrollView scrollView;
    @BindView(R.id.title)
    TitleBar titleBar;
    @BindView(R.id.video_view)
    ImageView mViewView;
    @BindView(R.id.tool_bar)
    XCollapsingToolbarLayout mToolBar;
    @BindView(R.id.bar_layout)
    AppBarLayout barLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.coordinator)
    CoordinatorLayout Coordinator;

    private int mOffset = 0;
    private int mScrollY = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_application_details;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
            //透明导航栏
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION );
        }
        ImmersionBar.setTitleBar( this, toolbar );

        Coordinator.setOnScrollChangeListener( new View.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = SmartUtil.dp2px( 170 );
            private int color = ContextCompat.getColor( getApplicationContext(), R.color.colorPrimary ) & 0x00ffffff;
            /*滚动更改*/
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                 MyToast.printLog( lastScrollY + "123" );
                if (lastScrollY < h) {
                    scrollY = Math.min( h, scrollY );
                    mScrollY = scrollY > h ? h : scrollY;
                    //                    buttonBar.setAlpha(1f * mScrollY / h);
                    titleBar.setBackgroundColor( ((255 * mScrollY / h) << 24) | color );
                    mViewView.setTranslationY( mOffset - mScrollY );
                }
                lastScrollY = scrollY;
            }
        } );


    }

    /*状态栏已启用*/
    @Override
    public boolean isStatusBarEnabled() {
        return true;
    }

    /*状态栏深色字体*/
    @Override
    public boolean statusBarDarkFont() {
        return false;
    }

    @Override
    protected void initData() {

        mRecycler.setAdapter( adapter );
        adapter.replaceData( Arrays.asList( getResources().getStringArray( R.array.mz ) ) );
    }

    BaseQuickAdapter <String, BaseViewHolder> adapter = new BaseQuickAdapter <String, BaseViewHolder>( R.layout.item_menu ) {
        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText( R.id.tv_menu_name, item );
        }
    };
}
