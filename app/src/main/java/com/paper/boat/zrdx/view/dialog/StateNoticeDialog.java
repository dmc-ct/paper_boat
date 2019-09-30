package com.paper.boat.zrdx.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.view.ProgressView;


/**
 * 加载、警告和成功提示框
 */
public class StateNoticeDialog extends Dialog {
    public static final int TYPE_WAIN = 1;
    public static final int TYPE_SUCCESS = 2;
    public static final int TYPE_PRO = 3;
    public static final int TYPE_DOWNLOAD = 4;

    private StateNoticeDialog(Context context) {
        super( context, R.style.Dialog );
    }

    public static class Builder {
        private Context context;
        private View mView;
        private StateNoticeDialog mDialog;

        private TextView content_text;
        private ImageView state_img;
        private ProgressBar progress_view;
        private ProgressView ProgressView2;

        Handler mHandler = new Handler();

        public Builder(Context _context) {
            context = _context;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            mView = inflater.inflate( R.layout.notice_dialog, null );

            state_img = mView.findViewById( R.id.state_img );
            content_text = mView.findViewById( R.id.content_text );
            progress_view = mView.findViewById( R.id.progress_view );
            ProgressView2 = mView.findViewById( R.id.ProgressView2 );


            mDialog = new StateNoticeDialog( context );
            mDialog.addContentView( mView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
        }

        //创建dialog对象   主要就是这个方法,加载自定义布局文件
        public void showNotice(String message, int type, boolean _isOutSideCancel, int time) {
            content_text.setText( message );

            state_img.setVisibility( View.GONE );
            progress_view.setVisibility( View.GONE );
            ProgressView2.setVisibility( View.GONE );

            if (type == 1) {  /*加载警告*/
                state_img.setVisibility( View.VISIBLE );
                state_img.setBackgroundResource( R.mipmap.warn_img );
            } else if (type == 2) { /*加载成功*/
                state_img.setVisibility( View.VISIBLE );
                state_img.setBackgroundResource( R.mipmap.success_img );
            } else if (type == 3) {  /*进度条*/
                progress_view.setVisibility( View.VISIBLE );  //加载提示框
            }

            mDialog.setCanceledOnTouchOutside( _isOutSideCancel );  //在外结束
            mDialog.setCancelable( _isOutSideCancel );  //可取消

            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            if (type != 4)
                mDialog.show();
        }

        public void showNotice(int plan,String message) {
            state_img.setVisibility( View.GONE );
            progress_view.setVisibility( View.GONE );
            ProgressView2.setVisibility( View.GONE );
            content_text.setText( message );
            ProgressView2.setVisibility( View.VISIBLE );
            ProgressView2.setValue( plan );

            mDialog.setCanceledOnTouchOutside( false );  //在外结束
            mDialog.setCancelable( false );  //可取消
            mDialog.show();
        }


        public void setOnDismissListener(OnDismissListener mOnDismissListener) {
            mDialog.setOnDismissListener( mOnDismissListener );
        }

        public void onDismiss() {
            mDialog.dismiss();
        }

        public boolean isShow() {
            return mDialog.isShowing();
        }
    }

}