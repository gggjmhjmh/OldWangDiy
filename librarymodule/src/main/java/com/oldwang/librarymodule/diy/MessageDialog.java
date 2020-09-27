package com.oldwang.librarymodule.diy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.librarymodule.R;

/**
 * @author OldWang
 * @description 消息提示框
 * @date 2020/7/3
 * 可以只有一个button,可以传自定义布局
 */
public class MessageDialog extends Dialog implements View.OnClickListener {

    private TextView tv_message;
    private String title, message, left, right;
    private boolean isOnlyOk;
    private int layoutResId;
    private View layoutView;
    private OnBaseClickListener baseClickListener;

    private int getLayoutResId() {
        //调用者设置了布局，就用设置的布局
        return layoutResId != 0 ? layoutResId : creatLayoutResId();
    }

    /**
     * 子类覆写要用的布局
     * @return
     */
    protected int creatLayoutResId() {
        return R.layout.view_message_dialog;
    }

    public MessageDialog setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
        return this;
    }

    public MessageDialog setLayoutView(View layoutView) {
        this.layoutView = layoutView;
        return this;
    }

    public MessageDialog setOnlyOk(boolean onlyOk) {
        this.isOnlyOk = onlyOk;
        return this;
    }

    public MessageDialog(Context context) {
        //super(context, R.style.NotDaddingDialog);
        super(context);
        setOwnerActivity((Activity) context);
    }

    public MessageDialog(Context context, String message) {
        //super(context, R.style.NotDaddingDialog);
        super(context);
        setOwnerActivity((Activity) context);
        this.message = message;
    }

    public MessageDialog(Context context, String leftBtText, String rightBtText) {
        //super(context, R.style.NotDaddingDialog);
        super(context);
        setOwnerActivity((Activity) context);
        this.left = leftBtText;
        this.right = rightBtText;
    }

    public MessageDialog(Context context, String message, String leftBtText, String rightBtText) {
        //super(context, R.style.NotDaddingDialog);
        super(context);
        setOwnerActivity((Activity) context);
        this.message = message;
        this.left = leftBtText;
        this.right = rightBtText;
    }

    public MessageDialog(Context context, String title, String message, String leftBtText, String rightBtText) {
        //super(context, R.style.NotDaddingDialog);
        super(context);
        setOwnerActivity((Activity) context);
        this.title = title;
        this.message = message;
        this.left = leftBtText;
        this.right = rightBtText;
    }

    public MessageDialog(Context context, int layoutResId) {
        super(context);
        setOwnerActivity((Activity) context);
        this.layoutResId = layoutResId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (layoutView != null)
            setContentView(layoutView);
        else setContentView(getLayoutResId());

        this.setCanceledOnTouchOutside(false);

        //标题
        TextView tv_title = findViewById(R.id.tv_title);
        if (tv_title != null) {
            if (TextUtils.isEmpty(title)) {
                tv_title.setVisibility(View.GONE);
            } else {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(title);
            }
        }
        //消息体
        tv_message = findViewById(R.id.tv_message);
        if (tv_title != null) {
            if (TextUtils.isEmpty(message)) {
                tv_message.setVisibility(View.GONE);
            } else {
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(message);
            }
        }

        TextView btn_left = findViewById(R.id.btn_left);
        TextView btn_right = findViewById(R.id.btn_right);
        if (btn_left != null) {
            btn_left.setOnClickListener(this);
            if (!TextUtils.isEmpty(left)) {
                btn_left.setText(left);
            }
        }
        if (btn_right != null) {
            btn_right.setOnClickListener(this);
            if (!TextUtils.isEmpty(right)) {
                btn_right.setText(right);
            }
        }

        if (isOnlyOk) {
            View line = findViewById(R.id.line);
            if (line != null) line.setVisibility(View.GONE);
            if (btn_left != null) btn_left.setVisibility(View.GONE);
            if (btn_right != null) {
                btn_right.setVisibility(View.VISIBLE);
                btn_right.setText(TextUtils.isEmpty(right) ? "确认" : right);
            }
        }

        View cha = findViewById(R.id.cha);
        if (cha != null)
            cha.setOnClickListener(this);

        View ok = findViewById(R.id.ok);
        if (ok != null)
            ok.setOnClickListener(this);


        windowDeploy();
    }


    @SuppressWarnings("deprecation")
    public void windowDeploy() {
        Window window = getWindow(); // 得到对话框
        window.setBackgroundDrawableResource(R.color.tran);
        WindowManager.LayoutParams wl = window.getAttributes();
        //wl.gravity = Gravity.CENTER;
        //wl.gravity = Gravity.CENTER_HORIZONTAL;
        WindowManager m = window.getWindowManager();
        wl.width = ViewGroup.LayoutParams.WRAP_CONTENT; //这样才能居中显示，才能点击外面dialog消失
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;//这样才能居中显示，才能点击外面dialog消失
        //      Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        //		wl.width = d.getWidth();
        //		wl.height = d.getHeight();
        window.setAttributes(wl);


        //wl.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_left) {// 左边 取消按钮点击
            if (baseClickListener != null) baseClickListener.onLeftBtClick(null);
        } else if (id == R.id.btn_right) {// 右边 确定按钮点击
            if (baseClickListener != null) baseClickListener.onRightBtClick(null);
        } else if (id == R.id.cha) {// 右上角叉叉
            if (baseClickListener != null) baseClickListener.onChaClick(null);
        } else if (id == R.id.ok) {// 确定按键
            if (baseClickListener != null) baseClickListener.onBtOkClick(null);
        }
        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        message = null;
        left = null;
        right = null;
    }

    public MessageDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MessageDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public MessageDialog setLeftBtText(String text) {
        this.left = text;
        return this;
    }

    public MessageDialog setRightBtText(String text) {
        this.right = text;
        return this;
    }

    public void show(OnBaseClickListener clickListener) {
        setBaseClickListener(clickListener)
                .show();
    }

    public MessageDialog setBaseClickListener(OnBaseClickListener clickListener) {
        baseClickListener = clickListener;
        return this;
    }

    public interface OnBaseClickListener {
        default void onLeftBtClick(View v) {
        }

        default void onRightBtClick(View v) {
        }

        default void onChaClick(View v) {
        }

        default void onBtOkClick(View v) {
        }
    }

    /**
     * 下面是 自定义布局的控件(基础布局里没有的控件) 的设置
     * 在调用show()后调用才有效
     */

    /**
     * 获取视图控件
     * 在调用show()后调用才能获取到控件
     *
     * @param viewId 视图id
     * @return 视图控件
     */
    public View getView(int viewId) {
        View view = getWindow().findViewById(viewId);
        return view;
    }

    /**
     * 设置背景图
     * 在调用show()后调用才有效
     *
     * @param viewId  视图id
     * @param bgResId 背景id
     */
    public MessageDialog setBgRes(int viewId, int bgResId) {
        View layout_bg = getWindow().findViewById(viewId);
        if (layout_bg != null) layout_bg.setBackgroundResource(bgResId);
        return this;
    }

    public MessageDialog setImageRes(int ivId, int imageResId) {
        ImageView iv = getWindow().findViewById(ivId);
        if (iv != null) iv.setImageResource(imageResId);
        return this;
    }

    /**
     * 设置文本
     * 在调用show()后调用才有效
     *
     * @param tvId TextView控件id
     * @param text 文本id
     * @return
     */
    public MessageDialog setText(int tvId, String text) {
        TextView tv = getWindow().findViewById(tvId);
        if (tv != null) tv.setText(text);
        return this;
    }

}
