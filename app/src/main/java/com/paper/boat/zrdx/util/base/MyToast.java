package com.paper.boat.zrdx.util.base;

import android.util.Log;
import android.view.Gravity;

import com.hjq.toast.ToastUtils;

public class MyToast {
    private static final String TAG = "csx";

    public static void showToast(String message) {
//        Toast.makeText( InitApp.getContext(), message, Toast.LENGTH_SHORT).show();
        ToastUtils.setGravity( Gravity.BOTTOM, 0, 100 );
        ToastUtils.show( message );
    }

    public static void printLog(String message) {
        Log.e( TAG, message );
    }

}
