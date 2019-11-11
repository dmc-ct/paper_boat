package com.paper.boat.zrdx.util.file;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.InitApp;
import com.paper.boat.zrdx.ui.adapter.GridImageAdapter;

import java.util.List;

// 例如 LocalMedia 里面返回三种path
// 1.media.getPath(); 为原图path
// 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
// 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
// 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
public class AlbumUtil {
    private int select = 1; /*单选或多选*/
    private int max_amount = 2; /*最多选择数量*/
    private int min_amount = 1; /*最少选择数量*/
    private boolean cutOut = false; /*裁剪*/
    private boolean RoundCut = false; /*圆形剪裁，默认方形*/
    private PictureSelector pictureSelector;
    private PictureSelectionModel pictureSelectionModel;
    private int type = PictureMimeType.ofImage();
    private int themeId = R.style.picture_QQ_style;

    public AlbumUtil(Fragment fr) {
        this.pictureSelector = PictureSelector.create( fr );
    }

    public AlbumUtil(Activity ac) {
        this.pictureSelector = PictureSelector.create( ac );
    }

    // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
    public AlbumUtil Type(int i) {
        type = i;
        return this;
    }

    /*是否裁剪*/
    public AlbumUtil CutOut(Boolean Select) {
        this.cutOut = Select;
        return this;
    }

    /*是否圆形裁剪*/
    public AlbumUtil RoundCut(Boolean Select) {
        this.RoundCut = Select;
        return this;
    }

    /*单选或多选*/
    public AlbumUtil singleAndDouble(int i) {
        this.select = i;
        return this;
    }

    /*最多选择数量*/
    public AlbumUtil MaxAmount(int i) {
        this.max_amount = i;
        return this;
    }

    /*最少选择数量*/
    public AlbumUtil MinAmount(int i) {
        this.max_amount = i;
        return this;
    }

    /*相机*/
    public GridImageAdapter.onAddPicClickListener Photo(List <LocalMedia> selectList, int i) {
        GridImageAdapter.onAddPicClickListener onAddPicClickListener;
        onAddPicClickListener = () -> {
            InitApp.getHandler().post( () -> {
                pictureSelectionModel = pictureSelector.openCamera( type );
                pictureSelectionModel.isCamera( true )// 是否显示拍照按钮
                        .theme(themeId)// 主题样式设置 具体参考 values/styles
                        .compress( true )// 是否压缩
                        .selectionMedia( selectList )// 是否传入已选图片
                        .previewEggs( true )//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        .minimumCompressSize( 100 )// 小于100kb的图片不压缩
                        .openClickSound( false )// 是否开启点击声音
                        .forResult( i );//结果回调onActivityResult code
                /*裁剪*/
                if (cutOut) {
                    RoundCut( pictureSelectionModel );
                }
            } );
        };
        return onAddPicClickListener;
    }

    /*相册*/
    public GridImageAdapter.onAddPicClickListener Album(List <LocalMedia> selectList, int i) {
        GridImageAdapter.onAddPicClickListener onAddPicClickListener;
        onAddPicClickListener = () -> {
            InitApp.getHandler().post( () -> {
                pictureSelectionModel = pictureSelector.openGallery( type );
                pictureSelectionModel.maxSelectNum( max_amount )// 最大图片选择数量
                        .theme(themeId)// 主题样式设置 具体参考 values/styles
                        .minSelectNum( min_amount )// 最小选择数量
                        .imageSpanCount( 3 )// 每行显示个数(选择内部)
                        .selectionMode( select )// 多选 or 单选 true为多false为单
                        .previewImage( true )// 是否可预览图片
                        .isCamera( true )// 是否显示拍照按钮
                        .isZoomAnim( true )// 图片列表点击 缩放效果 默认tru
                        .compress( true )// 是否压缩
                        .synOrAsy( true )//同步true或异步false 压缩 默认同步
                        .openClickSound( false )// 是否开启点击声音
                        .selectionMedia( selectList )// 是否传入已选图片
                        .minimumCompressSize( 100 )// 小于100kb的图片不压缩
                        .forResult( i );//结果回调onActivityResult code
                /*裁剪*/
                if (cutOut) {
                    RoundCut( pictureSelectionModel );
                }
            } );
        };
        return onAddPicClickListener;
    }

    private void RoundCut(PictureSelectionModel pictureSelectionModel) {
        pictureSelectionModel.enableCrop( cutOut )// 是否裁剪
                .freeStyleCropEnabled( true )// 裁剪框是否可拖拽
                .circleDimmedLayer( RoundCut )// 是否圆形裁剪
                .withAspectRatio( 3, 2 )// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .showCropFrame( false )// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid( false )// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isDragFrame( true )// 是否可拖动裁剪框(固定)
                .previewEggs( true )// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .cropCompressQuality( 100 )// 裁剪压缩质量 默认100
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .rotateEnabled( true ) // 裁剪是否可旋转图片
                .scaleEnabled( true );// 裁剪是否可放大缩小图片
    }

    /*其他属性*/
    public void cutOutProperty(PictureSelectionModel pictureSelector) {

        pictureSelector.freeStyleCropEnabled( true )// 裁剪框是否可拖拽
                .enableCrop( cutOut )// 是否裁剪
                .circleDimmedLayer( false )// 是否圆形裁剪
                .withAspectRatio( 3, 2 )// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .showCropFrame( false )// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid( false )// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound( false )// 是否开启点击声音
                .isDragFrame( false )// 是否可拖动裁剪框(固定)
                .videoMaxSecond( 15 ) //视频最大秒数
                .videoMinSecond( 10 ) //视频最小秒数
                .previewEggs( true )// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .cropCompressQuality( 100 )// 裁剪压缩质量 默认100
                .minimumCompressSize( 100 )// 小于100kb的图片不压缩
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .rotateEnabled( true ) // 裁剪是否可旋转图片
                .scaleEnabled( true )// 裁剪是否可放大缩小图片
                .videoQuality( 0 )// 视频录制质量 0 or 1
                .recordVideoSecond( 60 );//录制视频秒数 默认60s
    }

}
