package com.paper.boat.zrdx.ui.actvity.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.helper.InputTextHelper;

import butterknife.BindView;
import butterknife.OnClick;

/*重置密码*/
public class PasswordResetActivity extends MyActivity {

    @BindView(R.id.et_password_reset_password1)
    EditText mPasswordView1;
    @BindView(R.id.et_password_reset_password2)
    EditText mPasswordView2;
    @BindView(R.id.btn_password_reset_commit)
    Button mCommitView;

    @Override
    public void initView() {
        InputTextHelper.with( this)
                .addView(mPasswordView1)
                .addView(mPasswordView2)
                .setMain(mCommitView)
                .setListener(new InputTextHelper.OnInputTextListener() {

                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return mPasswordView1.getText().toString().length() >= 6 &&
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
        return R.layout.activity_password_reset;
    }

    @OnClick({R.id.btn_password_reset_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_password_reset_commit:
                toast(R.string.password_reset_success);
                finish();
                break;
            default:
                break;
        }
    }
}
