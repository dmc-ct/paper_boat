package com.paper.boat.zrdx.util.image;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
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
    public static void load(ImageView mImg, String imgURL, int placeholderRes, Boolean local) {
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
    public static void load(ImageView mImg, String imgURL, Boolean local) {

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

    /*圆角*/
    public static void loadBanner(ImageView mImg, String imgURL, Boolean local) {
        option = new RequestOptions()
                .error( R.mipmap.image )                    //加载错误之后的错误图
                .bitmapTransform( new RoundedCorners( 8 ) )//图片圆角为30
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

    /*圆角本地*/
    public static RoundedBitmapDrawable rectRoundBitmap(Bitmap bitmap) {
        //创建RoundedBitmapDrawable对象
        RoundedBitmapDrawable roundImg = RoundedBitmapDrawableFactory.create( InitApp.getContext().getResources(), bitmap );
        //抗锯齿
        roundImg.setAntiAlias( true );
        //设置圆角半径
        roundImg.setCornerRadius( 8 );
        return roundImg;
    }

    /*圆角本地*/
    public static RoundedBitmapDrawable rectRoundBitmap( Resources resource, int id) {
        Bitmap image = BitmapFactory.decodeResource( resource, id );
        //创建RoundedBitmapDrawable对象
        RoundedBitmapDrawable roundImg = RoundedBitmapDrawableFactory.create( InitApp.getContext().getResources(), image );
        //抗锯齿
        roundImg.setAntiAlias( true );
        //设置圆角半径
        roundImg.setCornerRadius( 8 );
        return roundImg;
    }
}
