package com.oldwang.my_demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.oldwang.lib_location.GDLocationManager;
import com.oldwang.lib_location.LocationInfo;
import com.oldwang.lib_location.MyLocationManager;
import com.oldwang.my_demo.R;

import static com.oldwang.lib_location.MyLocationManager.LOCATION_PERMISSION_CODE;
import static com.oldwang.lib_location.MyLocationManager.LOCATION_SERVICE_CODE;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locatin);
    }


    public void doClick(View view) {

        //先检查是否开启定位权限和定位服务
        MyLocationManager.checkPermissionAndService(this, new MyLocationManager.IsOpenedCallback() {
            @Override
            public void opened() {
                //同意了定位权限并且开启了定位服务
                //定位结束后的监听
                GDLocationManager.getInstance().setListener(new MyLocationManager.LocationCallback() {
                    @Override
                    public void onLocationSucceed(LocationInfo info) {
                        if (info != null) {
                            TextView tv_location = findViewById(R.id.tv_location);
                            tv_location.setText(info.getAddr());
                        }
                    }
                });
                //开始定位，每调用一次，只定位一次，不会连续定位
                GDLocationManager.getInstance().start(LocationActivity.this);
            }

            @Override
            public void notOpenPermission() {
                //未打开权限,用户拒绝开开启权限了
                MyLocationManager.toSystemSetPage(LocationActivity.this);
            }

            @Override
            public void notOpenService() {
                //未打开定位服务
                MyLocationManager.toOpenLocationService(LocationActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //跳转到系统设置页面返回
        if (requestCode == LOCATION_PERMISSION_CODE) {
            //检查是否开启定位权限和定位服务
            MyLocationManager.checkPermissionAndService(this, new MyLocationManager.IsOpenedCallback() {
                @Override
                public void opened() {

                }

                @Override
                public void notOpenPermission() {
                    //未打开权限,用户拒绝开开启权限了
                    Toast.makeText(LocationActivity.this, "未开启定位权限，无法定位", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void notOpenService() {

                }
            });
        }
        //跳转到系统定位设置页面返回
        else if (requestCode == LOCATION_SERVICE_CODE) {
            if (!MyLocationManager.isOpenLocationService(this)) {
                Toast.makeText(this, "未开启定位服务，无法定位", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
