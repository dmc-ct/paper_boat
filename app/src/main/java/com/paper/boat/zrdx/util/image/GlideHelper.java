package com.paper.boat.zrdx.util.image;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.InitApp;

import static com.paper.boat.zrdx.Url.URL;

/**
 * Created by Kevin on 2017/8/22.
 */

public final class GlideHelper {
    private static RequestOptions option;

    /**
     * 加载图片
     */
    public static void load(ImageView mImg, String imgURL, int placeholderRes,Boolean local) {
        option = new RequestOptions()
                .error( placeholderRes )                    //加载错误之后的错误图
                .placeholder( placeholderRes )                //加载成功之前占位图
                .fallback( placeholderRes )                  //url为空的时候,显示的图片
                .skipMemoryCache( false );                         //跳过内存缓存
        if (local) {
            Glide.with( InitApp.getContext() ).load( imgURL )
                    .apply( option )
                    .into( mImg );
        } else {
            Glide.with( InitApp.getContext() ).load( URL + imgURL )
                    .apply( option )
                    .into( mImg );
        }
    }

    /**
     * 加载图片
     */
    public static void load(ImageView mImg, String imgURL,Boolean local) {
        option = new RequestOptions()
                .error( R.mipmap.image )                    //加载错误之后的错误图
                .skipMemoryCache( false );                         //跳过内存缓存
        if (local) {
            Glide.with( InitApp.getContext() ).load( imgURL )
                    .apply( option )
                    .into( mImg );
        } else {
            Glide.with( InitApp.getContext() ).load( URL + imgURL )
                    .apply( option )
                    .into( mImg );
        }
    }

    public static void load(final Activity activity, Object url, ImageView imageView, boolean isClickFinish) {
        Glide.with( activity )
                .load( url )
                .into( imageView );
        if (isClickFinish) {
            imageView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            } );
        }
    }
}
