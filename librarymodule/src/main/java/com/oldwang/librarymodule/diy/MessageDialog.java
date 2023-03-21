package com.oldwang.librarymodule.diy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oldwang.librarymodule.R;

/**
 * @author OldWang
 * @description 消息提示框
 * @date 2020/7/3
 * 可以只有一个button,可以传自定义布局
 */
public class MessageDialog extends Dialog implements View.OnClickListener {

    private String title, message, left, right;
    private View layoutView;
    private int layoutId;
    private OnBaseClickListener baseClickListener;


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

    public MessageDialog(Context context, String message, String leftBtText) {
        //super(context, R.style.NotDaddingDialog);
        super(context);
        setOwnerActivity((Activity) context);
        this.message = message;
        this.left = leftBtText;
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


    /**
     * 子类覆写要用的布局
     *
     * @return
     */
    protected int createLayoutResId() {
        return R.layout.view_message_dialog;
    }

    public MessageDialog setLayoutResId(int layoutResId) {
        this.layoutId = layoutResId;
        return this;
    }

    public MessageDialog setLayoutView(View layoutView) {
        this.layoutView = layoutView;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (layoutView != null) {
            setContentView(layoutView);
        } else if (layoutId != 0) {
            setContentView(layoutId);
        } else setContentView(createLayoutResId());

        this.setCanceledOnTouchOutside(false);

        windowDeploy();

        initView();
    }


    @SuppressWarnings("deprecation")
    public void windowDeploy() {
        Window window = getWindow(); // 得到对话框
        window.setBackgroundDrawableResource(R.color.tran);
        WindowManager.LayoutParams wl = window.getAttributes();
        //wl.gravity = Gravity.CENTER;
        //设置为横向全屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        wl.width = ViewGroup.LayoutParams.WRAP_CONTENT; //这样才能居中显示，才能点击外面dialog消失
        window.setAttributes(wl);
        //wl.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
    }

    private void initView() {
        //标题
        TextView tv_title = findViewById(R.id.tv_title);
        if (tv_title != null) {
            if (title != null) { //默认null，啥也不干
                if (TextUtils.isEmpty(title)) { //空字符串
                    tv_title.setVisibility(View.GONE);
                } else {
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.setText(title);
                }
            }
        }
        //消息体
        TextView tv_message = findViewById(R.id.tv_message);
        ScrollView scrollView = findViewById(R.id.scrollView);
        if (tv_message != null) {
            if (message != null) { //默认null，啥也不干
                if (TextUtils.isEmpty(message)) {
                    if (scrollView != null) scrollView.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);
                } else {
                    if (scrollView != null) scrollView.setVisibility(View.VISIBLE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText(message);
                }
            }
        }

        TextView btn_left = findViewById(R.id.btn_left);
        TextView btn_right = findViewById(R.id.btn_right);
        if (btn_left != null) {
            btn_left.setOnClickListener(this);
            if (left != null) { //默认null，啥也不干
                if (TextUtils.isEmpty(left)) {
                    btn_left.setVisibility(View.GONE);
                } else {
                    btn_left.setText(left);
                }
            }
        }
        if (btn_right != null) {
            btn_right.setOnClickListener(this);
            if (left != null) { //默认null，啥也不干
                if (TextUtils.isEmpty(right)) {
                    btn_right.setVisibility(View.GONE);
                } else {
                    btn_right.setText(right);
                }
            }
        }

        if (btn_left == null || btn_right == null || TextUtils.isEmpty(left) || TextUtils.isEmpty(right)) {
            View line = findViewById(R.id.line);
            if (line != null) line.setVisibility(View.GONE);
        }


        View cha = findViewById(R.id.cha);
        if (cha != null)
            cha.setOnClickListener(this);

        View ok = findViewById(R.id.ok);
        if (ok != null)
            ok.setOnClickListener(this);
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
        } else {
            if (baseClickListener != null) baseClickListener.onViewClick(v);
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

    public MessageDialog show(OnBaseClickListener clickListener) {
        setBaseClickListener(clickListener)
                .show();
        return this;
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

        default void onViewClick(View v) {
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
