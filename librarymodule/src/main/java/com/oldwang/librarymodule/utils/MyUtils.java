package com.oldwang.librarymodule.utils;

import android.animation.ObjectAnimator;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by BlueHi on 2016/11/18.
 */

/**
 * 别忘了在application里init
 * logI
 * dp2px、px2dp
 */
public class MyUtils {
    private static final String TAG = "MyUtils";
    private static Context context;

    private MyUtils() {
    }

    public static Context getAppContext() {
        return context;
    }

    public static void init(Context context) {
        MyUtils.context = context;
    }

    public static boolean isWifiProxy() {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(Utils.getApp());
            proxyPort = android.net.Proxy.getPort(Utils.getApp());
        }
        boolean flag = (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
        ToastUtils.showShort("是否开启了网络代理=" + flag);
        return flag;
    }


    /**
     * 判断多个tv是否为空（TextView）
     *
     * @param tv
     * @return 只要有一个为空，就返回true
     */
    public static boolean isEmpty(TextView... tv) {
        for (TextView t : tv) {
            if (TextUtils.isEmpty(t.getText().toString().trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化文件大小
     *
     * @param fileS 文件大小
     * @return
     */
    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }


    /**
     * 判断手机是否为小米手机
     *
     * @return
     */
    public static boolean isMIUI() {
        return isBrand("xiaomi");
    }

    /**
     * 判断手机是否为某品牌的手机
     *
     * @param brandStr 手机品牌 如： "xiaomi"、"vivo" 等
     * @return
     */
    public static boolean isBrand(String brandStr) {
        String manufacturer = Build.MANUFACTURER;
        //这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
        if (brandStr.equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    /**
     * 小米后台弹出界面检测方法
     *
     * @param context
     * @return
     */
    public static boolean canBackgroundStart(Context context) {
        AppOpsManager ops = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            ops = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        }
        try {
            int op = 10021;
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]{int.class, int.class, String.class});
            Integer result = (Integer) method.invoke(ops, op, android.os.Process.myUid(), context.getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断 悬浮窗口权限是否打开,这个不管是小米，还是华为，都可以用来检测
     *
     * @param context
     * @return true 允许  false禁止
     */
    public static boolean getAppOps(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }

    /**
     * 判断vivo后台弹出界面 1未开启 0开启
     *
     * @param context
     * @return
     */
    public static int getvivoBgStartActivityPermissionStatus(Context context) {
        String packageName = context.getPackageName();
        Uri uri2 = Uri.parse("content://com.vivo.permissionmanager.provider.permission/start_bg_activity");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        try {
            Cursor cursor = context
                    .getContentResolver()
                    .query(uri2, null, selection, selectionArgs, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int currentmode = cursor.getInt(cursor.getColumnIndex("currentstate"));
                    cursor.close();
                    return currentmode;
                } else {
                    cursor.close();
                    return 1;
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return 1;
    }

    /**
     * 判断vivo锁屏显示 1未开启 0开启
     *
     * @param context
     * @return
     */
    public static int getVivoLockStatus(Context context) {
        String packageName = context.getPackageName();
        Uri uri2 = Uri.parse("content://com.vivo.permissionmanager.provider.permission/control_locked_screen_action");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        try {
            Cursor cursor = context
                    .getContentResolver()
                    .query(uri2, null, selection, selectionArgs, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int currentmode = cursor.getInt(cursor.getColumnIndex("currentstate"));
                    cursor.close();
                    return currentmode;
                } else {
                    cursor.close();
                    return 1;
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return 1;
    }

    /**
     * 小米后台锁屏检测方法
     *
     * @param context
     * @return
     */
    public static boolean canShowLockView(Context context) {
        AppOpsManager ops = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            ops = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        }
        try {
            int op = 10020; // >= 23
            // ops.checkOpNoThrow(op, uid, packageName)
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]
                    {int.class, int.class, String.class}
            );
            Integer result = (Integer) method.invoke(ops, op, android.os.Process.myUid(), context.getPackageName());

            return result == AppOpsManager.MODE_ALLOWED;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 判断多个字符串是否为空（String）
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String... str) {
        for (String s : str) {
            if (TextUtils.isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是手机号/是否11位1开头的数字
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        String PHONE_PATTERN = "^1[0-9]{10}$";
        return Pattern.compile(PHONE_PATTERN).matcher(phone).matches();
    }

    /**
     * 判断是否身份证号
     *
     * @param cardId
     * @return
     */
    public static boolean isCard(String cardId) {

        if (cardId.length() == 15 || cardId.length() == 18) {
            if (!cardCodeVerifySimple(cardId)) {
                return false;
                //error.put("cardId", "15位或18位身份证号码不正确");
            } else {
                if (cardId.length() == 18 && !cardCodeVerify(cardId)) {
                    return false;
                    //    error.put("cardId", "18位身份证号码不符合国家规范");
                }
            }
        } else {
            return false;
            //  error.put("cardId", "身份证号码长度必须等于15或18位");
        }
        return true;
    }

    private static boolean cardCodeVerifySimple(String cardcode) {
        //第一代身份证正则表达式(15位)
        String isIDCard1 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        //第二代身份证正则表达式(18位)
        String isIDCard2 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z])$";

        //验证身份证
        if (cardcode.matches(isIDCard1) || cardcode.matches(isIDCard2)) {
            return true;
        }
        return false;
    }

    private static boolean cardCodeVerify(String cardcode) {
        int i = 0;
        String r = "error";
        String lastnumber = "";

        i += Integer.parseInt(cardcode.substring(0, 1)) * 7;
        i += Integer.parseInt(cardcode.substring(1, 2)) * 9;
        i += Integer.parseInt(cardcode.substring(2, 3)) * 10;
        i += Integer.parseInt(cardcode.substring(3, 4)) * 5;
        i += Integer.parseInt(cardcode.substring(4, 5)) * 8;
        i += Integer.parseInt(cardcode.substring(5, 6)) * 4;
        i += Integer.parseInt(cardcode.substring(6, 7)) * 2;
        i += Integer.parseInt(cardcode.substring(7, 8)) * 1;
        i += Integer.parseInt(cardcode.substring(8, 9)) * 6;
        i += Integer.parseInt(cardcode.substring(9, 10)) * 3;
        i += Integer.parseInt(cardcode.substring(10, 11)) * 7;
        i += Integer.parseInt(cardcode.substring(11, 12)) * 9;
        i += Integer.parseInt(cardcode.substring(12, 13)) * 10;
        i += Integer.parseInt(cardcode.substring(13, 14)) * 5;
        i += Integer.parseInt(cardcode.substring(14, 15)) * 8;
        i += Integer.parseInt(cardcode.substring(15, 16)) * 4;
        i += Integer.parseInt(cardcode.substring(16, 17)) * 2;
        i = i % 11;
        lastnumber = cardcode.substring(17, 18);
        if (i == 0) {
            r = "1";
        }
        if (i == 1) {
            r = "0";
        }
        if (i == 2) {
            r = "x";
        }
        if (i == 3) {
            r = "9";
        }
        if (i == 4) {
            r = "8";
        }
        if (i == 5) {
            r = "7";
        }
        if (i == 6) {
            r = "6";
        }
        if (i == 7) {
            r = "5";
        }
        if (i == 8) {
            r = "4";
        }
        if (i == 9) {
            r = "3";
        }
        if (i == 10) {
            r = "2";
        }
        if (r.equals(lastnumber.toLowerCase())) {
            return true;
        }
        return false;
    }

    //是否有效字符串(是否包含非法字符)，校验只能是中文,数字,英文字母和指定的其他字符
    public static boolean isValidString(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }


    //设置手机号中间变**
    public static String mobHide(String mob) {
        StringBuilder sb = new StringBuilder(mob);
        return sb.replace(3, 7, "****").toString();
    }

    public static void logI(String TAG, String string) {
        Log.i(TAG, string);
    }

    //格式成分秒"mm:ss"
    public static String getMMSSTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        Date d1 = new Date(time);
        return format.format(d1);
    }

    /**
     * 将 dp 转为 px
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * ScreenUtils.getScreenDensity() + 0.5);
    }

    /**
     * 将 px 转为 dp
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / ScreenUtils.getScreenDensity() + 0.5);
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 判断是否安装了支付宝
     *
     * @return true 为已经安装
     */
    public static boolean isInstalledAlipay(Context context) {
        return isInstalledApp(context, "alipays://");
    }

    //判断手机上是否已安装招商银行APP
    public static boolean isInstalledCMB(Context context) {
        return isInstalledApp(context, "cmbmobilebank://");
    }

    /**
     * 判断手机上是否已安装某APP
     *
     * @param context
     * @param uriString
     * @return
     */
    public static boolean isInstalledApp(Context context, String uriString) {
        PackageManager manager = context.getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse(uriString));
        List<ResolveInfo> list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }

    //跳转到app
    public static void toApp(String uriString) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        Uri content_url = Uri.parse(uriString);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    /**
     * 保留两位小数，如果没有小数，不会显示小数
     */
    public static String formatDecimalsMaxTwo(double d) {
        return new java.text.DecimalFormat("#.##").format(d);
    }

    /**
     * 保留两位小数
     */
    public static String formatDecimals(double d) {
        return formatDecimals(d, 2);
//        return  new java.text.DecimalFormat("#.00").format(d);
    }

    /**
     * 保留几位小数
     */
    public static String formatDecimals(double d, int num) {
        return String.format("%." + num + "f", d);
    }

    /**
     * 获取安卓手机的uuid(android_id)
     *
     * @return
     */
    public static String getPhoneUuid() {
        String androidId = DeviceUtils.getAndroidID();
        logI("androidId", "androidId: " + androidId);
        return androidId;
    }


    //设置赞的数量
    public static void setZanNum(TextView tv_zan, int zanCount) {
        tv_zan.setText(formatNumW(zanCount));
    }

    //格式化数量 W万
    public static String formatNumW(int zanCount) {
        String zan_count = "";
        if (zanCount < 10000) {
            zan_count = zanCount + "";
        } else {
            int w = zanCount / 10000;
            int q = zanCount % 10000 / 1000;
            zan_count = w + "." + q + "W";
        }
        return zan_count;
    }


    /**
     * 根据宽的比例获取高度
     *
     * @param viewWidth     控件宽
     * @param contentWidth  内容宽
     * @param contentHeight 内容高
     * @return
     */
    public static int getHeihtFromWidthRatio(int viewWidth, int contentWidth, int contentHeight) {
        //缩放比(根据宽)
        float scaleRatio = contentWidth * 1f / viewWidth;
        //绽放后的高
        float ivHight = (scaleRatio == 0 ? 0 : contentHeight / scaleRatio);

        if (ivHight != 0) {
            //宽高比
            float wHRatio = ivHight == 0 ? 0 : viewWidth / ivHight;
            //重新计算宽高比
            if (wHRatio != 0) {
                //如果比例小于0.75,就取0.75;如果大于1.5,就取1.5
                wHRatio = wHRatio < 0.75 ? 0.75f : wHRatio > 1.5 ? 1.5f : wHRatio;
            }
            //最终高度
            ivHight = viewWidth / wHRatio;
        }
        return (int) ivHight;
    }

    /**
     * 判断两个时间段是否有重叠
     *
     * @param startTime  开始时间 （如20:30,传2030）
     * @param endTime    结束时间
     * @param startTime_ 开始时间
     * @param endTime_   结束时间
     * @return
     */
    public static boolean isTimeOverlap(int startTime, int endTime,
                                        int startTime_, int endTime_) {
        if (endTime_ <= startTime) {
            //如果跨天了
            if (endTime < startTime) {
                if (endTime > startTime_) {
                    // 重叠
                    return true;
                }
            }
            Log.i(TAG, "isTimeOverlap: `` 不重叠");
        } else if (endTime <= startTime_) {
            //如果跨天了
            if (endTime_ < startTime_) {
                if (endTime_ > startTime) {
                    // 重叠
                    return true;
                }
            }
            Log.i(TAG, "isTimeOverlap: `` 不重叠");
        } else {
            // 重叠
            return true;
        }
        return false;
    }

    //是否为有效的json数据
    public static boolean isJsonValid(String str) {
        try {
            new JSONObject(str);
        } catch (JSONException e) {
            try {
                new JSONArray(str);
            } catch (JSONException e1) {
                return false;
            }
        }
        return true;
    }

    //读取txt文件的文本内容
    public static String readTxt(File file) {
        String str = "";
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String mimeTypeLine = null;
            while ((mimeTypeLine = br.readLine()) != null) {
                str = str + mimeTypeLine;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 关闭 Disposable
     */
//    public static void closeDisposable (Disposable mDisposable){
//        // 关掉 计时器
//        if (mDisposable != null && !mDisposable.isDisposed()) {
//            mDisposable.dispose();
//            mDisposable = null;
//        }
//    }


    //视图抖动动画
    public static void douDong(View mView) {
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(mView, View.TRANSLATION_X.getName(), 0, 8f);//抖动幅度0到8
        oa1.setDuration(300);//持续时间
        oa1.setInterpolator(new CycleInterpolator(4));//抖动次数
        oa1.start();//开始动画
    }


}
