package com.paper.boat.zrdx.ui.actvity.apply;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.ui.fragment.apply.DetailsFragment;
import com.paper.boat.zrdx.util.image.GlideHelper;
import com.paper.boat.zrdx.widget.XCollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ApplicationDetailsActivity extends MyActivity
        implements XCollapsingToolbarLayout.OnScrimsListener, TabLayout.BaseOnTabSelectedListener {
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
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

    @BindView(R.id.table_layout)
    TabLayout mTableLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    /*图片*/
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.imageView2)
    ImageView imageView2;

    private Map <ContentPage, View> mPageMap = new HashMap <>();

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

        mToolBar.setOnScrimsListener( this );
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

        mTableLayout.addOnTabSelectedListener( this );
        /*图片加载*/
        imageView.setImageDrawable( GlideHelper.rectRoundBitmap( getResources(), R.mipmap.bag ) );
        imageView2.setImageDrawable( GlideHelper.rectRoundBitmap( getResources(), R.mipmap.image_weibo_home_2 ) );

        // 用于ViewPager适配器
        List <Fragment> fragments = new ArrayList <>();
        //为适配器添加片段
        fragments.add( new DetailsFragment() );
        fragments.add( new DetailsFragment() );
        fragments.add( new DetailsFragment() );
        viewPager.setAdapter( new VpAdapter( getSupportFragmentManager(), fragments ) );
        viewPager.setCurrentItem( 0, false );
        mTableLayout.setupWithViewPager( viewPager );
    }

    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
        private List <Fragment> data;

        VpAdapter(FragmentManager fm, List <Fragment> data) {
            super( fm );
            this.data = data;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return data.get( position );
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return ContentPage.getPageNames()[position];
        }
    }

    /*选项卡上的选择*/
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    /*在选项卡上未选中*/
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    /*在选项卡上重新选择*/
    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

//    BaseQuickAdapter <String, BaseViewHolder> adapter = new BaseQuickAdapter <String, BaseViewHolder>( R.layout.item_menu ) {
//        @Override
//        protected void convert(BaseViewHolder helper, String item) {
//            helper.setText( R.id.tv_menu_name, item );
//        }
//    };

    /**
     * CollapsingToolbarLayout 渐变回调
     * <p>
     * {@link XCollapsingToolbarLayout.OnScrimsListener}
     */
    @SuppressLint("NewApi")
    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        post( () -> {
            if (shown) {
                titleBar.setBackground( getResources().getDrawable( R.drawable.shape_gradient ) );
            } else {
                titleBar.setBackground( getResources().getDrawable( R.color.transparent ) );
            }
        } );
    }
}
