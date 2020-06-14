package com.example.my_demo.diy;

import android.content.Context;
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

public class LetterIndex extends View {

    private Paint mPaint;
    private TextView bindTextView; //显示字母的tv
    private int textSize = 15; //文字大小 sp

    private String[] letters = {"A", "B", "C", "D",
            "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};


    private int selectPostion; //选中的下标
    private int singleTextHeight; //单个text的高度

    public LetterIndex(Context context) {
        super(context);
        init();
    }

    public LetterIndex(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        mPaint = new Paint();
        mPaint.setTextSize(SizeUtils.sp2px(textSize));
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
            mPaint.setColor(i == selectPostion ? Color.RED : Color.BLACK);

            Rect mBounds = new Rect();
            mPaint.getTextBounds(letters[i], 0, 1, mBounds);
            float textWidth = mBounds.width();
            float textHeight = mBounds.height();
            float x = getWidth() / 2 - textWidth / 2;
            float y = singleTextHeight * (i + 1) + getPaddingTop() - textHeight / 2;

            canvas.drawText(letters[i], x, y, mPaint);
        }

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                setBackgroundColor(Color.parseColor("#cccccccc"));
                selectPostion = (int) ((event.getY() - getPaddingTop()) / singleTextHeight);
                if (selectPostion < 0) {
                    selectPostion = 0;
                } else if (selectPostion >= letters.length) {
                    selectPostion = letters.length - 1;
                }
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

