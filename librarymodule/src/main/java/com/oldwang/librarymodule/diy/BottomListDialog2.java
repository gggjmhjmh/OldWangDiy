package com.oldwang.librarymodule.diy;

import android.content.Context;
import android.view.View;

import com.oldwang.librarymodule.R;

/**
 * 底部列表弹框
 */

public class BottomListDialog2 extends BottomListDialog {

    @Override
    protected void setContentView() {
        setContentView(R.layout.dialog_bottom_list2);
    }

    public BottomListDialog2(Context context) {
        super(context);
    }

    protected void initView() {
        super.initView();
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
