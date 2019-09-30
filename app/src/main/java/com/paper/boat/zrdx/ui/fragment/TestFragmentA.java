package com.paper.boat.zrdx.ui.fragment;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.BaseFragment;

public final class TestFragmentA extends BaseFragment {

    public static TestFragmentA newInstance() {
        return new TestFragmentA();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }


    @Override
    protected void init() {

    }

    @Override
    protected int getSuccessView() {
        return R.layout.fragment_test_a;
    }

    @Override
    protected Object requestData() {
        return "";
    }


}