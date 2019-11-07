package com.paper.boat.zrdx.ui.fragment.main;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.paper.boat.dream.R;
import com.paper.boat.zrdx.InitApp;
import com.paper.boat.zrdx.common.MyLazyFragment;
import com.paper.boat.zrdx.view.dialog.AddressDialog;
import com.paper.boat.zrdx.view.dialog.BaseDialog;
import com.paper.boat.zrdx.view.dialog.BaseDialogFragment;
import com.paper.boat.zrdx.view.dialog.DateDialog;
import com.paper.boat.zrdx.view.dialog.InputDialog;
import com.paper.boat.zrdx.view.dialog.MenuDialog;
import com.paper.boat.zrdx.view.dialog.MessageDialog;
import com.paper.boat.zrdx.view.dialog.PayPasswordDialog;
import com.paper.boat.zrdx.view.dialog.TimeDialog;
import com.paper.boat.zrdx.view.dialog.ToastDialog;
import com.paper.boat.zrdx.view.dialog.UpdateDialog;
import com.paper.boat.zrdx.view.dialog.WaitDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.OnClick;


public class DialogFragment extends MyLazyFragment {

    public static DialogFragment newInstance() {
        return new DialogFragment();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dialog;
    }

    @OnClick({R.id.btn_dialog_message, R.id.btn_dialog_input, R.id.btn_dialog_bottom_menu, R.id.btn_dialog_center_menu,
            R.id.btn_dialog_succeed_toast, R.id.btn_dialog_fail_toast, R.id.btn_dialog_warn_toast, R.id.btn_dialog_wait,
            R.id.btn_dialog_pay, R.id.btn_dialog_address, R.id.btn_dialog_date, R.id.btn_dialog_time,
            R.id.btn_dialog_update, R.id.btn_dialog_share, R.id.btn_dialog_custom})
    public void onClick(View v) {
        switch (v.getId()) {
            // 消息对话框
            case R.id.btn_dialog_message:
                new MessageDialog.Builder( getAttachActivity() )
                        // 标题可以不用填写
                        .setTitle( "我是标题" )
                        // 内容必须要填写
                        .setMessage( "我是内容" )
                        // 确定按钮文本
                        .setConfirm( getString( R.string.common_confirm ) )
                        // 设置 null 表示不显示取消按钮
                        .setCancel( getString( R.string.common_cancel ) )
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener( new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                toast( "确定了" );
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast( "取消了" );
                            }
                        } )
                        .show();
                break;
            case R.id.btn_dialog_input:
                // 输入对话框
                new InputDialog.Builder( getAttachActivity() )
                        // 标题可以不用填写
                        .setTitle( "我是标题" )
                        // 内容可以不用填写
                        .setContent( "我是内容" )
                        // 提示可以不用填写
                        .setHint( "我是提示" )
                        // 确定按钮文本
                        .setConfirm( getString( R.string.common_confirm ) )
                        // 设置 null 表示不显示取消按钮
                        .setCancel( getString( R.string.common_cancel ) )
                        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                        .setListener( new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                toast( "确定了：" + content );
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast( "取消了" );
                            }
                        } )
                        .show();
                break;
            case R.id.btn_dialog_bottom_menu:
                List <String> data = new ArrayList <>();
                for (int i = 0; i < 10; i++) {
                    data.add( "我是数据" + i );
                }
                // 底部选择框
                new MenuDialog.Builder( getAttachActivity() )
                        // 设置 null 表示不显示取消按钮
                        //.setCancel(getString(R.string.common_cancel))
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setList( data )
                        .setListener( new MenuDialog.OnListener <String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, String string) {
                                toast( "位置：" + position + "，文本：" + string );
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast( "取消了" );
                            }
                        } )
                        .show();
                break;
            case R.id.btn_dialog_center_menu:
                List <String> data1 = new ArrayList <>();
                for (int i = 0; i < 10; i++) {
                    data1.add( "我是数据" + i );
                }
                // 居中选择框
                new MenuDialog.Builder( getAttachActivity() )
                        .setGravity( Gravity.CENTER )
                        // 设置 null 表示不显示取消按钮
                        //.setCancel(null)
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setList( data1 )
                        .setListener( new MenuDialog.OnListener <String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, String string) {
                                toast( "位置：" + position + "，文本：" + string );
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast( "取消了" );
                            }
                        } )
                        .show();
                break;
            case R.id.btn_dialog_succeed_toast:
                // 成功对话框
                new ToastDialog.Builder( getAttachActivity() )
                        .setType( ToastDialog.Type.FINISH )
                        .setMessage( "完成" )
                        .show();
                break;
            case R.id.btn_dialog_fail_toast:
                // 失败对话框
                new ToastDialog.Builder( getAttachActivity() )
                        .setType( ToastDialog.Type.ERROR )
                        .setMessage( "错误" )
                        .show();
                break;
            case R.id.btn_dialog_warn_toast:
                // 警告对话框
                new ToastDialog.Builder( getAttachActivity() )
                        .setType( ToastDialog.Type.WARN )
                        .setMessage( "警告" )
                        .show();
                break;
            case R.id.btn_dialog_wait:
                // 等待对话框
                final BaseDialog dialog = new WaitDialog.Builder( getAttachActivity() )
                        // 消息文本可以不用填写
                        .setMessage( getString( R.string.common_loading ) )
                        .show();
                InitApp.getHandler().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 5000 );
                break;
            case R.id.btn_dialog_pay:
                // 支付密码输入对话框
                new PayPasswordDialog.Builder( getAttachActivity() )
                        .setTitle( getString( R.string.pay_title ) )
                        .setSubTitle( "用于购买一个女盆友" )
                        .setMoney( "￥ 100.00" )
                        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                        .setListener( new PayPasswordDialog.OnListener() {

                            @Override
                            public void onCompleted(BaseDialog dialog, String password) {
                                toast( password );
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast( "取消了" );
                            }
                        } )
                        .show();
                break;
            case R.id.btn_dialog_address:
                // 选择地区对话框
                new AddressDialog.Builder( getAttachActivity() )
                        .setTitle( getString( R.string.address_title ) )
                        // 设置默认省份
                        //.setProvince("广东省")
                        // 设置默认城市（必须要先设置默认省份）
                        //.setCity("广州市")
                        // 不选择县级区域
                        //.setIgnoreArea()
                        .setListener( new AddressDialog.OnListener() {

                            @Override
                            public void onSelected(BaseDialog dialog, String province, String city, String area) {
                                toast( province + city + area );
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast( "取消了" );
                            }
                        } )
                        .show();
                break;
            case R.id.btn_dialog_date:
                // 日期选择对话框
                new DateDialog.Builder( getAttachActivity() )
                        .setTitle( getString( R.string.date_title ) )
                        // 确定按钮文本
                        .setConfirm( getString( R.string.common_confirm ) )
                        // 设置 null 表示不显示取消按钮
                        .setCancel( getString( R.string.common_cancel ) )
                        // 设置日期
                        //.setDate("2018-12-31")
                        //.setDate("20181231")
                        //.setDate(1546263036137)
                        // 设置年份
                        //.setYear(2018)
                        // 设置月份
                        //.setMonth(2)
                        // 设置天数
                        //.setDay(20)
                        // 不选择天数
                        //.setIgnoreDay()
                        .setListener( new DateDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day) {
                                toast( year + getString( R.string.common_year ) + month + getString( R.string.common_month ) + day + getString( R.string.common_day ) );

                                // 如果不指定时分秒则默认为现在的时间
                                Calendar calendar = Calendar.getInstance();
                                calendar.set( Calendar.YEAR, year );
                                // 月份从零开始，所以需要减 1
                                calendar.set( Calendar.MONTH, month - 1 );
                                calendar.set( Calendar.DAY_OF_MONTH, day );
                                toast( "时间戳：" + calendar.getTimeInMillis() );
                                //toast(new SimpleDateFormat("yyyy年MM月dd日 kk:mm:ss").format(calendar.getTime()));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast( "取消了" );
                            }
                        } )
                        .show();
                break;
            case R.id.btn_dialog_time:  /*时间*/
                // 时间选择对话框
                new TimeDialog.Builder(getAttachActivity())
                        .setTitle(getString(R.string.time_title))
                        // 确定按钮文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置时间
                        //.setTime("23:59:59")
                        //.setTime("235959")
                        // 设置小时
                        //.setHour(23)
                        // 设置分钟
                        //.setMinute(59)
                        // 设置秒数
                        //.setSecond(59)
                        // 不选择秒数
                        //.setIgnoreSecond()
                        .setListener(new TimeDialog.OnListener() {

                            @Override
                            public void onSelected(BaseDialog dialog, int hour, int minute, int second) {
                                toast(hour + getString(R.string.common_hour) + minute + getString(R.string.common_minute) + second + getString(R.string.common_second));

                                // 如果不指定年月日则默认为今天的日期
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, second);
                                toast("时间戳：" + calendar.getTimeInMillis());
                                //toast(new SimpleDateFormat("yyyy年MM月dd日 kk:mm:ss").format(calendar.getTime()));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        })
                        .show();
                break;
            case R.id.btn_dialog_share:  /*分享*/
                toast("开发中");
                break;
            case R.id.btn_dialog_update: /*更新*/
                new UpdateDialog.Builder( getAttachActivity() )
                        // 版本名
                        .setVersionName( "v 2.0" )
                        // 文件大小
                        .setFileSize( "10 M" )
                        // 是否强制更新
                        .setForceUpdate( false )
                        // 更新日志
                        .setUpdateLog( "更新日志" )
                        // 下载 url
                        .setDownloadUrl( "https://raw.githubusercontent.com/getActivity/AndroidProject/master/AndroidProject.apk" )
                        .show();
                break;
            case R.id.btn_dialog_custom:
                // 自定义对话框
                new BaseDialogFragment.Builder( getAttachActivity() )
                        .setContentView( R.layout.dialog_custom )
                        //.setText(id, "我是预设置的文本")
                        .setOnClickListener( R.id.btn_dialog_custom_ok, new BaseDialog.OnClickListener <ImageView>() {

                            @Override
                            public void onClick(BaseDialog dialog, ImageView view) {
                                dialog.dismiss();
                            }
                        } )
                        .addOnShowListener( new BaseDialog.OnShowListener() {
                            @Override
                            public void onShow(BaseDialog dialog) {
                                toast( "Dialog  显示了" );
                            }
                        } )
                        .addOnCancelListener( new BaseDialog.OnCancelListener() {
                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast( "Dialog 取消了" );
                            }
                        } )
                        .addOnDismissListener( new BaseDialog.OnDismissListener() {
                            @Override
                            public void onDismiss(BaseDialog dialog) {
                                toast( "Dialog 销毁了" );
                            }
                        } )
                        .setOnKeyListener( new BaseDialog.OnKeyListener() {
                            @Override
                            public boolean onKey(BaseDialog dialog, KeyEvent event) {
                                toast( "按键代码：" + event.getKeyCode() );
                                return false;
                            }
                        } )
                        .show();
                break;
            default:
                break;
        }
    }


}
