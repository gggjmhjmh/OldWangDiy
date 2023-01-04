package com.app.yf.myapplication.view.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.app.yf.myapplication.R;

public class ThreeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);

        setupWindowAnimations();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finishAfterTransition();
//                supportFinishAfterTransition();
            }
        });

    }

    private void setupWindowAnimations() {
        // 我们不想定义新的 Enter Transition。
        // 只更改默认的过渡持续时间
//        getWindow().getEnterTransition().setDuration(getResources().getInteger(R.integer.anim_duration_long));


        // 爆炸效果的动画
        Explode transition = new Explode();
        transition.setDuration(getResources().getInteger(R.integer.anim_duration_long));

        getWindow().setEnterTransition(transition); //进入时动画


        // 侧滑动画
        Slide transition2 = new Slide();
        transition2.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        transition2.setInterpolator(new AccelerateInterpolator());
        transition2.setSlideEdge(Gravity.RIGHT);

        getWindow().setReturnTransition(transition2); //返回时动画


//        getWindow().setExitTransition(enterTransition);
    }

}
