package com.paper.boat.zrdx.util.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.paper.boat.zrdx.InitApp;
import com.paper.boat.zrdx.bean.rest.AppUpdate;
import com.paper.boat.zrdx.network.Converter;
import com.paper.boat.zrdx.network.MyRetrofit;
import com.paper.boat.zrdx.network.Result;
import com.paper.boat.zrdx.util.HandlerTools;
import com.paper.boat.zrdx.util.base.MyToast;
import com.paper.boat.zrdx.view.dialog.StateNoticeDialog;

import java.io.File;
import java.text.DecimalFormat;

import retrofit2.Response;

import static com.paper.boat.zrdx.util.File.MyShared.APK_FILE_PATH;


public class UpdateApp {
    //定义下载文件名称
    private String apkFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wgy_";
    private Context mContext;
    private StateNoticeDialog.Builder mDialog;
    private String neoApp;
    private Activity activity;

    public UpdateApp(Context context,Activity activitys) {
        this.mContext = context;
        this.activity = activitys;
    }

    //APP更新
    public void checkAppUpdate(Boolean sing) {
        String oldApp = HandlerTools.getVerName();
        mDialog = new StateNoticeDialog.Builder( mContext );
        MyToast.printLog( oldApp );
        MyRetrofit.getRetrofit().checkAppUpdate( oldApp, "1" ).enqueue( new Converter <Result <AppUpdate>>() {
            @Override
            public void onSuccess(Result <AppUpdate> appUpdateResult) {
                if (appUpdateResult.code == 0) {
                    neoApp = appUpdateResult.data.version_no;
                    MyToast.printLog( neoApp + "," + HandlerTools.getVerName() );
                    int reslut = HandlerTools.compareVersion( neoApp, HandlerTools.getVerName() ); //0相等1不相等
                    String str = appUpdateResult.data.download_path; //下载路径
                    if (reslut == 1 && str.startsWith( "http" ) && str.endsWith( "apk" )) {  //判断是否是app的下载链接
                        AlertDialog.Builder dialog = new AlertDialog.Builder( mContext );
                        dialog.setTitle( "发现新版本,是否更新？" );
                        dialog.setMessage( appUpdateResult.data.description );
                        dialog.setPositiveButton( "确认", (dialog1, which) -> {
                            //有新版本更新
                            downloadAPK( str, neoApp );  //下载路径，最新的版本号
                            dialog1.dismiss();
                        } );
                        dialog.setNegativeButton( "取消", (dialog12, which) -> dialog12.dismiss() );
                        dialog.show();
                    } else {
                        if (sing)
                            MyToast.showToast( "当前以是最新版本==" );
                    }
                } else {
                    MyToast.showToast( appUpdateResult.message );
                }
            }

            @Override
            public void onError(String err, Response <Result <AppUpdate>> resp) {
                if (sing)
                    MyToast.showToast( err );
            }

        } );
    }

    /**
     * 点击不关闭
     */
    private void Base_DialogNotTouch() {
        if (mDialog.isShow()) {
            mDialog.onDismiss();
        }
        mDialog.showNotice( "加载中", StateNoticeDialog.TYPE_PRO, true, 0 );
    }

    private void Base_DisMissDialog() {
        mDialog.onDismiss();
    }

    /**
     * 下载地址
     * 版本号
     */
    private void downloadAPK(String url, String apkName) {
        Base_DialogNotTouch();
        final String apkPath = apkFilePath + apkName + ".apk";  //app名称
        File mm = new File( apkPath );
        //存在
        if (mm.exists()) {
            Base_DisMissDialog();
            //已有安装包，直接调用安装
            checkPermissionAndInstall( apkPath );
            return;
        }
        FileDownloader.getImpl().create( url )
                .setPath( apkPath )  //指定目录
                .setListener( new FileDownloadListener() {
                    //期间
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e( "cs", task + "-" + soFarBytes + "-" + totalBytes + "=====" );
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        Log.e( "cs", "基础下载任务" + task + "-" + etag + "继续:" + isContinue + "到目前为止,字节" + soFarBytes + "字节大小" + totalBytes );
                    }

                    //前进
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        //下载进度回调
                        Log.e( "cs", task + "-" + soFarBytes + "-" + totalBytes + "==" );
                        DecimalFormat df = new DecimalFormat( "0.00" );
                        double c = (double) soFarBytes / totalBytes;
                        int es = (int) Math.round( c * 100 );
                        mDialog.showNotice( es, "正在下载..." );
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                        MyToast.printLog( "版本更新：下载重试" );
                        MyToast.showToast( "版本更新：下载重试" );
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        //下载完成
                        Base_DisMissDialog();
                        MyToast.showToast( "更新包下载完成" );
                        checkPermissionAndInstall( apkPath );
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        MyToast.printLog( "版本更新：下载暂停" );
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        MyToast.showToast( "更新包下载失败" );
                        Base_DisMissDialog();
                        //下载进度回调
                        Log.e( "cs", task + "-" + e + "-" + "=" );
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        MyToast.showToast( "更新包下载失败" );
                        Base_DisMissDialog();
                        //下载进度回调
                        Log.e( "cs", task + "1" );
                    }
                } ).start();
    }

    /*
     * 8.0以上需要安装未知来源权限，此权限不能运行时申请
     */
    public void checkPermissionAndInstall(String apkFilePath) {
        MyShared.getInstance().put( APK_FILE_PATH, apkFilePath );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean haveInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                AlertDialog.Builder builder = new AlertDialog.Builder( mContext );
                builder.setTitle( "提示" );
                builder.setCancelable( false );
                builder.setMessage( "没有安装未知来源权限，是否去设置？" );
                builder.setPositiveButton( "确定", (dialog, which) -> {
                    Uri packageURI = Uri.parse( "package:" + mContext.getPackageName() );
                    Intent intent = new Intent( Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI );
                    activity.startActivityForResult( intent, MyShared.INSTALL_PERMISS_CODE );
//                    mContext.sendBroadcast( intent );
                    dialog.dismiss();
                } );
                builder.setNegativeButton( "取消", (dialog, which) -> {
                    dialog.dismiss();
                } );
                builder.setCancelable( true );
                builder.show();
            } else {
                installAPK( apkFilePath );
            }
        } else {
            installAPK( apkFilePath );
        }
    }

    /**
     * 下载到本地后执行安装
     */
    private void installAPK(String apkFilePath) {
        File file = new File( apkFilePath );
        if (!file.exists()) {
            MyToast.showToast( "获取下载文件失败" );
            return;
        }
        Intent intent = new Intent( Intent.ACTION_VIEW );
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        if (Build.VERSION.SDK_INT >= 24) {  //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件  (主机地址好像没用啊?)
            Uri apkUri = FileProvider.getUriForFile( mContext, InitApp.getContext().getPackageName(), file );
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
            intent.setDataAndType( apkUri, "application/vnd.android.package-archive" );
        } else {
            intent.setDataAndType( Uri.fromFile( file ), "application/vnd.android.package-archive" );
        }
        mContext.startActivity( intent );
    }
}
