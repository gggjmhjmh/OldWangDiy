package com.app.yf.myapplication.view.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.View;

import com.app.yf.myapplication.R;

public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        setupWindowAnimations();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finishAfterTransition();
                supportFinishAfterTransition();
            }
        });

    }

    private void setupWindowAnimations() {
        // 我们不想定义新的 Enter Transition。
        // 只更改默认的过渡持续时间
//        getWindow().getEnterTransition().setDuration(getResources().getInteger(R.integer.anim_duration_long));

//        Slide enterTransition = new Slide();
//        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
//        enterTransition.setInterpolator(new AccelerateInterpolator());
//        enterTransition.setSlideEdge(Gravity.RIGHT);

        // 渐变动画
        Fade enterTransition = new Fade();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
//        getWindow().setEnterTransition(enterTransition);
//        getWindow().setExitTransition(enterTransition);


        ChangeBounds changeBoundsTransition = new ChangeBounds();
//        setSharedElementEnterTransition(changeBoundsTransition);
        getWindow().setEnterTransition(changeBoundsTransition);

    }

}
