package com.oldwang.lib_location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * @author OldWang
 * @description 高德地图定位管理工具
 * @date 2017/8/1
 */


public class GDLocationManager extends MyLocationManager {
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;

    private AMapLocationListener mLocationListener;
    private LocationCallback mListener;


    private static class GDLocationManagerHolder {
        public final static GDLocationManager instance = new GDLocationManager();
    }

    public static GDLocationManager getInstance() {
        return GDLocationManagerHolder.instance;
    }


    private GDLocationManager() {
    }

    //初始化高德定位
    public void init(Context context) {
        if (mLocationClient != null) return;

        mLocationClient = new AMapLocationClient(context);
        initOption();

        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.e("定位", "onLocationChanged=" + aMapLocation.getLatitude() + "," + aMapLocation.getLongitude() + ",getErrorCode=" + aMapLocation.getErrorCode() + ",aMapLocation=" + aMapLocation);
                if (mListener != null)
                    mListener.onLocationSucceed(changeInnerLocationInfo(aMapLocation));
            }
        };
        mLocationClient.setLocationListener(mLocationListener);

    }

    /*默认定位一次；bd09ll坐标系,默认不使用GPS*/
    private void initOption() {
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mLocationOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mLocationOption.setHttpTimeOut(10000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mLocationOption.setInterval(1000);//可选，设置定位间隔。默认为2秒
        mLocationOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mLocationOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mLocationOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mLocationOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mLocationOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mLocationOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true

        mLocationClient.setLocationOption(mLocationOption);
    }


    @Override
    public void start(Context context) {
        init(context);
        if (mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }
        mLocationClient.startLocation();
    }


    @Override
    public void stop() {
        if (mLocationClient != null)
            mLocationClient.stopLocation();
    }

    @Override
    public void setListener(LocationCallback listener) {
        this.mListener = listener;
    }

    private LocationInfo changeInnerLocationInfo(AMapLocation location) {
        if (location.getErrorCode() != AMapLocation.LOCATION_SUCCESS || location.getAddress() == null) {
            return null;
        }

        LocationInfo info = new LocationInfo();
        info.setmLatitude(location.getLatitude());
        info.setmLongitude(location.getLongitude());
        info.setmDesc(location.getDescription());

        info.setmCountry(location.getCountry());//中国
        info.setmProvince(location.getProvince());//浙江省
        info.setmCity(location.getCity());//宁波市
        info.setmCityCode(location.getCityCode());//城市编码
        info.setmDistrict(location.getDistrict());//镇海区
        info.setmStreet(location.getStreet());//兴庄路
        info.setmStreetNum(location.getStreetNum());//85号
        info.setAoiName(location.getAoiName());

        return info;
    }
}
