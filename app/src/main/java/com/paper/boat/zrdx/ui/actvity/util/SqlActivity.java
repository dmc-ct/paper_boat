package com.paper.boat.zrdx.ui.actvity.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;

import java.util.HashMap;

public class SqlActivity extends MyActivity {
    private Button btn_get_data;
    private TextView tv_data;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0x11:
                case 0x12:
                    String s = (String) msg.obj;
                    tv_data.setText( s );
                    break;
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sql;
    }

    @Override
    protected void initView() {

        // 控件的初始化
        btn_get_data = findViewById( R.id.btn_get_data );
        tv_data = findViewById( R.id.tv_data );

//        Connection connection = getConn( "dmc");

        setListener();
    }

    /**
     * 设置监听
     */
    private void setListener() {

        // 按钮点击事件
        btn_get_data.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 创建一个线程来连接数据库并获取数据库中对应表的数据
                new Thread( new Runnable() {
                    @Override
                    public void run() {
                        // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                        HashMap <String, Object> map = DBUtils.getInfoByName( "Charger9527" );
                        Message message = handler.obtainMessage();
//                        MyToast.printLog( map + "" );
                        if (map != null) {
                            String s = "";
                            for (String key : map.keySet()) {
                                s += key + ":" + map.get( key ) + "\n";
                            }
                            message.what = 0x12;
                            message.obj = s;
                        } else {
                            message.what = 0x11;
                            message.obj = "查询结果为空";
                        }
                        // 发消息通知主线程更新UI
                        handler.sendMessage( message );
                    }
                } ).start();

            }
        } );

    }

    @Override
    protected void initData() {

    }
}
