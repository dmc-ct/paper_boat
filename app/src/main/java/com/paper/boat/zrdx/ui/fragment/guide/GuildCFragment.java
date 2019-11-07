package com.paper.boat.zrdx.ui.fragment.guide;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.gyf.immersionbar.BarHide;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.MainActivity;
import com.paper.boat.zrdx.common.MyLazyFragment;
import com.paper.boat.zrdx.util.base.AppConfig;

import java.util.List;

import butterknife.BindView;

public class GuildCFragment extends MyLazyFragment
        implements OnPermission, Animation.AnimationListener {
    private static final int ANIM_TIME = 1000;

    @BindView(R.id.iv_splash_bg)
    View mImageView;
    @BindView(R.id.iv_splash_icon)
    View mIconView;
    @BindView(R.id.iv_splash_name)
    View mNameView;

    @BindView(R.id.tv_splash_debug)
    View mDebugView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guild_c;
    }

    @Override
    protected void initView() {
        // 初始化动画
        AlphaAnimation aa = new AlphaAnimation( 0.4f, 1.0f );
        aa.setDuration( ANIM_TIME * 2 );
        aa.setAnimationListener( this );
        mImageView.startAnimation( aa );

        /*比例动画*/
        ScaleAnimation sa = new ScaleAnimation( 0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
        sa.setDuration( ANIM_TIME );
        mIconView.startAnimation( sa );

        /*旋转动画*/
        RotateAnimation ra = new RotateAnimation( 180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
        ra.setDuration( ANIM_TIME );
        mNameView.startAnimation( ra );

        // 设置状态栏和导航栏参数
        statusBarConfig()
                // 有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .fullScreen( true )
                // 隐藏状态栏
                .hideBar( BarHide.FLAG_HIDE_STATUS_BAR )
                // 透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .transparentNavigationBar()
                .init();
    }

    @Override
    protected void initData() {
        if (AppConfig.isDebug()) {
            mDebugView.setVisibility( View.VISIBLE );
        } else {
            mDebugView.setVisibility( View.INVISIBLE );
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        requestPermission();
    }

    private void requestPermission() {
        XXPermissions.with( getAttachActivity() )
                .permission( Permission.Group.STORAGE )
                .request( this );
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void hasPermission(List <String> granted, boolean isAll) {
        startActivityFinish( MainActivity.class );
    }

    @Override
    public void noPermission(List <String> denied, boolean quick) {
        if (quick) {
            toast( R.string.common_permission_fail );
            XXPermissions.gotoPermissionSettings( getContext(), true );
        } else {
            toast( R.string.common_permission_hint );
            postDelayed( this::requestPermission, 1000 );
        }
    }
}
