package com.paper.boat.zrdx.util.base;

import android.util.Log;

import androidx.annotation.StringRes;

import com.hjq.toast.ToastUtils;

public class MyToast {
    private static final String TAG = "csx";

    public static void showToast(String message) {
        ToastUtils.show( message );
    }

    public static void show(String message) {
        ToastUtils.show( message );
    }

    public static void show(CharSequence text) {
        ToastUtils.show( text );
    }

    public static void show(@StringRes int id) {
        ToastUtils.show( id );
    }

    public static void show(Object object) {
        ToastUtils.show( object );
    }

    public static void printLog(String message) {
        Log.e( TAG, message );
    }

}
