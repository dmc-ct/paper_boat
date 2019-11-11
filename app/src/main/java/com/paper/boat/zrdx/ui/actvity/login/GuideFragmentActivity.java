package com.paper.boat.zrdx.ui.actvity.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.common.MyLazyFragment;
import com.paper.boat.zrdx.ui.fragment.guide.GuildAFragment;
import com.paper.boat.zrdx.ui.fragment.guide.GuildBFragment;
import com.paper.boat.zrdx.ui.fragment.guide.GuildCFragment;
import com.paper.boat.zrdx.util.file.IntentKey;
import com.paper.boat.zrdx.util.file.MyShared;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GuideFragmentActivity extends MyActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List <Fragment> fragmentList;
    private Bundle bundle = new Bundle();
    private MyLazyFragment myLazyFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gride_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        fragmentList = new ArrayList <>();

        IntentKey.GUIDE_ID_SING = (int)MyShared.getInstance().get( IntentKey.STATUS,0 );

        switch (IntentKey.GUIDE_ID_SING) {
            case 1:
                myLazyFragment = new GuildBFragment();
                break;
            case 2:
                myLazyFragment = new GuildCFragment();
                break;
            case 0:
            default:
                myLazyFragment = new GuildAFragment();
                break;
        }

        //        bundle.putInt( "guide", 2 );
        //        myLazyFragment.setArguments( bundle );

        fragmentList.add( myLazyFragment );
        viewPager.setOffscreenPageLimit( 1 );
        viewPager.setAdapter( new FragmentPagerAdapter( getSupportFragmentManager() ) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get( position );
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        } );
    }


}
