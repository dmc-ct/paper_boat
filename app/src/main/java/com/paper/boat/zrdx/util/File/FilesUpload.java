package com.paper.boat.zrdx.util.File;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import androidx.fragment.app.Fragment;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.InitApp;
import com.paper.boat.zrdx.ui.adapter.GridImageAdapter;
import com.paper.boat.zrdx.util.base.MyToast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import id.zelory.compressor.Compressor;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 文件上传
 */
public class FilesUpload {

    /**
     * Activity
     *
     * @param activity           当前页面
     * @param max                最大数量
     * @param Single_or_multiple 单选或多选
     * @param mode               拍照或相册
     * @param selectList         媒体对象
     * @param back_logo          回传标识
     * @param tailor             是否剪裁
     */
    public static GridImageAdapter.onAddPicClickListener getActivityFilesUpload(Activity activity, int max, boolean Single_or_multiple
            , boolean mode, List <LocalMedia> selectList, int back_logo, boolean tailor) {

        GridImageAdapter.onAddPicClickListener onAddPicClickListener = () -> {
            int themeId = R.style.picture_white_style;
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create( activity )
                        .openGallery( PictureMimeType.ofImage() )// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme( themeId )// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum( max )// 最大图片选择数量
                        .minSelectNum( 1 )// 最小选择数量
                        .imageSpanCount( 4 )// 每行显示个数(选择内部)
                        .selectionMode( Single_or_multiple ?
                                PictureConfig.MULTIPLE : PictureConfig.SINGLE )// 多选 or 单选 true为多false为单
                        .previewImage( true )// 是否可预览图片
                        .previewVideo( true )// 是否可预览视频
                        .enablePreviewAudio( true ) // 是否可播放音频
                        .isCamera( true )// 是否显示拍照按钮
                        .isZoomAnim( true )// 图片列表点击 缩放效果 默认true
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                        .enableCrop( tailor )// 是否裁剪
                        .compress( true )// 是否压缩
                        .synOrAsy( true )//同步true或异步false 压缩 默认同步
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride( 160, 160 )// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio( 3, 2 )// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls( false )// 是否显示uCrop工具栏，默认不显示
                        .isGif( false )// 是否显示gif图片
                        .freeStyleCropEnabled( true )// 裁剪框是否可拖拽
                        .circleDimmedLayer( false )// 是否圆形裁剪
                        .showCropFrame( false )// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid( false )// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound( false )// 是否开启点击声音
                        .selectionMedia( selectList )// 是否传入已选图片
                        //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                        .previewEggs( true )// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .minimumCompressSize( 100 )// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled(true) // 裁剪是否可旋转图片
                        //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()//显示多少秒以内的视频or音频也可适用
                        //.recordVideoSecond()//录制视频秒数 默认60s
                        .forResult( back_logo );//结果回调onActivityResult code
            } else {
                // 单独拍照
                PictureSelector.create( activity )
                        .openCamera( PictureMimeType.ofImage() )// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                        .theme( themeId )// 主题样式设置 具体参考 values/styles
                        .maxSelectNum( max )// 最大图片选择数量
                        .minSelectNum( 1 )// 最小选择数量
                        .selectionMode( Single_or_multiple ?
                                PictureConfig.MULTIPLE : PictureConfig.SINGLE )// 多选 or 单选
                        .previewImage( true )// 是否可预览图片
                        .previewVideo( true )// 是否可预览视频
                        .enablePreviewAudio( true ) // 是否可播放音频
                        .isCamera( true )// 是否显示拍照按钮
                        .enableCrop( tailor )// 是否裁剪
                        .compress( true )// 是否压缩
                        .glideOverride( 160, 160 )// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio( 3, 2 )// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls( false )// 是否显示uCrop工具栏，默认不显示
                        .isGif( false )// 是否显示gif图片
                        .freeStyleCropEnabled( true )// 裁剪框是否可拖拽
                        .circleDimmedLayer( false )// 是否圆形裁剪
                        .showCropFrame( false )// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid( false )// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound( false )// 是否开启点击声音
                        .selectionMedia( selectList )// 是否传入已选图片
                        .previewEggs( true )//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
                        .minimumCompressSize( 100 )// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled() // 裁剪是否可旋转图片
                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()////显示多少秒以内的视频or音频也可适用
                        .forResult( back_logo );//结果回调onActivityResult code
            }
        };

        return onAddPicClickListener;
    }

    /**
     * Activity
     *
     * @param activity           当前页面
     * @param max                最大数量
     * @param Single_or_multiple 单选或多选
     * @param mode               拍照或相册
     * @param selectList         媒体对象
     * @param back_logo          回传标识
     * @param tailor             是否剪裁
     */
    public static GridImageAdapter.onAddPicClickListener getActivityFilesUploadFrm(Fragment activity, int max, boolean Single_or_multiple
            , boolean mode, List <LocalMedia> selectList, int back_logo, boolean tailor) {

        GridImageAdapter.onAddPicClickListener onAddPicClickListener = () -> {
            int themeId = R.style.picture_white_style;
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create( activity )
                        .openGallery( PictureMimeType.ofImage() )// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme( themeId )// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum( max )// 最大图片选择数量
                        .minSelectNum( 1 )// 最小选择数量
                        .imageSpanCount( 4 )// 每行显示个数(选择内部)
                        .selectionMode( Single_or_multiple ?
                                PictureConfig.MULTIPLE : PictureConfig.SINGLE )// 多选 or 单选 true为多false为单
                        .previewImage( true )// 是否可预览图片
                        .previewVideo( true )// 是否可预览视频
                        .enablePreviewAudio( true ) // 是否可播放音频
                        .isCamera( true )// 是否显示拍照按钮
                        .isZoomAnim( true )// 图片列表点击 缩放效果 默认true
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                        .enableCrop( tailor )// 是否裁剪
                        .compress( true )// 是否压缩
                        .synOrAsy( true )//同步true或异步false 压缩 默认同步
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride( 160, 160 )// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio( 3, 2 )// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls( false )// 是否显示uCrop工具栏，默认不显示
                        .isGif( false )// 是否显示gif图片
                        .freeStyleCropEnabled( true )// 裁剪框是否可拖拽
                        .circleDimmedLayer( false )// 是否圆形裁剪
                        .showCropFrame( false )// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid( false )// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound( false )// 是否开启点击声音
                        .selectionMedia( selectList )// 是否传入已选图片
                        //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                        .previewEggs( true )// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .minimumCompressSize( 100 )// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled(true) // 裁剪是否可旋转图片
                        //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()//显示多少秒以内的视频or音频也可适用
                        //.recordVideoSecond()//录制视频秒数 默认60s
                        .forResult( back_logo );//结果回调onActivityResult code
            } else {
                // 单独拍照
                PictureSelector.create( activity )
                        .openCamera( PictureMimeType.ofImage() )// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                        .theme( themeId )// 主题样式设置 具体参考 values/styles
                        .maxSelectNum( max )// 最大图片选择数量
                        .minSelectNum( 1 )// 最小选择数量
                        .selectionMode( Single_or_multiple ?
                                PictureConfig.MULTIPLE : PictureConfig.SINGLE )// 多选 or 单选
                        .previewImage( true )// 是否可预览图片
                        .previewVideo( true )// 是否可预览视频
                        .enablePreviewAudio( true ) // 是否可播放音频
                        .isCamera( true )// 是否显示拍照按钮
                        .enableCrop( tailor )// 是否裁剪
                        .compress( true )// 是否压缩
                        .glideOverride( 160, 160 )// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio( 3, 2 )// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls( false )// 是否显示uCrop工具栏，默认不显示
                        .isGif( false )// 是否显示gif图片
                        .freeStyleCropEnabled( true )// 裁剪框是否可拖拽
                        .circleDimmedLayer( false )// 是否圆形裁剪
                        .showCropFrame( false )// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid( false )// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound( false )// 是否开启点击声音
                        .selectionMedia( selectList )// 是否传入已选图片
                        .previewEggs( true )//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
                        .minimumCompressSize( 100 )// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled() // 裁剪是否可旋转图片
                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()////显示多少秒以内的视频or音频也可适用
                        .forResult( back_logo );//结果回调onActivityResult code
            }
        };

        return onAddPicClickListener;
    }

    /**
     * 清空缓存
     */
    public static void clear_cache(Activity activity) {
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

    /**
     * 图片压缩
     */
    public static File photo_compression(Context context, File file, Boolean bool) {
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

}
