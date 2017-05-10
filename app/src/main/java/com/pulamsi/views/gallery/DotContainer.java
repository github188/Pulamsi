package com.pulamsi.views.gallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.pulamsi.R;

/**
 * 图片轮播的小圆点容器，当图片轮播容器切换图片时会自动高亮该位置的小圆点
 *
 * @author WilliamChik on 15/10/12.
 */
public class DotContainer extends LinearLayout implements IPagerIndicator {

  private int mLittleDotSize = -2;
  /** 小圆点间的间距 */
  private int mLittleDotSpan = 36;
  /** 小圆点的半径 */
  private float mDotRadius = 6f;
  /** 当前高亮小圆点的索引 */
  private int mCurrentIdx = 0;
  /** 小圆点的总数量 */
  private int mTotalNum = 0;
  /** 小圆点高亮的颜色 */
  private int mSelectedColor = getResources().getColor(R.color.app_pulamsi_main_color);
  /** 小圆点非高亮的颜色 */
  private int mUnSelectedColor = getResources().getColor(R.color.white);


  public DotContainer(Context context) {
    super(context);
  }

  public DotContainer(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    setGravity(Gravity.CENTER_HORIZONTAL);

    TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.DotContainer, 0, 0);
    mDotRadius = arr.getDimension(R.styleable.DotContainer_my_dot_radius, mDotRadius);
    mLittleDotSpan = (int) arr.getDimension(R.styleable.DotContainer_my_dot_span, mLittleDotSpan);
    mSelectedColor = arr.getColor(R.styleable.DotContainer_my_dot_selected_color, mSelectedColor);
    mUnSelectedColor = arr.getColor(R.styleable.DotContainer_my_dot_unselected_color, mUnSelectedColor);
    arr.recycle();

    mLittleDotSize = (int) (mLittleDotSpan / 2 + mDotRadius * 2);
  }

  @Override
  public final void setNum(int num) {
    if (num < 0) {
      return;
    }

    mTotalNum = num;

    removeAllViews();
    setOrientation(HORIZONTAL);
    for (int i = 0; i < num; i++) {
      LittleDot dot = new LittleDot(getContext(), i);
      if (i == 0) {
        dot.setColor(mSelectedColor);
      } else {
        dot.setColor(mUnSelectedColor);
      }
      dot.setLayoutParams(new LayoutParams(mLittleDotSize, (int) mDotRadius * 2, 1));
      dot.setClickable(true);
      addView(dot);
    }
  }

  @Override
  public int getTotal() {
    return mTotalNum;
  }

  @Override
  public int getCurrentIndex() {
    return mCurrentIdx;
  }

  public final void setSelected(int index) {
    if (index >= getChildCount() || index < 0 || mCurrentIdx == index) {
      return;
    }
    if (mCurrentIdx < getChildCount() && mCurrentIdx >= 0) {
      ((LittleDot) getChildAt(mCurrentIdx)).setColor(mUnSelectedColor);
    }
    ((LittleDot) getChildAt(index)).setColor(mSelectedColor);
    mCurrentIdx = index;
  }

  /**
   * 自定义的小圆点，用画布绘制
   */
  private class LittleDot extends View {

    private int mColor;
    private Paint mPaint;
    private int mIndex;

    public LittleDot(Context context, int index) {
      super(context);
      mPaint = new Paint();
      // 抗锯齿
      mPaint.setAntiAlias(true);
      mIndex = index;
    }

    public int getIndex() {
      return mIndex;
    }

    public void setColor(int color) {
      if (color == mColor) {
        return;
      }
      mColor = color;
      invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      mPaint.setColor(mColor);
      canvas.drawCircle(mLittleDotSize / 2, mDotRadius, mDotRadius, mPaint);
    }
  }
}
