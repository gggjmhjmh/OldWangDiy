package com.example.my_demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymodule.diy.CircleProgessView;
import com.example.my_demo.R;

public class CircleProgessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progess);

        final CircleProgessView circleProgess = findViewById(R.id.circleProgessView);

        circleProgess.setProgessColor(true, Color.GREEN)
                .setProgressAnimation(80, 500);

        circleProgess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleProgess.setProgressAnimation(80, 500);
            }
        });

    }
}
