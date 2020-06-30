package com.oldwang.lib_location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;


/**
 * @author OldWang
 * @description 地图定位管理 封装接口，增加可扩展性
 * @date 2020/6/30
 */


public abstract class MyLocationManager {

    public static int LOCATION_SERVICE_CODE = 1;

    abstract void start(Context context);

    abstract void stop();

    abstract void setListener(LocationCallback listener);

    public interface LocationCallback {
        void onLocationSucceed(LocationInfo info);

    }

    /**
     * 是否启用定位服务
     *
     * @param mContext
     * @return
     */
    public boolean isOpenLocationService(Context mContext) {
        LocationManager mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //是否启用定位服务
//        if (!mLocationManager.isProviderEnabled(LocationManager.KEY_PROVIDER_ENABLED)) {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }

    /**
     * 去开启定位服务
     *
     * @param mContext
     */
    public void toOpenLocationService(Context mContext) {
        //去开启定位设置页面
        ((Activity) mContext).startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), LOCATION_SERVICE_CODE);
    }

    public void onViewClicked(Context mContext) {

       /* try {
            // 如果没有打开定位
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return ;
            }
        } catch (Exception e) {
        }*/

        AndPermission.with(mContext).permission(Permission.ACCESS_FINE_LOCATION).onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> permissions) {
                // TODO what to do.
                Log.i("custome", "timer onGranted");

                if (isOpenLocationService(mContext)) {
//                    finish();
                } else {
                    Log.i("onRefresh: ", "onRefresh: 未启用定位服务");
                    toOpenLocationService(mContext);//去开启定位设置页面
                }
            }
        }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> permissions) {
                // TODO what to do
                Log.i("custome", "timer onDenied");
                toSystemSetPage(mContext);//去设置权限
            }
        }).start();


//        //去开启定位设置页面
//        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
    }

    //跳转到系统该应用的设置页面
    private void toSystemSetPage(Context mContext) {
        //去设置权限
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", mContext.getPackageName());
            intent.putExtra("app_uid", mContext.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        }
        ((Activity) mContext).startActivityForResult(intent, 2);
        ((Activity) mContext).startActivity(intent);
    }


}
