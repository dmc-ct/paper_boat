package com.dream.zrdx.mylibrary;

import android.app.Application;
import android.widget.Toast;

public class MyModel {

    public static void helloWorld(Application connection){
        Toast.makeText(connection , "你好世界", Toast.LENGTH_SHORT ).show();
    }

}
