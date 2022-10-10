package com.oldwang.librarymodule.utils;

/**
 * 是否快速点击
 */
public class FastClickUtil {

    private static final int MIN_DELAY_TIME = 500;  // 两次点击间隔不能少于500ms
    private static long lastClickTime;

    /**
     * 是否快速点击
     * 不管是否快速点击，都重新计算间隔时间
     * 效果：如果快速一直点，一直返回true
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }


    private static long lastTime = 0;

    /**
     * 是否快速点击
     * 如果是快速点击，不重新计算间隔时间
     * 效果：如果快速一直点，过了离上次返回false的间隔300毫秒，就会返回false
     */
    public static boolean isFastClick2() {
        return isFastClick2(300);
    }

    /**
     * 是否快速点击
     * 如果是快速点击，不重新计算间隔时间
     * 效果：如果快速一直点，过了离上次返回false的间隔，就会返回false
     *
     * @param intervalMillis 间隔 毫秒 (就是在多少毫秒之内算快速点击)
     */
    public static boolean isFastClick2(int intervalMillis) {
        long curTime = System.currentTimeMillis();
        if (curTime - lastTime < intervalMillis) {
            return true;
        }
        lastTime = curTime;
        return false;
    }
}
