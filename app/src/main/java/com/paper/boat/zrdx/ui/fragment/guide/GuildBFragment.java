package com.paper.boat.zrdx.ui.fragment.guide;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.widget.TextView;

import com.gyf.immersionbar.BarHide;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.MainActivity;
import com.paper.boat.zrdx.common.MyLazyFragment;
import com.paper.boat.zrdx.view.layout.CustomVideoView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class GuildBFragment extends MyLazyFragment {
    @BindView(R.id.video_view)
    CustomVideoView videoView;
    @BindView(R.id.tv_guide_skip)
    TextView tv_guide_skip;
    private int date = 6;
    private Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            post( () -> {
                if (date > 0) {
                    date --;
                }else if (date == 0){
                    videoView.stopPlayback();
                    timer.cancel();
                    startActivityFinish( MainActivity.class );
                }
                tv_guide_skip.setText( date + "  跳过" );
            } );
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guild_b;
    }

    @Override
    protected void initView() {
        // 设置状态栏和导航栏参数
        statusBarConfig()
                // 有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .fullScreen( true )
                // 隐藏状态栏
                .hideBar( BarHide.FLAG_HIDE_STATUS_BAR )
                // 透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .transparentNavigationBar()
                .init();
        timer.schedule( task, 0, 1000 );//启动定时器
    }

    @Override
    protected void initData() {
        Uri uri = Uri.parse( "android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_1 );
        /**播放视频**/
        videoView.playVideo( uri );
        tv_guide_skip.setOnClickListener( view -> {
            videoView.stopPlayback();
            startActivityFinish( MainActivity.class );
        } );
        videoView.setOnClickListener( view -> toast( "点击了视频" ) );
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel(); // 该命用于以销毁定时器，一般可以在onStop里面调用
    }

    /*销毁视频*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
