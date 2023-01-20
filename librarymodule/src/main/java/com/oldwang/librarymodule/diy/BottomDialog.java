package com.oldwang.librarymodule.diy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oldwang.librarymodule.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 底部弹框
 */

abstract public class BottomDialog extends Dialog {
    protected Context mContext;

    abstract protected void setContentView();

    public BottomDialog(Context context) {
        super(context, R.style.BottomDialog);
        mContext = context;
        setContentView();
        Window window = this.getWindow();
        //设置位置在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置弹入弹出动画
        window.setWindowAnimations(R.style.BottomDialog_Animation);
        //设置为横向全屏
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        //        setCancelable(false);
//        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
