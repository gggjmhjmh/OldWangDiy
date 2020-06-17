package com.oldwang.my_demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.oldwang.librarymodule.diy.LetterIndexView;
import com.oldwang.my_demo.R;

public class LetterIndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_index);
        LetterIndexView letterIndex = findViewById(R.id.letterIndex);
        letterIndex.bindTextView((TextView) findViewById(R.id.textView_letter));

        letterIndex.setOnSelectLinstener(new LetterIndexView.OnSelectLinstener() {
            @Override
            public void onSelcet(int SelectPostion, String selctStr) {
                Log.i("LetterIndexActivity", "onSelcet: " + selctStr);
            }
        });

    }
}
