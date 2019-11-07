package com.paper.boat.zrdx;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.helper.ActivityStackManager;
import com.paper.boat.zrdx.helper.DoubleClickHelper;
import com.paper.boat.zrdx.ui.fragment.main.CollapsingFragment;
import com.paper.boat.zrdx.ui.fragment.main.DialogFragment;
import com.paper.boat.zrdx.ui.fragment.main.FPersonalData;
import com.paper.boat.zrdx.ui.fragment.main.TowFragment;
import com.paper.boat.zrdx.view.button.BottomNavigationViewEx;
import com.paper.boat.zrdx.view.edit.KeyboardWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends MyActivity
        implements  KeyboardWatcher.SoftKeyboardStateListener {
    @BindView(R.id.bnve)
    BottomNavigationViewEx bnve;
    @BindView(R.id.vp)
    ViewPager vp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        /*初始化界面*/
        initData();
        /*底部初始化*/
        init();
        /*底部按钮监听*/
        initEvent();
    }

    private void initEvent() {

        // 设置监听器以在单击底部导航项时更改视图分页程序的当前项
        bnve.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = 0;
                switch (item.getItemId()) {
                    case R.id.i_music:
                        position = 0;
                        break;
                    case R.id.i_backup:
                        position = 1;
                        break;
                    case R.id.i_favor:
                        position = 2;
                        break;
                    case R.id.i_visibility:
                        position = 3;
                        break;
                }
                if (previousPosition != position) {
                    vp.setCurrentItem( position, false );
                    previousPosition = position;
                }
                return true;
            }
        } );

        //设置监听器以在滚动视图页导航时更改当前选中的底部导航项
        vp.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if (position >= 2)// 2 is center
//                    position++;// if page is 2, need set bottom item to 3, and the same to 3 -> 4
                bnve.setCurrentItem( position );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        } );


    }

    @Override
    public void initData() {

    }

    private void init() {
        /*底部隐藏*/
        KeyboardWatcher.with(this)
                .setListener(this);
        /*
         * ViewPager 适配器
         */
//        BaseFragmentAdapter <MyLazyFragment> mPagerAdapter = new BaseFragmentAdapter <>( this );
        // collections
        // 用于ViewPager适配器
        List <Fragment> fragments = new ArrayList <>();
        //为适配器添加片段
        fragments.add( CollapsingFragment.newInstance() );
        fragments.add( TowFragment.newInstance() );
        fragments.add( DialogFragment.newInstance() );
        fragments.add( FPersonalData.newInstance() );
        //启用项目切换模式
        bnve.enableItemShiftingMode( false );
        //启用移动模式
        bnve.enableShiftingMode( false );
        //使动画
        bnve.enableAnimation( true );
        // set adapter
        VpAdapter adapter = new VpAdapter( getSupportFragmentManager(), fragments );
        vp.setAdapter( adapter );
        // 限制页面数量
        vp.setOffscreenPageLimit( adapter.getCount() );
    }

    /**
     * {@link KeyboardWatcher.SoftKeyboardStateListener}
     */
    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        bnve.setVisibility( View.GONE );
    }

    @Override
    public void onSoftKeyboardClosed() {
        bnve.setVisibility( View.VISIBLE );
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
    }

    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            //移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack( false );
            postDelayed( () -> {
                // 进行内存优化，销毁掉所有的界面
                ActivityStackManager.getInstance().finishAllActivities();
                // 销毁进程（请注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
                // System.exit(0);
            }, 300 );
        } else {
            toast( R.string.home_exit_hint );
        }
    }

    @Override
    protected void onDestroy() {
        if (this instanceof ViewPager.OnPageChangeListener && vp != null){
            vp.removeOnPageChangeListener( (ViewPager.OnPageChangeListener) this );
            vp.setAdapter( null );
            bnve.setOnNavigationItemSelectedListener( null );
        }
        super.onDestroy();
    }

}
