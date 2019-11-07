package com.paper.boat.zrdx.ui.actvity.web;

import android.content.Context;
import android.content.res.Resources;

import com.paper.boat.dream.R;

/**
 * Created by BrainWang on 05/01/2016.
 */

class ADFilterTool {

    /*过滤广告*/
    static boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray( R.array.adBlockUrl );
        for (String adUrl : adUrls) {
            if (url.contains( adUrl )) {
                return true;
            }
        }
        return false;
    }

}