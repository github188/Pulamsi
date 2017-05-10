package com.pulamsi.views.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.pulamsi.R;

/**
 * All rights reserved by Author<br>
 * 用于分段显示进度的进度条
 */
public class DotsProgressBar extends View {

    /**
     * 进度条的点数
     */
    private int mDotsCount;

    /**
     * 进度条圆点的半径
     */
    private int mDotsRadius;

    /**
     * 进度条的宽度
     */
    private int mDotsProgressWidth;

    /**
     * 进度条宽度的一半
     */
    private int mDotsProgressWidthHalf;

    /**
     * 进度条的背景色
     */
    private int mDotsBackColor;

    /**
     * 进度条的前景色
     */
    private int mDotsFrontColor;

    /**
     * 进度条前进的速度
     */
    private int mSpeed;

    /**
     * 目前已经进行的时间
     */
    private int mPartTime;

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 原先的进度在某个点
     */
    private int mOldPosition = 0;

    /**
     * 新的进度在某个点
     */
    private int mNewPosition = 0;

    /**
     * 每段矩形的长度
     */
    private int mPartWidth;

    /**
     * 使用插值器来获得进度值，这样不仅简化了获取进度值的过程，还可以向外提供可定制性
     */
    private Interpolator mInterpolator;

    /**
     * 可以设置一个 ViewPager 与进度条同步
     */
    private ViewPager mViewPager;

    /**
     * 用于存放需要绘制的最新坐标
     */
    private int[] mParams;

    /**
     * 标记进度条是否在动画中
     */
    private boolean mIsRunning = false;

    public DotsProgressBar(Context context) {
        this(context, null);
    }

    public DotsProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotsProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DotsProgressBar);
        mDotsCount = typedArray.getInt(R.styleable.DotsProgressBar_barDotsCount, 2);
        mDotsRadius = typedArray.getDimensionPixelSize(R.styleable.DotsProgressBar_barDotsRadius, dp2px(8));
        mDotsProgressWidth = typedArray.getDimensionPixelSize(R.styleable.DotsProgressBar_barProgressWidth, dp2px(8));
        if ((2 * mDotsRadius) < mDotsProgressWidth)
            mDotsProgressWidth = mDotsRadius * 2; // 如果用户设置进度条的宽度大于点的直径，则设置为半径大小
        mDotsProgressWidthHalf = mDotsProgressWidth / 2;
        // 获取用户定义的时间，并转化成时间间隔
        mSpeed = typedArray.getInt(R.styleable.DotsProgressBar_barSpeed, 40);
        // 获取用户定义的进度条背景色与前景色
        mDotsBackColor = typedArray.getColor(R.styleable.DotsProgressBar_barBackColor, ContextCompat.getColor(context, android.R.color.darker_gray));
        mDotsFrontColor = typedArray.getColor(R.styleable.DotsProgressBar_barFrontColor, ContextCompat.getColor(context, android.R.color.holo_blue_light));
        typedArray.recycle();
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        // 初始化插值器
        mInterpolator = new LinearInterpolator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, mDotsRadius * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mPartWidth = (width - mDotsRadius * 2) / (mDotsCount - 1);
        // 画背景
        mPaint.setColor(mDotsBackColor);
        canvas.drawRect(mDotsRadius, height / 2 - mDotsProgressWidthHalf, width - mDotsRadius, height / 2 + mDotsProgressWidthHalf, mPaint);
        for (int i = 0; i < mDotsCount; i++) {
            canvas.drawCircle(mDotsRadius + mPartWidth * i, mDotsRadius, mDotsRadius, mPaint);
        }
        // 调整画笔为前景色
        mPaint.setColor(mDotsFrontColor);
        // 绘制前景进度条
        int start = mDotsRadius + mNewPosition * mPartWidth;
        canvas.drawRect(mDotsRadius, mDotsRadius - mDotsProgressWidthHalf, start, mDotsRadius + mDotsProgressWidthHalf, mPaint);
        for (int i = 0; i < (mNewPosition + 1); i++) {
            canvas.drawCircle(mDotsRadius + i * mPartWidth, mDotsRadius, mDotsRadius, mPaint);
        }
    }

    /**
     * 设置进度点
     *
     * @param Position
     */
    public void setRunCountPosition(int Position) {
        if (Position < 0 || Position > mDotsCount)
            return;
        mNewPosition = Position;
        postInvalidate();
    }

    /**
     * 设置插值器
     *
     * @param interpolator 插值器
     */
    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    /**
     * dp 转 px
     *
     * @param dpValue dp 值
     * @return 返回 px 值
     */
    private int dp2px(int dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
