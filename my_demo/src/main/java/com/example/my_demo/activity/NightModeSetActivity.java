package com.example.my_demo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.my_demo.R;

/**
 * Android夜间模式的实现方案：https://blog.csdn.net/qq_20521573/article/details/76222085
 */

/**
 * 夜间模式设置页面
 */
public class NightModeSetActivity extends AppCompatActivity {
    static boolean isChangeNightMode; //是否黑夜模式转换重新加载页面的
    private boolean currIsNightMode; //当前是否是黑夜模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_mode_set);

        //如果是黑夜模式转换，执行一个渐变动画
        if (isChangeNightMode) {
            isChangeNightMode = false;
            Animation animation = AnimationUtils.loadAnimation(NightModeSetActivity.this, R.anim.alpha_anim);
            findViewById(R.id.rootView).startAnimation(animation);
        }

        final Switch Switch = findViewById(R.id.Switch);

        //  获取当前模式
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        currIsNightMode = currentNightMode == AppCompatDelegate.MODE_NIGHT_YES;
        Switch.setChecked(currIsNightMode);

        Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (currIsNightMode == b) return; //表示是第一次设置，不操作，不然可能会进入循环
                currIsNightMode = b;
                isChangeNightMode = true;
                //设置白天、黑夜模式
                AppCompatDelegate.setDefaultNightMode(b ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

            }
        });

    }
}
