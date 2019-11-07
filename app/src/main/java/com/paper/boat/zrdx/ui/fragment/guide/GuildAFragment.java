package com.paper.boat.zrdx.ui.fragment.guide;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.MainActivity;
import com.paper.boat.zrdx.common.MyLazyFragment;
import com.paper.boat.zrdx.ui.actvity.login.LoginActivity;
import com.paper.boat.zrdx.util.File.IntentKey;
import com.paper.boat.zrdx.util.base.MyToast;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

public class GuildAFragment extends MyLazyFragment {
    @BindView(R.id.banner_guide_background)
    BGABanner mBannerGuideBackground;
    @BindView(R.id.banner_guide_foreground)
    BGABanner mBannerGuideForeground;
    @BindView(R.id.tv_guide_skip)
    TextView mTvGuideSkip;
    @BindView(R.id.btn_guide_enter)
    Button mBtnGuideEnter;

    @Override
    protected int getLayoutId() {
        /**获取参数，根据不同的参数播放不同的视频**/
//        int index = getArguments().getInt( "guide" );
        return R.layout.fragment_guild_a;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        /*设置监听器*/
        setListener();
        /*设置数据源*/
        processLogic();
    }

    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mBannerGuideForeground.setEnterSkipViewIdAndDelegate( R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                /*记住密码*/
                if (myShared.contains( IntentKey.REMEMBER_PASSWORD ) && (Boolean) myShared.get( IntentKey.REMEMBER_PASSWORD, false )) {
                    startActivity( MainActivity.class ).go();
                } else {
                    startActivity( LoginActivity.class ).go();
                }
                finish();
            }
        } );
        /*广告点击事件*/
        mBannerGuideBackground.setDelegate( new BGABanner.Delegate <ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                MyToast.showToast( "点击了" + position );
            }
        } );
    }

    private void processLogic() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize( 720, 1280, 320, 640 );
        // 设置数据源
        mBannerGuideBackground.setData( localImageSize, ImageView.ScaleType.CENTER_CROP,
                                        R.mipmap.uoko_guide_background_1,
                                        R.mipmap.uoko_guide_background_2,
                                        R.mipmap.uoko_guide_background_3 );

        mBannerGuideForeground.setData( localImageSize, ImageView.ScaleType.FIT_XY,
                                        R.drawable.uoko_guide_foreground_1,
                                        R.drawable.uoko_guide_foreground_2,
                                        R.drawable.uoko_guide_foreground_3 );

//        mBannerGuideBackground.setData( Arrays.asList( "网络图片路径1", "网络图片路径2", "网络图片路径3")
//                , Arrays.asList( "提示文字1", "提示文字2", "提示文字3"));
//        mBannerGuideBackground.setAdapter(new BGABanner.Adapter<ImageView, String>() {
//            @Override
//            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
//                GlideHelper.load( itemView, model,true);
//            }
//        });

    }
}
