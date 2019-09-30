package com.paper.boat.zrdx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.BaseActivity;
import com.paper.boat.zrdx.common.BaseFragment;
import com.paper.boat.zrdx.ui.fragment.DialogFragment;
import com.paper.boat.zrdx.ui.fragment.FPersonalData;
import com.paper.boat.zrdx.ui.fragment.TestFragment;
import com.paper.boat.zrdx.ui.fragment.TowFtagment;
import com.paper.boat.zrdx.util.base.MyToast;
import com.paper.boat.zrdx.view.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView( R.id.bnve )
    BottomNavigationViewEx bnve;
    @BindView( R.id.vp )
    ViewPager vp;

    private VpAdapter adapter;
    // collections
    private List<Fragment> fragments;// 用于ViewPager适配器
    private static final String TAG = MainActivity.class.getSimpleName();
    //运行时权限
    @SuppressLint("InlinedApi")
    private String[] permissionArrays = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE //写存贮
            , Manifest.permission.READ_EXTERNAL_STORAGE //读取
            , Manifest.permission.INTERNET  //网络
            , Manifest.permission.ACCESS_FINE_LOCATION  //位置
            , Manifest.permission.READ_PHONE_STATE //电话状态
            , Manifest.permission.RECORD_AUDIO //录音
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        //权限申请
        initPermission();
        /*初始化界面*/
        initData();
        /*底部初始化*/
        init();
        /*底部按钮监听*/
        initEvent();
    }

    private void initPermission() {
        //当前版本大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //用于请求和检查针对Android M的应用程序的系统权限(API >= 23)。
            //调用上下文判断是否具有一组权限
            if (!EasyPermissions.hasPermissions( this, permissionArrays )) {
                MyToast.showToast( "请给予系统运行时权限..." );
                //未完全获取到权限  请求一组权限
                //第二个参数是被拒绝后再次申请该权限的解释，第三个参数是请求码，第四个参数是要申请的权限
                EasyPermissions.requestPermissions( this, "为让系统正常运行，请给予系统所有权限", 1401, permissionArrays );
            }
        }
    }

    private void initEvent() {

        // 设置监听器以在单击底部导航项时更改视图分页程序的当前项
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                if(previousPosition != position) {
                    vp.setCurrentItem(position, false);
                    previousPosition = position;
                 }
                return true;
            }
        });

        //设置监听器以在滚动视图页导航时更改当前选中的底部导航项
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
//                Log.i(TAG, "-----ViewPager-------- previous item:" + bind.bnve.getCurrentItem() + " current item:" + position + " ------------------");
//                if (position >= 2)// 2 is center
//                    position++;// if page is 2, need set bottom item to 3, and the same to 3 -> 4
                bnve.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void initData() {
        fragments = new ArrayList<>(4);

        // 创建备份片段并添加它A
        BaseFragment musicFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", getString(R.string.music));
        musicFragment.setArguments(bundle);

        // 创建备份片段并添加它
        TowFtagment backupFragment = new TowFtagment();
        bundle = new Bundle();
        bundle.putString("title", getString(R.string.backup));
        backupFragment.setArguments(bundle);

        // 创建备份片段并添加它
        BaseFragment favorFragment = new DialogFragment();
        bundle = new Bundle();
        bundle.putString("title", getString(R.string.favor));
        favorFragment.setArguments(bundle);

        // 创建备份片段并添加它
        BaseFragment visibilityFragment = new FPersonalData();
        bundle = new Bundle();
        bundle.putString("title", getString(R.string.visibility));
        visibilityFragment.setArguments(bundle);


        //为适配器添加片段
        fragments.add(musicFragment);
        fragments.add(backupFragment);
        fragments.add(favorFragment);
        fragments.add(visibilityFragment);
    }

    private void init() {
        //启用项目切换模式
        bnve.enableItemShiftingMode(false);
        //启用移动模式
        bnve.enableShiftingMode(false);
        //使动画
        bnve.enableAnimation(true);
        // set adapter
        adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List <String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List <String> perms) {

    }

    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        VpAdapter(FragmentManager fm, List <Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        }
    }


}
