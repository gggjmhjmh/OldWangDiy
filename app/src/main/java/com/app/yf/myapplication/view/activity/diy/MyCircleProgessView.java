package com.app.yf.myapplication.view.activity.diy;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class MyCircleProgessView extends View {
    private static final String TAG = "MyView";
    private Context context;

    //圆的大小 , 控件的宽、高 ， 谁小取谁的值
    private int circleSize;

    //进度值 百分比
    private float progress = 58;

    //线的宽度 px
    private int strokeWidth = 30;

    private int textSize = 42;  //文字大小

    private int textColor = Color.BLUE;  //文字颜色
    private int circleColor = Color.GRAY; //圆的颜色
    private int progessColor = Color.GREEN; //进度颜色
    private int[] progessColors; //进度值的渐变颜色

    private boolean isRound = true; //端点是否为圆头

    /**
     * 设置端点是否为圆头
     *
     * @param isRound
     */
    public void setIsRound(boolean isRound) {
        this.isRound = isRound;
    }

    /**
     * 设置文字大小 dp
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = dip2px(context, textSize);
    }

    public void setTextColor(int textColor) {
        setColor(textColor, circleColor, progessColor);
    }

    public void setCircleColor(int circleColor) {
        setColor(textColor, circleColor, progessColor);
    }

    public void setProgessColor(int progessColor) {
        setColor(textColor, circleColor, progessColor);
    }

    public void setColor(int textColor, int circleColor, int progessColor) {
        this.textColor = textColor;
        this.circleColor = circleColor;
        this.progessColor = progessColor;
//        invalidate();
    }


    /**
     * 设置进度值渐变颜色
     *
     * @param progessColors 颜色数组
     * @param isRound       端点是否为圆头, 注：为圆头时，起点的圆头是结尾的颜色。开始和结束的颜色设为一样的就没问题
     */
    public void setProgessColor(int[] progessColors, boolean isRound) {
        this.progessColors = progessColors;
        this.isRound = isRound;
    }

    /**
     * 设置线的宽度
     *
     * @param strokeWidth dp
     */
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = dip2px(context, strokeWidth);
    }

    private int shadowWidth = 15; //阴影一边所占的宽度 px

    /**
     * 设置阴影
     *
     * @param radius      阴影模糊半径 dp
     * @param dx          偏移x  dp
     * @param dy          偏移y  dp
     * @param shadowColor 阴影颜色
     */
    public void setShadow(float radius, float dx, float dy, int shadowColor) {
        drawCirclePaint.setShadowLayer(dip2px(context, radius), dip2px(context, dx), dip2px(context, dy), shadowColor);
        //阴影所一边所占的宽度 = 模糊半径 + 模糊半径/2 + x与y的偏移最大值
        shadowWidth = dip2px(context, radius + radius / 2 + Math.max(dx, dy));
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    /**
     * 动画效果设置进度值
     *
     * @param progress 进度值
     * @param duration 动画时间
     */
    public void setProgressAnimation(float progress, long duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", 0, progress);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }


    public MyCircleProgessView(Context context) {
        super(context);
        init(context);
    }

    public MyCircleProgessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setStrokeWidth(10); //设置默认为10dp
        // 初始化画笔
        initPaint();
    }

    private Paint mPaint; //画文字及进度值的画笔
    private Paint drawCirclePaint; //画底部圆的画笔，之所以要再弄个画笔，是为了方便设置阴影

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER); //对齐方式: 左中右

        drawCirclePaint = new Paint();
        drawCirclePaint.setAntiAlias(true);
        drawCirclePaint.setStyle(Paint.Style.STROKE); //设置空心
        drawCirclePaint.setStrokeWidth(strokeWidth); //设置笔画宽度
        drawCirclePaint.setColor(circleColor); //设置颜色
        setShadow(2, 1, 2, Color.RED); //设置阴影
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure: width: " + widthMeasureSpec);
        Log.i(TAG, "onMeasure: height: " + heightMeasureSpec);
        Log.i(TAG, "onMeasure: getWidth: " + getWidth());
        Log.i(TAG, "onMeasure: getheight: " + getHeight());
        Log.i(TAG, "onMeasure: getMeasuredWidth: " + getMeasuredWidth());
        Log.i(TAG, "onMeasure: getMeasuredHeight: " + getMeasuredHeight());

        //圆的大小 , 控件的宽、高 ， 谁小取谁的值
        circleSize = Math.min(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

//        canvas.drawColor(Color.RED);

        //画文字
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        float y = mPaint.getFontSpacing() / 3; //行距：第一行的基线与第二行的基线的距离
        canvas.drawText((int) progress + "%", circleSize / 2, circleSize / 2 + y, mPaint);

        //画进度条底部圆
        canvas.drawCircle(circleSize / 2, circleSize / 2, circleSize / 2 - strokeWidth / 2 - shadowWidth, drawCirclePaint);
        /* - strokeWidth / 2  画的线为内边距效果,不然一半的线宽就越出范围了 */

        mPaint.setStyle(Paint.Style.STROKE); //设置空心
        mPaint.setStrokeWidth(strokeWidth); //设置笔画宽度
        mPaint.setColor(progessColor);
        //如果设置了渐变
        if (progessColors != null) {
            //设置着色器，画渐变
            Shader sweepGradient = new SweepGradient(circleSize / 2f, circleSize / 2f, progessColors, null);

            //设置起始位置旋转90度
            Matrix matrix = new Matrix();
            matrix.setRotate(90, circleSize / 2, circleSize / 2);
            sweepGradient.setLocalMatrix(matrix);

            mPaint.setShader(sweepGradient);
        }

        //设置端点是否为圆头
        mPaint.setStrokeCap(isRound ? Paint.Cap.ROUND : Paint.Cap.BUTT);

        //画进度条的进度值弧线。  线的宽度/2 为超出本身大小的宽度(因为画的线不是内、外边距，而是居中) 再加上阴影要占用的宽度
        canvas.drawArc(strokeWidth / 2 + shadowWidth, strokeWidth / 2 + shadowWidth, circleSize - strokeWidth / 2 - shadowWidth, circleSize - strokeWidth / 2 - shadowWidth, 90, progress * 3.6f, false, mPaint);

        mPaint.setShader(null); //清除着色器

        canvas.restore();
    }

}
