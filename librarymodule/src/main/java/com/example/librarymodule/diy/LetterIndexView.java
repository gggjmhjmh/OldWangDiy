package com.example.librarymodule.diy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.example.librarymodule.R;

/**
 * OldWang
 * 字母索引控件
 * 2020/6/15
 */
public class LetterIndexView extends View {

    private Paint mPaint;
    private TextView bindTextView; //显示字母的tv

    private String[] letters = {"A", "B", "C", "D",
            "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};


    private int selectPostion; //选中的下标
    private int singleTextHeight; //单个text的高度


    private int textSize; //文字大小 px
    private int select_text_color; //选中的文字颜色
    private int unselect_text_color; //未选中的文字颜色
    private int bg_color; //背景颜色

    public LetterIndexView(Context context) {
        super(context);
        init();
    }

    public LetterIndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LetterIndexView);
        textSize = (int) array.getDimension(R.styleable.LetterIndexView_text_size, (float) SizeUtils.sp2px(15));
        select_text_color = array.getColor(R.styleable.LetterIndexView_select_text_color, Color.RED);
        unselect_text_color = array.getColor(R.styleable.LetterIndexView_unselect_text_color, Color.BLACK);
        bg_color = array.getColor(R.styleable.LetterIndexView_bg_color, Color.parseColor("#cccccccc"));

        init();
    }

    public void init() {
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        mPaint.setStrokeWidth(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        Rect mBounds = new Rect();
        mPaint.getTextBounds("W", 0, 1, mBounds);
        float textWidth = mBounds.width();
        float textHeight = mBounds.height();

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (int) (getPaddingLeft() + textWidth + getPaddingRight());
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) (getPaddingTop() + textHeight * letters.length + getPaddingBottom());
        }

        singleTextHeight = (height - getPaddingTop() - getPaddingBottom()) / letters.length;

        setMeasuredDimension(width, height);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        for (int i = 0; i < letters.length; i++) {
            mPaint.setColor(i == selectPostion ? select_text_color : unselect_text_color);

            Rect mBounds = new Rect();
            mPaint.getTextBounds(letters[i], 0, 1, mBounds);
            float textWidth = mBounds.width();
            float textHeight = mBounds.height();
            float x = getWidth() / 2 - textWidth / 2;
            float y = singleTextHeight * (i + 1) - singleTextHeight / 2 + textHeight / 2 + getPaddingTop();

            canvas.drawText(letters[i], x, y, mPaint);
        }

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(bg_color);
            case MotionEvent.ACTION_MOVE:
                //当前移动到的下标
                int currPostion = (int) ((event.getY() - getPaddingTop()) / singleTextHeight);
                if (currPostion < 0) { //超出上边
                    currPostion = 0;
                } else if (currPostion >= letters.length) { //超出下边
                    currPostion = letters.length - 1;
                }
                //如果当前触摸到的索引还是上次触摸到的(选中的)
                if (selectPostion == currPostion) {
                    //如果是按下
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (bindTextView != null) {
                            bindTextView.setVisibility(VISIBLE);
                            bindTextView.setText(letters[selectPostion]);
                        }
                        if (onSelectLinstener != null) {
                            onSelectLinstener.onSelcet(selectPostion, letters[selectPostion]);
                        }
                    }
                    //防止重复绘制和重复回调
                    break;
                }

                selectPostion = currPostion;
                invalidate();

                if (bindTextView != null) {
                    bindTextView.setVisibility(VISIBLE);
                    bindTextView.setText(letters[selectPostion]);
                }
                if (onSelectLinstener != null) {
                    onSelectLinstener.onSelcet(selectPostion, letters[selectPostion]);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (bindTextView != null) {
                    bindTextView.setVisibility(GONE);
                }
                setBackgroundColor(Color.TRANSPARENT);
                break;
        }

        return true;
    }

    public void bindTextView(TextView bindTextView) {
        this.bindTextView = bindTextView;
        this.bindTextView.setVisibility(GONE);
    }

    private OnSelectLinstener onSelectLinstener;

    public void setOnSelectLinstener(OnSelectLinstener onSelectLinstener) {
        this.onSelectLinstener = onSelectLinstener;
    }

    public interface OnSelectLinstener {
        void onSelcet(int SelectPostion, String selctStr);
    }

}

