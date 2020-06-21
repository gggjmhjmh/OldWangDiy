package com.oldwang.my_demo;

import android.content.Intent;
import android.os.Bundle;

import com.oldwang.my_demo.activity.CircleProgessActivity;
import com.oldwang.my_demo.activity.LetterIndexActivity;
import com.oldwang.my_demo.activity.NightModeSetActivity;
import com.oldwang.my_demo.adapter.FirstFragmentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        initFAbutton();
    }

    private void initFAbutton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this, NightModeSetActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && requestCode == 1) {
            //黑夜模式转换了,可能是新的adk版本不需要调用这行代码了
//            ActivityCompat.recreate(MainActivity.this); //重启activity
        }
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            list.add("你好啊" + i);
        }
        recyclerView.setAdapter(new FirstFragmentAdapter(this, list));
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //设置支toolbar
        toolbar.setNavigationIcon(R.drawable.ic_launcher_round);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "啦啦啦", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        //todo 如果条目设置了setActionView();该条目的点击就不会走onOptionsItemSelected(MenuItem item)
    /*    MenuItem menuItemZero = menu.findItem(R.id.id);
        View v = getLayoutInflater().inflate(R.layout.menu_item_view, null, true);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"id",Toast.LENGTH_SHORT).show();
            }
        });
        menuItemZero.setActionView(v);*/
//        menuItemZero.setActionView(R.layout.menu_item_view);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.id) {
            Toast.makeText(MainActivity.this, "id", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.letterIndex:
                startActivity(new Intent(this, LetterIndexActivity.class));
                break;
            case R.id.circleProgess:
                startActivity(new Intent(this, CircleProgessActivity.class));
                break;
        }

    }
}
