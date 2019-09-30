package com.paper.boat.zrdx.util.string;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*文本表单验证*/
public class verify {

    /*验证手机号码*/
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        String s2 = "^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$";// 验证手机号
        if (!TextUtils.isEmpty( str )) {
            p = Pattern.compile( s2 );
            m = p.matcher( str );
            b = m.matches();
        }
        return b;
    }

    /**
     * 只校验正数 0-90.000000 0-180.000000 范围内
     * 经纬度校验
     * 经度longitude: (?:[0-9]|[1-9][0-9]|1[0-7][0-9]|180)\\.([0-9]{6})
     * 纬度latitude：  (?:[0-9]|[1-8][0-9]|90)\\.([0-9]{6})
     *
     * @return
     */
    public static boolean checkItude(String longitude, String latitude) {
        String reglo = "((?:[0-9]|[1-9][0-9]|1[0-7][0-9])\\.([0-9]{0,6}))|((?:180)\\.([0]{0,6}))";
        String regla = "((?:[0-9]|[1-8][0-9])\\.([0-9]{0,6}))|((?:90)\\.([0]{0,6}))";
        longitude = longitude.trim();
        latitude = latitude.trim();
        return longitude.matches( reglo ) == true ? latitude.matches( regla ) : false;
    }

}
