package com.paper.boat.zrdx.ui.fragment;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.ui.actvity_utill.SelectPositionActiviy;
import com.paper.boat.zrdx.common.BaseFragment;

import butterknife.BindView;


/**
 * Created by yu on 2016/11/11.
 */

public class TestFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.textView2)
    TextView textView;
    @BindView( R.id.button )
    Button button;
    private String title;

    @Override
    protected void init() {
        textView.setText( "基础包" );
        button.setOnClickListener( this );
    }

    @Override
    protected int getSuccessView() {
        return R.layout.frag_base;
    }

    @Override
    protected Object requestData() {
        return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                startActivity( SelectPositionActiviy.class ).goForResult( 1001 );
                break;
        }
    }
}
