package com.oldwang.librarymodule.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationManagerCompat;

public class NotifyPermissionUtils {

    //用户是否打开了通知权限
    public static boolean isOpen(Context mContext) {
        if (NotificationManagerCompat.from(mContext).areNotificationsEnabled())
            return true;
        return false;
    }

    //去打开通知权限
    public static void goOpen(Context mContext) {
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
        ((Activity) mContext).startActivityForResult(intent, 1);
    }


    //去打开通知权限
    public static void openNotifyP(final Context mContext) {
        //用户是否打开了通知权限
        if (isOpen(mContext)) {
            return; //如果已经打开了，直接返回
        }
        new AlertDialog.Builder(mContext).setTitle("提示").setMessage("为了能及时收到最新消息，请打开通知权限").setNegativeButton("取消", null).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goOpen(mContext);
            }
        }).show();
    }

}
