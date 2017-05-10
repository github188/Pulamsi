package com.pulamsi.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.pulamsi.R;
import com.pulamsi.utils.DensityUtil;
import com.pulamsi.views.header.RentalsSunHeaderView;

import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * material 风格的下拉刷新 header， 封装一些基本操作及初始化参数
 *
 * @author WilliamChik on 2015/9/2.
 */
public class PtrStylelFrameLayout extends PtrClassicFrameLayout {
    /**
     * 谷歌官方样式的刷新
     */
    private MaterialHeader mPtrMaterialHeader;
    /**
     * 自定义的的刷新样式
     */
    private RentalsSunHeaderView header;

    /**
     * 自定义属性的值
     */
    String styleTag;
    boolean materialKeep;

    public PtrStylelFrameLayout(Context context) {
        super(context);
        initViews();
        initGesture();
    }

    public PtrStylelFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //通过xml文件配置
        //读取属性资源
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PtrStyle);
        //分别读取cellHeight和cellWidth属性
        styleTag = a.getString(R.styleable.PtrStyle_style);
        materialKeep = a.getBoolean(R.styleable.PtrStyle_MaterialKeep,false);
        //释放资源
        a.recycle();

        initStyle(styleTag);
        initViews();
        initGesture();
    }


    public PtrStylelFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
        initGesture();
    }

    /**
     * 判断样式
     *
     * @param styleTag
     */
    private void initStyle(String styleTag) {
        if (styleTag != null && "Rentals".equals(styleTag)) {
            //自定义样式,城市升起太阳
            // header
            header = new RentalsSunHeaderView(getContext());
            header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
            header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
            header.setUp(this);
            setHeaderView(header);
            addPtrUIHandler(header);
            setLoadingMinTime(1000);
            // 阻尼系数，越大，感觉下拉时越吃力
            setResistance(2.0f);
            // 触发刷新时移动的位置对于头部的比例
            setRatioOfHeaderHeightToRefresh(4.0f);
        } else {
            //官方Material样式
            // init header
            mPtrMaterialHeader = new MaterialHeader(getContext());
            int[] colors = getResources().getIntArray(R.array.ptr_material_header_colors);
            mPtrMaterialHeader.setColorSchemeColors(colors);
            mPtrMaterialHeader.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
            mPtrMaterialHeader.setPadding(0, DensityUtil.dp2px(15), 0, DensityUtil.dp2px(10));
            mPtrMaterialHeader.setPtrFrameLayout(this);
            setHeaderView(mPtrMaterialHeader);
            addPtrUIHandler(mPtrMaterialHeader);
            // 阻尼系数，越大，感觉下拉时越吃力
            setResistance(3.0f);
            // 触发刷新时移动的位置对于头部的比例
            setRatioOfHeaderHeightToRefresh(1.0f);
        }
        if (materialKeep)
            setPinContent(true);//刷新时，保持内容不动，仅头部下移

    }


    private void initViews() {
        // 回弹到刷新高度所用时间
        setDurationToClose(200);
        // 头部回弹时间
        setDurationToCloseHeader(600);
        // 刷新时保持头部
        setKeepHeaderWhenRefresh(true);
        // false 释放刷新 | true 下拉刷新
        setPullToRefresh(false);
    }


    /**
     * 解决下拉刷新与图片轮播的手势冲突
     * 感谢http://blog.csdn.net/drg1612/article/details/49781505
     * 代替方案
     */
    private GestureDetector detector;
    private void initGesture() {
        detector = new GestureDetector(getContext(),gestureListener);
    }

    private boolean mIsDisallowIntercept = false;
    private boolean mIsHorizontalMode = false;
    private boolean isFirst = true;
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            isFirst = true;
            mIsHorizontalMode = false;
            mIsDisallowIntercept = false;
            return super.dispatchTouchEvent(e);
        }
        if (detector.onTouchEvent(e) && mIsDisallowIntercept && mIsHorizontalMode){
            return dispatchTouchEventSupper(e);
        }
        if (mIsHorizontalMode) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        this.mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float disX, disY;
            if(distanceX < 0) {
                disX = -distanceX;
            } else {
                disX = distanceX;
            }
            if(distanceY < 0) {
                disY = -distanceY;
            } else {
                disY = distanceY;
            }

            if (disX > disY) {
                if (isFirst) {
                    mIsHorizontalMode = true;
                    isFirst = false;
                }
            } else {
                if (isFirst) {
                    mIsHorizontalMode = false;
                    isFirst = false;
                }
                return false;//垂直滑动会返回false
            }
            return true;//水平滑动会返回true
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

    /**
     * 滑动ViewPager冲突事件的解决标记
     */
//    private boolean disallowInterceptTouchEvent = false;

    /*
    *//**
     * 自己写的
     * 下拉刷新，嵌套ViewPager，导致滑动手势冲突
     * @param disallowIntercept
     *//*
     *
     *
     *
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                this.requestDisallowInterceptTouchEvent(false);//很重要，解决下拉刷新手势冲突
        }
        if (disallowInterceptTouchEvent) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }*/
}


