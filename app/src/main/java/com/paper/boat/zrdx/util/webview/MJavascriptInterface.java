package com.paper.boat.zrdx.util.webview;

import android.content.Context;
import android.util.Log;
import com.paper.boat.zrdx.util.image.ImageBrowseIntent;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Administrator on 2017/2/10.
 */

public class MJavascriptInterface {
    private Context context;
    private String[] imageUrls;
    private ArrayList <String> list;

    public MJavascriptInterface(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        list = new ArrayList <>();
        Collections.addAll( list, imageUrls );
        for (int i = 0; i < imageUrls.length; i++) {
            if (img.equals( imageUrls[i].replace( "amp;", "" ) ) || img.equals( imageUrls[i] )) {
                ImageBrowseIntent.showUrlImageBrowse( context, list, i );
            }
        }
        for (int i = 0; i < imageUrls.length; i++) {
            Log.e( "图片地址" + i, imageUrls[i].toString() );
        }
    }
}
