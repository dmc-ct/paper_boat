package com.paper.boat.zrdx.ui.actvity.personal;

import android.view.Gravity;
import android.view.View;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.helper.ActivityStackManager;
import com.paper.boat.zrdx.helper.CacheDataManager;
import com.paper.boat.zrdx.ui.actvity.login.LoginActivity;
import com.paper.boat.zrdx.util.File.IntentKey;
import com.paper.boat.zrdx.util.File.MyShared;
import com.paper.boat.zrdx.view.button.SwitchButton;
import com.paper.boat.zrdx.view.dialog.BaseDialog;
import com.paper.boat.zrdx.view.dialog.MenuDialog;
import com.paper.boat.zrdx.view.dialog.ToastDialog;
import com.paper.boat.zrdx.view.layout.SettingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*设置界面*/
public class SettingActivity extends MyActivity implements SwitchButton.OnCheckedChangeListener {
    @BindView(R.id.sb_setting_cache)
    SettingBar mCleanCacheView;

    @BindView(R.id.sb_setting_auto)
    SettingBar mAutoLoginView;
    @BindView(R.id.sb_setting_switch)
    SwitchButton mAutoSwitchView;
    @BindView(R.id.sb_setting_guide)
    SettingBar mSb_setting_guide;

    @Override
    public void initView() {
        // 设置切换按钮的监听
        mAutoSwitchView.setOnCheckedChangeListener( this );
        /*主题子样选择*/
        switch ((int) MyShared.getInstance().get( IntentKey.STATUS, 0 )) {
            case 1:
                mSb_setting_guide.setLeftText( "主题二" );
                break;
            case 2:
                mSb_setting_guide.setLeftText( "主题三" );
                break;
            case 0:
            default:
                mSb_setting_guide.setLeftText( "主题一" );
                break;
        }
    }

    @Override
    public void initData() {
        // 获取应用缓存大小
        mCleanCacheView.setRightText( CacheDataManager.getTotalCacheSize( this ) );
        /*是否存在记住密码*/
        if (myShared.contains( IntentKey.REMEMBER_PASSWORD )) {
            mAutoSwitchView.setChecked( (Boolean) myShared.get( IntentKey.REMEMBER_PASSWORD, false ) );
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @OnClick({R.id.sb_setting_language, R.id.sb_setting_update, R.id.sb_setting_agreement, R.id.sb_setting_about,
            R.id.sb_setting_cache, R.id.sb_setting_auto, R.id.sb_setting_exit, R.id.sb_setting_guide})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sb_setting_language: /*语言切换*/

                break;
            case R.id.sb_setting_update: /*检查更新*/

                break;
            case R.id.sb_setting_agreement:  /*隐私*/

                break;
            case R.id.sb_setting_guide: /*主题*/
                List <String> list = new ArrayList <>();
                list.add( "主题一" );
                list.add( "主题二" );
                list.add( "主题三" );
                new MenuDialog.Builder( this )
                        .setGravity( Gravity.CENTER )
                        .setList( list )
                        .setListener( new MenuDialog.OnListener <String>() {
                            @Override
                            public void onSelected(BaseDialog dialog, int position, String string) {
                                mSb_setting_guide.setLeftText( string );
                                MyShared.getInstance().put( IntentKey.STATUS, position );
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast( "取消了" );
                            }
                        } )
                        .show();
                break;
            case R.id.sb_setting_about:  /*关于*/
                startActivity( AboutActivity.class ).go();
                break;
            case R.id.sb_setting_auto: /*switch*/
                // 自动登录
                mAutoSwitchView.setChecked( !mAutoSwitchView.isChecked() );
                break;
            case R.id.sb_setting_cache:
                // 清空缓存
//                ImageLoader.clear(this);
                String Cache = CacheDataManager.getTotalCacheSize( this );
                if (Cache.equals( "0KB" )) {
                    post( () -> showPrompt( ToastDialog.Type.ERROR, "暂无可清理文件" ) );
                    return;
                }
                showLoading( "清理中...." );
                postDelayed( () -> {
                    // 重新获取应用缓存大小
                    showComplete();
                    showPrompt( "以清理: " + Cache );
                    CacheDataManager.clearAllCache( this );
                    mCleanCacheView.setRightText( CacheDataManager.getTotalCacheSize( this ) );
                }, 500 );
                break;
            case R.id.sb_setting_exit: /*退出登录*/
                // 退出登录
                startActivity( LoginActivity.class ).go();
                /*清空所有的存贮文件*/
                myShared.clearAll();
                // 进行内存优化，销毁掉所有的界面
                ActivityStackManager.getInstance().finishAllActivities( LoginActivity.class );
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(SwitchButton button, boolean isChecked) {
        myShared.put( IntentKey.REMEMBER_PASSWORD, isChecked );
    }
}
