package com.paper.boat.zrdx.ui.actvity.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.helper.InputTextHelper;
import com.paper.boat.zrdx.view.button.CountdownView;

import butterknife.BindView;
import butterknife.OnClick;

/*忘记密码*/
public class PasswordForgetActivity extends MyActivity {
    @BindView(R.id.et_password_forget_phone)
    EditText mPhoneView;
    @BindView(R.id.et_password_forget_code)
    EditText mCodeView;
    @BindView(R.id.cv_password_forget_countdown)
    CountdownView mCountdownView;
    @BindView(R.id.btn_password_forget_commit)
    Button mCommitView;

    @Override
    public void initView() {
        InputTextHelper.with( this)
                .addView(mPhoneView)
                .addView(mCodeView)
                .setMain(mCommitView)
                .setListener(new InputTextHelper.OnInputTextListener() {

                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return mPhoneView.getText().toString().length() == 11 && mCodeView.getText().toString().length() == 4;
                    }
                })
                .build();
    }

    @Override
    public void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_password_forget;
    }

    @OnClick({R.id.cv_password_forget_countdown, R.id.btn_password_forget_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_password_forget_countdown:
                if (mPhoneView.getText().toString().length() != 11) {
                    // 重置验证码倒计时控件
                    mCountdownView.resetState();
                    toast(R.string.common_phone_input_error);
                } else {
                    // 获取验证码
                    toast(R.string.common_code_send_hint);
                }
                break;
            case R.id.btn_password_forget_commit:
                // 重置密码
                startActivity( PasswordResetActivity.class).go();
                finish();
                break;
            default:
                break;
        }
    }
}
