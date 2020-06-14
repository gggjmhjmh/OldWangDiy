package com.example.my_demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.my_demo.R;
import com.example.my_demo.diy.LetterIndex;

public class LetterIndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_index);
        LetterIndex letterIndex = findViewById(R.id.letterIndex);
        letterIndex.bindTextView((TextView) findViewById(R.id.textView_letter));

        letterIndex.setOnSelectLinstener(new LetterIndex.OnSelectLinstener() {
            @Override
            public void onSelcet(int SelectPostion, String selctStr) {

            }
        });
        
    }
}
