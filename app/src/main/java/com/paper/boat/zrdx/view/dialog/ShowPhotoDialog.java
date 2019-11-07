package com.paper.boat.zrdx.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.util.base.XClickUtil;
import com.paper.boat.zrdx.util.interfaces.Dialog;
import com.paper.boat.zrdx.util.interfaces.Uploading;

/**
 * 底部对话框
 */
public class ShowPhotoDialog {

    //拍照或从相册中选择
    public static void showPhotoDialog(Context mContext, final Dialog dialog) {
        BottomDialog mBottomPhotoDialog = new BottomDialog( mContext, 0, true );
        View view = LayoutInflater.from( mContext ).inflate( R.layout.bottom_photo, null, false );
        //取消
        AppCompatTextView dialog_image_clicked_btn_cancel = view.findViewById( R.id.dialog_image_clicked_btn_cancel );
        dialog_image_clicked_btn_cancel.setOnClickListener( v -> mBottomPhotoDialog.dismiss() );
        //从相册选择
        AppCompatTextView dialog_image_clicked_btn_undetermined = view.findViewById( R.id.dialog_image_clicked_btn_undetermined );
        dialog_image_clicked_btn_undetermined.setText( "从相册选择" );
        dialog_image_clicked_btn_undetermined.setOnClickListener( v -> {
            if (XClickUtil.isFastDoubleClick()) {
                return;
            }
            dialog.photo();
            mBottomPhotoDialog.dismiss();
        } );
        //拍照
        AppCompatTextView dialog_image_clicked_btn_delete = view.findViewById( R.id.dialog_image_clicked_btn_delete );
        dialog_image_clicked_btn_delete.setText( "拍照" );
        dialog_image_clicked_btn_delete.setOnClickListener( v -> {
            if (XClickUtil.isFastDoubleClick()) {
                return;
            }
            dialog.image();
            mBottomPhotoDialog.dismiss();
        } );
        mBottomPhotoDialog.setContentView( view );
        // 设置背景为透明色 那么白色的就能呈现出来了
        mBottomPhotoDialog.getDelegate().findViewById( com.google.android.material.R.id.design_bottom_sheet )
                .setBackgroundColor( mContext.getResources().getColor( android.R.color.transparent ) );
        mBottomPhotoDialog.show();
    }

    //文件上传
    public static void fileUploading(String imgPath, String token, final Uploading uploading) {
//        File file = FilesUpload.photo_compression( InitApp.getContext(), new File( imgPath ), false );
//        RequestBody requestFile = RequestBody.create( MediaType.parse( "png/*" ), file );
//        MultipartBody.Part body = MultipartBody.Part.createFormData( "images", file.getName(), requestFile );
        //文件上传
//        MyRetrofit.getRetrofit().uploadFile( token, body ).enqueue( new Converter <Result <FilesUploads>>() {
//            @Override
//            public void onSuccess(Result <FilesUploads> filesUploadsResult) {
//                uploading.succeed( filesUploadsResult );
//            }
//            @Override
//            public void onError(String err, Response <Result <FilesUploads>> resp) {
//                MyToast.showToast( err );
//            }
//        } );
    }
}
