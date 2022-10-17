package com.oldwang.librarymodule.utils;

import android.animation.ValueAnimator;
import android.view.Window;
import android.view.WindowManager;

/**
 * 页面变暗/变亮
 */
public class WindowDimUtil {

    /**
     * 页面变暗/变亮
     *
     * @param window 页面窗口
     * @param isDim  是变暗还是恢复变亮
     */
    public static void windowDim(Window window, boolean isDim) {
        windowDim(window, isDim ? 0.5f : 1f);
    }

    /**
     * 页面变暗/变亮
     *
     * @param window   页面窗口
     * @param dimValue 变暗的值
     */
    public static void windowDim(Window window, float dimValue) {
        if (window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = dimValue;
        window.setAttributes(params);
    }


    /**
     * 页面动画变暗变亮
     *
     * @param window 页面窗口
     * @param isDim  是变暗还是恢复变亮
     */
    public static void windowAnimationDim(Window window, boolean isDim) {
        windowAnimationDim(window, isDim ? 0.5f : 1f);
    }

    /**
     * 页面动画变暗变亮
     *
     * @param window   页面窗口
     * @param dimValue 暗的值
     */
    public static void windowAnimationDim(Window window, float dimValue) {
        if (window == null) return;
        float from = window.getAttributes().alpha;
        if (from == dimValue) return;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, dimValue);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(animation -> {
            if (window == null) return;
            WindowManager.LayoutParams params = window.getAttributes();
            params.alpha = (Float) animation.getAnimatedValue();
            window.setAttributes(params);
        });
        valueAnimator.start();
    }

}
