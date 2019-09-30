package com.paper.boat.zrdx.util.string;

/*字符串的截取*/
public class InterceptString {

    /*截取从str开始到end的字符*/
    public static String substring(String sig, int str, int end) {
        return sig.substring( str, end );
    }

    /*是否包含字符串*/
    public static Boolean contain(String str, String aims) {
        return str.contains( aims );
    }

    /*获取字符串开始的位置*/
    public static int position(String str, String aims) {
        return str.indexOf( aims );
    }

}
