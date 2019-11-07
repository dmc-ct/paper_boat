package com.paper.boat.zrdx.ui.actvity.login;

import android.widget.Button;
import android.widget.EditText;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.helper.InputTextHelper;
import com.paper.boat.zrdx.view.button.CountdownView;

import butterknife.BindView;

/*注册界面*/
public class RegisterActivity extends MyActivity {

    @BindView(R.id.et_register_phone)
    EditText mPhoneView;
    @BindView(R.id.cv_register_countdown)
    CountdownView mCountdownView;

    @BindView(R.id.et_register_code)
    EditText mCodeView;

    @BindView(R.id.et_register_password1)
    EditText mPasswordView1;
    @BindView(R.id.et_register_password2)
    EditText mPasswordView2;

    @BindView(R.id.btn_register_commit)
    Button mCommitView;

    @Override
    public void initView() {
        InputTextHelper.with( this)
                .addView(mPhoneView)
                .addView(mCodeView)
                .addView(mPasswordView1)
                .addView(mPasswordView2)
                .setMain(mCommitView)
                .setListener(new InputTextHelper.OnInputTextListener() {
                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return mPhoneView.getText().toString().length() == 11 &&
                                mPasswordView1.getText().toString().length() >= 6 &&
                                mPasswordView1.getText().toString().equals(mPasswordView2.getText().toString());
                    }
                })
                .build();
    }

    @Override
    public void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }
}
