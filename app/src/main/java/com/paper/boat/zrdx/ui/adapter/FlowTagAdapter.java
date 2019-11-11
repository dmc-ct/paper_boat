package com.paper.boat.zrdx.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.paper.boat.dream.R;


/**
 * @author xuexiang
 * @date 2017/11/21 上午10:44
 */
public class FlowTagAdapter extends BaseTagAdapter<String, TextView> {

    public FlowTagAdapter(Context context) {
        super(context);
    }

    @Override
    protected TextView newViewHolder(View convertView) {
        return (TextView) convertView.findViewById( R.id.tv_tag_item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.xui_adapter_default_flow_tag_item;
    }

    @Override
    protected void convert(TextView textView, String item, int position) {
        textView.setText(item);
    }
}
