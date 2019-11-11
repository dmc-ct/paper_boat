package com.paper.boat.zrdx.ui.fragment.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyLazyFragment;
import com.paper.boat.zrdx.ui.actvity.personal.SettingActivity;
import com.paper.boat.zrdx.ui.actvity.util.SelectPositionActivity;
import com.paper.boat.zrdx.ui.adapter.GridImageAdapter;
import com.paper.boat.zrdx.util.file.AlbumUtil;
import com.paper.boat.zrdx.util.image.GlideHelper;
import com.paper.boat.zrdx.util.image.ImageBrowseIntent;
import com.paper.boat.zrdx.util.interfaces.Dialog;
import com.paper.boat.zrdx.view.dialog.AddressDialog;
import com.paper.boat.zrdx.view.dialog.BaseDialog;
import com.paper.boat.zrdx.view.dialog.InputDialog;
import com.paper.boat.zrdx.view.dialog.MessageDialog;
import com.paper.boat.zrdx.view.dialog.ShowPhotoDialog;
import com.paper.boat.zrdx.view.layout.SettingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FPersonalData extends MyLazyFragment {
    @BindView(R.id.iv_person_data_avatar)
    ImageView mIvPersonDataAvatar;
    @BindView(R.id.fl_person_data_head)
    FrameLayout mFlPersonDataHead;
    @BindView(R.id.sb_person_data_id)
    SettingBar mSbPersonDataId;
    @BindView(R.id.sb_person_data_name)
    SettingBar mSbPersonDataName;
    @BindView(R.id.sb_person_data_address)
    SettingBar mSbPersonDataAddress;
    @BindView(R.id.sb_person_data_phone)
    SettingBar mSbPersonDataPhone;
    @BindView(R.id.sb_person_data_system)
    SettingBar mSbPersonDataSystem;
    /* 点击添加图片跳转*/
    private GridImageAdapter.onAddPicClickListener mOnAddPicClickListener;
    private List <LocalMedia> selectList = new ArrayList <>();
    private View view;
    private Unbinder unbinder;
    //图片路径
    private String imgPath;

    public static FPersonalData newInstance() {
        return new FPersonalData();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_data;
    }


    @OnClick({R.id.iv_person_data_avatar, R.id.fl_person_data_head, R.id.sb_person_data_id,
            R.id.sb_person_data_name, R.id.sb_person_data_address, R.id.sb_person_data_phone,
            R.id.sb_person_data_alms, R.id.sb_person_data_system})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_person_data_avatar:
                if (!TextUtils.isEmpty( imgPath )) {
                    ImageBrowseIntent.showUrlImageBrowse( getAttachActivity(), imgPath );
                } else {
                    onClick( mFlPersonDataHead );
                }
                break;
            case R.id.fl_person_data_head:
                XXPermissions.with( getAttachActivity() )
                        // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                        .constantRequest()
                        // 不指定权限则自动获取清单中的危险权限
                        .permission( Permission.Group.STORAGE ) /*存贮*/
                        .permission( Permission.CAMERA ) /*拍照*/
                        .request( new OnPermission() {
                            @Override
                            public void hasPermission(List <String> granted, boolean isAll) {
                                if (isAll) {
                                    ShowPhotoDialog.showPhotoDialog( getAttachActivity(), new Dialog() {
                                        @Override
                                        public void photo() {
                                            mOnAddPicClickListener = new AlbumUtil( FPersonalData.this )
                                                    .CutOut( true )
                                                    .RoundCut( true )
                                                    .Album( selectList, 1001 );
                                            mOnAddPicClickListener.onAddPicClick(); //添加图片点击
                                        }

                                        @Override
                                        public void image() {
                                            mOnAddPicClickListener = new AlbumUtil( FPersonalData.this )
                                                    .CutOut( true )
                                                    .RoundCut( true )
                                                    .Photo( selectList, 1001 );
                                            mOnAddPicClickListener.onAddPicClick(); //添加图片点击
                                        }
                                    } );
                                } else {
                                    toast( "获取权限成功，部分权限未正常授予" );
                                }
                            }

                            @Override
                            public void noPermission(List <String> denied, boolean quick) {
                                if (quick) {
                                    toast( "被永久拒绝授权，请手动授予权限" );
                                    //如果是被永久拒绝就跳转到应用权限系统设置页面
                                    XXPermissions.gotoPermissionSettings( getAttachActivity() );
                                } else {
                                    toast( "获取权限失败" );
                                }
                            }
                        } );
                break;
            case R.id.sb_person_data_id:
                break;
            case R.id.sb_person_data_name:
                new InputDialog.Builder( getActivity() )
                        // 标题可以不用填写
                        .setTitle( getString( R.string.personal_data_name_hint ) )
                        .setContent( mSbPersonDataName.getRightText() )
                        //.setHint(getString(R.string.personal_data_name_hint))
                        //.setConfirm("确定")
                        // 设置 null 表示不显示取消按钮
                        //.setCancel("取消")
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener( new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!mSbPersonDataName.getRightText().equals( content )) {
                                    mSbPersonDataName.setRightText( content );
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                            }
                        } )
                        .show();
                break;
            case R.id.sb_person_data_address:
                new AddressDialog.Builder( getActivity() )
                        .setTitle( "选择地区" )
                        // 设置默认省份
                        .setProvince( "贵州省" )
                        // 设置默认城市（必须要先设置默认省份）
//                        .setCity( "贵阳市" )
                        // 不选择县级区域
//                        .setIgnoreArea()
                        .setListener( new AddressDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, String province, String city, String area) {
                                String address = province + " " + city + " " + area;
                                if (!mSbPersonDataAddress.getRightText().equals( address )) {
                                    mSbPersonDataAddress.setRightText( address );
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                            }
                        } )
                        .show();
                break;
            case R.id.sb_person_data_phone:
//                MyModel.helloWorld( getContext() );
                XXPermissions.with( getAttachActivity() )
                        // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                        .constantRequest()
                        // 支持请求6.0悬浮窗权限8.0请求安装权限
//                        .permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES)
                        // 不指定权限则自动获取清单中的危险权限
                        .permission( Permission.Group.LOCATION )
                        .request( new OnPermission() {

                            @Override
                            public void hasPermission(List <String> granted, boolean isAll) {
                                if (isAll) {
                                    startActivity( SelectPositionActivity.class ).goForResult( 1001 );
                                } else {
                                    toast( "获取权限成功，部分权限未正常授予" );
                                }
                            }

                            @Override
                            public void noPermission(List <String> denied, boolean quick) {
                                if (quick) {
                                    toast( "被永久拒绝授权，请手动授予权限" );
                                    //如果是被永久拒绝就跳转到应用权限系统设置页面
                                    XXPermissions.gotoPermissionSettings( getAttachActivity() );
                                } else {
                                    toast( "获取权限失败" );
                                }
                            }
                        } );

//                startActivityForResult( SelectPositionActivity.class , 1001 );
                break;
            case R.id.sb_person_data_alms:
                new MessageDialog.Builder( getAttachActivity() )
                        .setTitle( "施舍" )
                        .setMessage( "如果您觉得这个目还可以，可否愿意花一点点钱（推荐 10.24 元）" )
                        .setConfirm( "支付宝" )
                        .setCancel( null )
                        //.setAutoDismiss(false)
                        .setListener( new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                try {
                                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(
                                            "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718" +
                                                    "&qrcode=https%3A%2F%2Fqr.alipay.com%2FTSX02220DHKGPW1LR9SKA55%3F_s%3Dweb-other" ) );
                                    intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                                    startActivity( intent );
                                    toast( "非常感谢您的支持，谢谢么么哒！" );
                                } catch (Exception e) {
                                    toast( "打开支付宝失败" );
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                            }
                        } )
                        .show();
                break;
            case R.id.sb_person_data_system:  /*系统设置*/
                startActivity( SettingActivity.class ).go();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1001) {
                selectList = PictureSelector.obtainMultipleResult( data );
                for (LocalMedia media : selectList) {
                    imgPath = media.getCompressPath();
                    GlideHelper.load( mIvPersonDataAvatar, imgPath, R.mipmap.image, true );
                }
            }
        }
    }
}
