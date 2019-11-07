package com.paper.boat.zrdx.util.File;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.paper.boat.zrdx.InitApp;
import com.paper.boat.zrdx.util.base.MyToast;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FileTool {

    /**
     * 图片压缩
     */
    public File photo_compression(Context context, File file, Boolean bool) {
        File photo_file = null;
        try {
            if (bool) {
                //普通压缩
                photo_file = new Compressor( context ).compressToFile( file );
            } else {
                //自定义压缩
                photo_file = new Compressor( context )
                        .setMaxWidth( 640 ) //设置最大宽度
                        .setMaxHeight( 480 )  //设置最大高度
                        .setQuality( 75 ) //设置质量
                        .setCompressFormat( Bitmap.CompressFormat.WEBP ) //设置压缩格式
                        .setDestinationDirectoryPath( Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES ).getAbsolutePath() ) //设置目标路径
                        .compressToFile( file );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo_file;
    }

    /**
     * 清空缓存
     */
    public void clear_cache(Activity activity) {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions( activity );
        permissions.request( Manifest.permission.WRITE_EXTERNAL_STORAGE ).subscribe( new Observer <Boolean>() {
            //在订阅
            @Override
            public void onSubscribe(Disposable d) {
            }

            //准备
            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    //删除缓存目录文件
                    PictureFileUtils.deleteCacheDirFile( InitApp.getContext() );
                } else {
                    MyToast.showToast( "读取内存卡权限被拒绝" );
                }
            }

            //异常
            @Override
            public void onError(Throwable e) {
            }

            //弹出层加载显示内容后事件
            @Override
            public void onComplete() {
            }
        } );
    }

}
