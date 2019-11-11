package com.paper.boat.zrdx.ui.actvity.login;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.MainActivity;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.helper.InputTextHelper;
import com.paper.boat.zrdx.util.file.IntentKey;
import com.paper.boat.zrdx.view.edit.KeyboardWatcher;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends MyActivity implements KeyboardWatcher.SoftKeyboardStateListener {
    @BindView(R.id.iv_login_logo)
    ImageView mLogoView;

    @BindView(R.id.ll_login_body)
    LinearLayout mBodyLayout;
    @BindView(R.id.et_login_phone)
    EditText mPhoneView;
    @BindView(R.id.et_login_password)
    EditText mPasswordView;

    @BindView(R.id.btn_login_commit)
    Button mCommitView;

    @BindView(R.id.v_login_blank)
    View mBlankView;

    @BindView(R.id.ll_login_other)
    View mOtherView;
    @BindView(R.id.iv_login_qq)
    View mQQView;
    @BindView(R.id.iv_login_wx)
    View mWeChatView;

    /**
     * logo 缩放比例
     */
    private final float mLogoScale = 0.8f;
    /**
     * 动画时间
     */
    private final int mAnimTime = 300;

    @Override
    public void initView() {
        InputTextHelper.with( this )
                .addView( mPhoneView )
                .addView( mPasswordView )
                .setMain( mCommitView )
                .setListener( new InputTextHelper.OnInputTextListener() {
                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return mPhoneView.getText().toString().length() >= 1 &&
                                mPasswordView.getText().toString().length() >= 1;
                    }
                } )
                .build();

        post( () -> {
            // 因为在小屏幕手机上面，因为计算规则的因素会导致动画效果特别夸张，所以不在小屏幕手机上面展示这个动画效果
            if (mBlankView.getHeight() > mBodyLayout.getHeight()) {
                // 只有空白区域的高度大于登录框区域的高度才展示动画
                KeyboardWatcher.with( LoginActivity.this )
                        .setListener( LoginActivity.this );
            }
        } );
    }

    @Override
    public void initData() {

    }

    @Override
    public void onRightClick(View v) {
        // 跳转到注册界面
        startActivityForResult( RegisterActivity.class, new ActivityCallback() {
            @Override
            public void onActivityResult(int resultCode, @Nullable Intent data) {
                // 如果已经注册成功，就执行登录操作
                if (resultCode == RESULT_OK && data != null) {
                    mPhoneView.setText( data.getStringExtra( IntentKey.PHONE ) );
                    mPasswordView.setText( data.getStringExtra( IntentKey.PASSWORD ) );
                    onClick( mCommitView );
                }
            }
        } );
    }

    @OnClick({R.id.tv_login_forget, R.id.btn_login_commit, R.id.iv_login_qq, R.id.iv_login_wx})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_forget: /*忘记密码*/
                startActivity( PasswordForgetActivity.class ).go();
                break;
            case R.id.btn_login_commit: /*登陆*/
                showLoading();
                postDelayed( () -> {
                    showComplete();
                    showPrompt();
                    post( () -> {
                        // 处理登录
                        startActivity( MainActivity.class ).go();
                    } );
                }, 2000 );
                break;
            case R.id.iv_login_qq:
                toast( "点击了QQ登陆" );
                break;
            case R.id.iv_login_wx:
                toast( "点击了微信登陆" );
                break;
            default:
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    /*在软键盘上打开*/
    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int[] location = new int[2];
        // 获取这个 View 在屏幕中的坐标（左上角）
        mBodyLayout.getLocationOnScreen( location );
        //int x = location[0];
        int y = location[1];
        int bottom = screenHeight - (y + mBodyLayout.getHeight());
        if (keyboardHeight > bottom) {
            // 执行位移动画
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat( mBodyLayout, "translationY", 0, -(keyboardHeight - bottom) );
            objectAnimator.setDuration( mAnimTime );
            objectAnimator.setInterpolator( new AccelerateDecelerateInterpolator() );
            objectAnimator.start();

            // 执行缩小动画
            mLogoView.setPivotX( mLogoView.getWidth() / 2f );
            mLogoView.setPivotY( mLogoView.getHeight() );
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat( mLogoView, "scaleX", 1.0f, mLogoScale );
            ObjectAnimator scaleY = ObjectAnimator.ofFloat( mLogoView, "scaleY", 1.0f, mLogoScale );
            ObjectAnimator translationY = ObjectAnimator.ofFloat( mLogoView, "translationY", 0.0f, -(keyboardHeight - bottom) );
            animatorSet.play( translationY ).with( scaleX ).with( scaleY );
            animatorSet.setDuration( mAnimTime );
            animatorSet.start();
        }
    }

    /*在软键盘上关闭*/
    @Override
    public void onSoftKeyboardClosed() {
        // 执行位移动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat( mBodyLayout, "translationY", mBodyLayout.getTranslationY(), 0 );
        objectAnimator.setDuration( mAnimTime );
        objectAnimator.setInterpolator( new AccelerateDecelerateInterpolator() );
        objectAnimator.start();

        if (mLogoView.getTranslationY() == 0) {
            return;
        }
        // 执行放大动画
        mLogoView.setPivotX( mLogoView.getWidth() / 2f );
        mLogoView.setPivotY( mLogoView.getHeight() );
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat( mLogoView, "scaleX", mLogoScale, 1.0f );
        ObjectAnimator scaleY = ObjectAnimator.ofFloat( mLogoView, "scaleY", mLogoScale, 1.0f );
        ObjectAnimator translationY = ObjectAnimator.ofFloat( mLogoView, "translationY", mLogoView.getTranslationY(), 0 );
        animatorSet.play( translationY ).with( scaleX ).with( scaleY );
        animatorSet.setDuration( mAnimTime );
        animatorSet.start();
    }
}
