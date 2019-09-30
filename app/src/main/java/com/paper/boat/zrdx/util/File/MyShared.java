package com.paper.boat.zrdx.util.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.paper.boat.zrdx.InitApp;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 轻量级存贮
 */
public class MyShared {
    private static final String FILE = "file_shared";
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String IS_LOGIN = "is_login";
    public static final String USER = "user";
    //记住密码
    public static final String PASSWORD = "password";
    public static final String Token = "to_ken";
    //安装标识
    public static final int INSTALL_PERMISS_CODE = 100;
    public static final String APK_FILE_PATH = "apk_file_path";

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private static final MyShared ourInstance = new MyShared( InitApp.getContext() );


    public static MyShared getInstance() {
        return ourInstance;
    }

    public MyShared(Context context) {
        mContext = context;
    }

    public void put(String key, Object object) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences( FILE, Context.MODE_PRIVATE );
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (object instanceof String) {
            editor.putString( key, (String) object );
        } else if (object instanceof Integer) {
            editor.putInt( key, (Integer) object );
        } else if (object instanceof Boolean) {
            editor.putBoolean( key, (Boolean) object );
        } else if (object instanceof Float) {
            editor.putFloat( key, (Float) object );
        } else if (object instanceof Long) {
            editor.putLong( key, (Long) object );
        } else {
            editor.putString( key, object.toString() );
        }

        SharedPreferencesCompat.apply( editor );
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public Object get(String key, Object defaultObject) {
        SharedPreferences sp = mContext.getSharedPreferences( FILE,
                Context.MODE_PRIVATE );

        if (defaultObject instanceof String) {
            return sp.getString( key, (String) defaultObject );
        } else if (defaultObject instanceof Integer) {
            return sp.getInt( key, (Integer) defaultObject );
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean( key, (Boolean) defaultObject );
        } else if (defaultObject instanceof Float) {
            return sp.getFloat( key, (Float) defaultObject );
        } else if (defaultObject instanceof Long) {
            return sp.getLong( key, (Long) defaultObject );
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(String key) {
        SharedPreferences sp = mContext.getSharedPreferences( FILE,
                Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sp.edit();
        editor.remove( key );
        SharedPreferencesCompat.apply( editor );
    }

    /**
     * 清除所有数据
     */
    public static void clearAll() {
        SharedPreferences sp = mContext.getSharedPreferences( FILE,
                Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply( editor );
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(String key) {
        SharedPreferences sp = mContext.getSharedPreferences( FILE,
                Context.MODE_PRIVATE );
        return sp.contains( key );
    }

    /**
     * 返回所有的键值对
     */
    public static Map <String, ?> getAll() {
        SharedPreferences sp = mContext.getSharedPreferences( FILE,
                Context.MODE_PRIVATE );
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod( "apply" );
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        private static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke( editor );
                    return;
                }
            } catch (Exception e) {
            }
            editor.commit();
        }
    }
}
