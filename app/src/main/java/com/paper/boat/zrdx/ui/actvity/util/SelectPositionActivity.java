package com.paper.boat.zrdx.ui.actvity.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyActivity;
import com.paper.boat.zrdx.util.base.MyToast;
import com.paper.boat.zrdx.util.map.GDMapUtil;


/**
 * ----------------------------------------------------------------------------------------------
 * <p>
 * 项目: smart_door_grid_android_app      com.dream.zrdx.wang_ge.ui.scan_activity
 * 创建: pst  邮箱：1274218999@lecent.com
 * 时间: 19-7-15 下午5:16
 * <p>
 * <p>
 * 描述: 地图跳转
 * startActivity( SelectPositionActivity.class ).goForResult( 1001 );
 * <p>
 * 使用方法
 * Bundle extras = data.getExtras();
 * String addressString = extras.getString( "address", "" );
 * latitude = extras.getDouble("latitude", 0.0);
 * longitude = extras.getDouble("longitude", 0.0);
 * ----------------------------------------------------------------------------------------------
 */
public class SelectPositionActivity extends MyActivity
        implements AMap.InfoWindowAdapter, AMap.OnInfoWindowClickListener, AMap.OnCameraChangeListener {
    private AMap aMap;
    private MapView mapView;
    private View infoWindow = null;//(成员变量)
    TextView tv_name;//左侧地址的显示
    TextView tv_submit;//右侧确定按钮
    private LatLng latlng = new LatLng( 26.616661, 106.648923 );//观山湖区
    private GeocodeSearch geocoderSearch;

    @SuppressLint("InflateParams")
    public void initView() {
        //引用刚才书写的自定义布局
        infoWindow = LayoutInflater.from( this ).inflate( R.layout.map_select_position, null );
        tv_name = infoWindow.findViewById( R.id.tv_name );
        tv_submit = infoWindow.findViewById( R.id.tv_submit );
    }

    public void initData() {
        geocoderSearch = new GeocodeSearch( this );
        geocoderSearch.setOnGeocodeSearchListener( new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int code) {
                if (code == 1000 && regeocodeResult != null) {
                    //逆向地理地址
                    String address = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                    if (!TextUtils.isEmpty( address )) {
                        tv_name.setText( address );
                    }
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        } );
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_postion_;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
//        setContentView( R.layout-v2.activity_select_postion_ );
        findViewById( R.id.map_liearlayoout ).setVisibility( View.GONE );
        mapView = findViewById( R.id.map );
        mapView.onCreate( savedInstanceState ); // 此方法必须重写
        initView();
        initData();
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener( new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    GDMapUtil.getInstance().startLocation();
                }
            } );// 设置amap加载成功事件监听器
            aMap.setOnCameraChangeListener( this );//
            aMap.setOnInfoWindowClickListener( this );// 设置点击infoWindow事件监听器
            aMap.setInfoWindowAdapter( this );// 设置自定义InfoWindow样式
            // 设置所有maker显示在当前可视区域地图中
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include( latlng ).build();
            aMap.moveCamera( CameraUpdateFactory.newLatLngBounds( bounds, 10 ) );
            aMap.moveCamera( CameraUpdateFactory.zoomTo( 15 ) );
        }
        GDMapUtil.getInstance().initLocation( getApplicationContext(), new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    latlng = new LatLng( aMapLocation.getLatitude(), aMapLocation.getLongitude() );
                    aMap.moveCamera( CameraUpdateFactory.changeLatLng( latlng ) );
                    addMarkerInScreenCenter();
                    //添加定位图标
                    GDMapUtil.getInstance().stopLocation();
                } else {
                    MyToast.printLog( GDMapUtil.getInstance().getGPSStatusString( aMapLocation.getErrorCode() ) );
                }

            }
        } );

    }

    private Marker centerMarker;

    private void addMarkerInScreenCenter() {
        Point screenPosition = aMap.getProjection().toScreenLocation( latlng );
        centerMarker = aMap.addMarker( new MarkerOptions()
                                               .anchor( 0.5f, 0.5f )
                                               .icon( BitmapDescriptorFactory
                                                              .fromResource( R.mipmap.ic_location_1 ) ) );
        //设置Marker在屏幕上,不跟随地图移动
        centerMarker.setPositionByPixels( screenPosition.x, screenPosition.y );
        centerMarker.setTitle( "" );
        centerMarker.showInfoWindow();
    }

    /**
     * 自定义infoWindow窗口
     */
    public void render(View view) {
        tv_submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.showToast( "地址：" + tv_name.getText().toString() );
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (tv_name.getText().toString().equals( "定位中" )) {
                    bundle.putString( "address", "" );
                } else {
                    bundle.putString( "address", "" + tv_name.getText().toString() );
                }
                bundle.putDouble( "latitude", centerMarker.getPosition().latitude );
                bundle.putDouble( "longitude", centerMarker.getPosition().longitude );
                intent.putExtras( bundle );
                setResult( 1002, intent );
                finish();
            }
        } );
    }

    /**
     * 自定义infoWindow窗口
     */
    @Override
    public View getInfoWindow(Marker marker) {
        render( infoWindow );
        return infoWindow;
    }

    /**
     * 自定义infoWindow窗口
     */
    @Override
    public View getInfoContents(Marker marker) {
        render( infoWindow );
        return infoWindow;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        mapView.onSaveInstanceState( outState );
    }

    @Override
    protected void onStop() {
        super.onStop();
        GDMapUtil.getInstance().stopLocation();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        GDMapUtil.getInstance().destroyLocation();
    }


    private LatLng currentTarget;

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }


    /**
     * 地图移动结束回调
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng target = cameraPosition.target;
        latlng = new LatLng( target.latitude, target.longitude );
        if (centerMarker != null) {
            centerMarker.remove();
        }
        addMarkerInScreenCenter();
//        uploadEvent.setLatitude(target.latitude + "");//这是我的model用来获取数据
//        uploadEvent.setLongitude(target.longitude + "");//这是我的model用来获取数据

        LatLonPoint latLonPoint = new LatLonPoint( target.latitude, target.longitude );
        //逆地理编码，通过经纬度获取地理位置
        RegeocodeQuery query = new RegeocodeQuery( latLonPoint, 200, GeocodeSearch.AMAP );
        geocoderSearch.getFromLocationAsyn( query );//逆向对象，下一步给初始化

    }

}
