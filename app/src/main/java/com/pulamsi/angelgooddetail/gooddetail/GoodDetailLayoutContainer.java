package com.pulamsi.angelgooddetail.gooddetail;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.pulamsi.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 商品详情专属定制的布局容器，包含一个顶部 ScrollView 、一个底部 ScrollView 和一个悬浮在底部 ScrollView 顶部的 TabLayout，
 * 当向上或向下滑动一定的距离后容器中的两个 ScrollView 会自动向上或向下滑动
 * <p/>
 * 原理如下：
 * 1、在适当时机设置向上或向下自动滑动的标志位；
 * 2、通过定时器不断发送布局更新的操作（即 requestLayout()），并不断递减或递增全局的Y轴变量，直到满足条件就停止；
 * 3、重写 onLayout() 方法，布局更新操作时会动态 layout 两个 ScrollView 和 TabLayout 的位置（三者的 layout 参数跟全局Y轴变量有关）。
 *
 * @author WilliamChik on 2015/10/1
 */
public class GoodDetailLayoutContainer extends RelativeLayout {

    public static final String TAG = GoodDetailLayoutContainer.class.getSimpleName();

    /**
     * scroll滑动距离监听器
     */
    private ScrollListener scrollListener;

    /**
     * 布局自动上滑或下滑的消息
     */
    public static final int AUTO_SCROLL_MESSAGE = 0x00000010;

    /**
     * 布局回顶的消息
     */
    public static final int SCROLL_TO_TOP_MESSAGE = 0x00000020;

    /**
     * 布局自动上滑
     */
    public static final int AUTO_UP = 0;

    /**
     * 布局自动下滑
     */
    public static final int AUTO_DOWN = 1;

    /**
     * 动画完成
     */
    public static final int DONE = 2;

    /**
     * 更新布局的周期，单位是毫秒，用于定时器周期地更新布局，理论上最长每 16.667 毫秒更新一次布局就可以保持 60fps，但实际上布局的更新周期最好设得更低
     */
    private static final int REQUEST_LAYOUT_PERIOD = 2;

    /**
     * 上下自动滑动时，每个更新布局周期时增加或减少的像素数量，与自动滑动的动画速度相关
     */
    public static final float AUTO_SCROLL_SPEED = 10.0f;

    /**
     * 商品详情页顶部标题栏高度
     */
    public final int TITLE_BAR_HEIGHT = getResources().getDimensionPixelOffset(R.dimen.title_bar_height);

    /**
     * 商品详情页底部操作栏高度
     */
    public final int BOTTOM_OPERATION_BAR_HEIGHT =
            getResources().getDimensionPixelOffset(R.dimen.good_detail_bottom_operation_bar_height);

    /**
     * 布局的当前状态，分为 {@link #AUTO_UP}、 {@link #AUTO_DOWN} 和  {@link #DONE}
     */
    private int state = DONE;

    /**
     * 用于计算手滑动的速度
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 布局实际高度，不同系统会有不同的计算方式，具体见 onMeasure()
     */
    private int mContainerHeight;

    /**
     * 布局宽度
     */
    private int mContainerWidth;

    /**
     * 布局内的顶部视图
     */
    private ScrollView mTopView;

    /**
     * 布局内位于底部视图的悬浮 TabLayout，一直悬浮在底部视图的顶部
     */
    private View mBottomHoverTabLayout;

    /**
     * 布局内的底部视图
     */
    private ScrollView mBottomView;

    /**
     * 是否已执行 onMeasure()
     */
    private boolean isMeasured = false;

    /**
     * 布局是否可以下拉
     */
    private boolean canPullDown;

    /**
     * 布局是否可以上拉
     */
    private boolean canPullUp;

    /**
     * 作用一：防止手动滑到底部或顶部时继续滑动而改变布局，必须再次按下才能继续滑动；
     * 作用二：去除多点拖动剧变的关键，true 布局可以拖拽，false 可以舍弃将要到来的第一个move事件。
     * 在新的 pointer down 或 up 时把 canSwitchLayout 设置成 false 可以舍弃将要到来的第一个move事件，防止 mMoveTotalY 出现剧变。
     * 为什么会出现剧变呢？因为假设一开始只有一只手指在滑动，记录的坐标值是这个 pointer 的事件坐标点，这时候另一只手指按下了导致事件又多了一个 pointer，
     * 这时候到来的 move 事件的坐标可能就变成了新的 pointer 的坐标，这时计算与上一次坐标的差值就会出现剧变，变化的距离就是两个pointer间的距离。
     * 所以要把这个 move 事件舍弃掉，让 mLastMoveY 值记录这个 pointer 的坐标再开始计算 mMoveTotalY。pointer up的时候也一样。
     */
    private boolean canSwitchLayout;

    /**
     * 记录当前展示的是哪个视图，0 是 mTopView，1 是 mBottomView
     */
    private int mCurrentViewIndex = 0;

    /**
     * 滑动的Y轴总距离（当顶部布局滑动到底部时开始记录），这个是控制布局的主要变量
     */
    private float mMoveTotalY;

    /**
     * 上次滑动到的Y轴距离
     */
    private float mLastMoveY;

    /**
     * 返回顶部需要混动的 Y 轴距离
     */
    private float backTopTotalY;

    /**
     * 用于实现布局自动上滑和下滑的定时器包装类
     */
    private InnerTimerWrapper mAutoScrollTimer;

    /**
     * 用于实现布局回顶功能的定时器包装类
     */
    private InnerTimerWrapper mScrollToTopTimer;

    /**
     * 底部 ScrollView 显示时机监听器
     */
    private OnViewShowListener mOnViewShowListener;

    /**
     * 接收定时器发送的更新布局消息的处理器
     * 1. 当接收到布局自动上滑或下滑的消息时，不断递减或递增滑动的Y轴总距离，直到满足条件就停止
     * 2. 当接收到回顶的消息时
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == AUTO_SCROLL_MESSAGE) {
                // 自动上滑或下滑
                autoScrollHandleLogic();
            } else if (msg.arg1 == SCROLL_TO_TOP_MESSAGE) {
                // 回到顶部
                scrollToTopHandleLogic();
            }
        }
    };

    /**
     * 设置滑动距离监听器
     * @param scrollListener
     */
    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    /**
     * 布局滚动到顶部的处理逻辑
     */
    private void scrollToTopHandleLogic() {
//    mTopView.scrollTo(0, 0);
        // 回顶速度是自动滑动速度的两倍
        mMoveTotalY += 2.5 * AUTO_SCROLL_SPEED;
        if (mMoveTotalY > 0) {
            mScrollToTopTimer.cancel();

            mMoveTotalY = 0;
            state = DONE;
            mCurrentViewIndex = 0;
        }

        requestLayout();
    }

    /**
     * 布局自动上滑或下滑的处理逻辑
     */
    private void autoScrollHandleLogic() {
        if (mMoveTotalY != 0) {
            if (state == AUTO_UP) {
                // 布局自动向上移动，显示底层布局
                mMoveTotalY -= AUTO_SCROLL_SPEED;
                if (mMoveTotalY <= -mContainerHeight) {
                    mMoveTotalY = -mContainerHeight;
                    state = DONE;
                    mCurrentViewIndex = 1;
                    // 底部 ScrollView 自动向上移动完成，可以开始加载底部 ScrollView 的相关内容
                    if (mOnViewShowListener != null) {
                        mOnViewShowListener.onCompleteShowBottomView();
                    }
                }
            } else if (state == AUTO_DOWN) {
                // 布局自动向下移动，显示顶层布局
                if (mOnViewShowListener != null) {
                    mOnViewShowListener.onShowTopView();
                }
                mMoveTotalY += AUTO_SCROLL_SPEED;
                if (mMoveTotalY >= 0) {
                    mMoveTotalY = 0;
                    state = DONE;
                    mCurrentViewIndex = 0;
                }
            } else {
                mAutoScrollTimer.cancel();
            }
        }

        requestLayout();
    }

    /**
     * 顶部布局的 onTouchListener，用于监听整个布局什么时候可以上拉
     */
    private OnTouchListener mTopViewTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ScrollView sv = (ScrollView) v;
            // ScrollView 内第一个子视图的 measuredHeight 要大于 ScrollView 本身的 measureHeight
            if (sv.getScrollY() == (sv.getChildAt(0).getMeasuredHeight() - sv.getMeasuredHeight()) && mCurrentViewIndex == 0) {
                canPullUp = true;
            } else {
                canPullUp = false;
            }

            /*if (event.getAction() == MotionEvent.ACTION_MOVE) {
                // 可以监听到ScrollView的滚动事件，功能修复中
                scrollListener.Scroll(sv.getScrollY());
            }*/
            return false;
        }
    };




/**
 * 底部布局的 onTouchListener，用于监听整个布局什么时候可以下拉
 */
private OnTouchListener mBottomViewTouchListener = new OnTouchListener() {

    @Override
    public boolean onTouch(View sv, MotionEvent event) {
        if (sv.getScrollY() == 0 && mCurrentViewIndex == 1) {
            canPullDown = true;
        } else {
            canPullDown = false;
        }
        return false;
    }
};

    public void setOnViewShowListener(OnViewShowListener onViewShowListener) {
        mOnViewShowListener = onViewShowListener;
    }

    public GoodDetailLayoutContainer(Context context) {
        super(context);
        init();
    }

    public GoodDetailLayoutContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GoodDetailLayoutContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mAutoScrollTimer = new InnerTimerWrapper(mHandler, AUTO_SCROLL_MESSAGE);
        mScrollToTopTimer = new InnerTimerWrapper(mHandler, SCROLL_TO_TOP_MESSAGE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() != 3) {
            throw new IllegalStateException("ScrollViewContainer only can host 3 elements");
        }

        if (!isMeasured) {
            isMeasured = true;

            mContainerWidth = getMeasuredWidth();
            if (Build.VERSION.SDK_INT < 18) {
                // 4.3  以下的系统，实际的布局高度应该减去标题栏的高度和底部操作栏的高度。
                mContainerHeight = getMeasuredHeight() - TITLE_BAR_HEIGHT - BOTTOM_OPERATION_BAR_HEIGHT;
            } else {
                // 4.3 及以上系统，实际的布局高度应该减去标题栏的高度。
                mContainerHeight = getMeasuredHeight() - TITLE_BAR_HEIGHT;
            }
// else {
//        // 5.1.0 及以上的系统，布局会自动适配标题栏的高度和底部操作栏的高度，所以不用做任何操作。
//        mContainerHeight = getMeasuredHeight();
//      }

            mTopView = (ScrollView) getChildAt(0);
            mBottomView = (ScrollView) getChildAt(1);
            mBottomHoverTabLayout = getChildAt(2);

            mTopView.setOnTouchListener(mTopViewTouchListener);
            mBottomView.setOnTouchListener(mBottomViewTouchListener);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mTopView.layout(0, (int) mMoveTotalY, mContainerWidth, mTopView.getMeasuredHeight() + (int) mMoveTotalY);
        mBottomView
                .layout(0, mTopView.getMeasuredHeight() + mBottomHoverTabLayout.getMeasuredHeight() + (int) mMoveTotalY, mContainerWidth,
                        mTopView.getMeasuredHeight() + mBottomView.getMeasuredHeight() + (int) mMoveTotalY);
        // TabLayout 一直悬浮在底部视图的顶部
        mBottomHoverTabLayout.layout(0, mTopView.getMeasuredHeight() + (int) mMoveTotalY, mContainerWidth,
                mTopView.getMeasuredHeight() + mBottomHoverTabLayout.getMeasuredHeight() + (int) mMoveTotalY);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mLastMoveY = ev.getY();
                mVelocityTracker.addMovement(ev);
                canSwitchLayout = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 当一只手指按下或抬起时舍弃将要到来的第一个事件move，防止多点拖拽的bug
                canSwitchLayout = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(ev);
                if (canPullUp && mCurrentViewIndex == 0 && canSwitchLayout) {
                    mMoveTotalY += (ev.getY() - mLastMoveY);
                    // 防止上下越界
                    if (mMoveTotalY > 0) {
                        // 防止上滑越界
                        mMoveTotalY = 0;
                        mCurrentViewIndex = 0;
                    } else if (mMoveTotalY < -mContainerHeight) {
                        // 防止下滑越界
                        mMoveTotalY = -mContainerHeight;
                        mCurrentViewIndex = 1;

                    }
                    if (mMoveTotalY < -8) {
                        // 防止事件冲突
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else if (canPullDown && mCurrentViewIndex == 1 && canSwitchLayout) {
                    mMoveTotalY += (ev.getY() - mLastMoveY);
                    // 防止上下越界
                    if (mMoveTotalY < -mContainerHeight) {
                        // 防止下滑越界
                        mMoveTotalY = -mContainerHeight;
                        mCurrentViewIndex = 1;
                    } else if (mMoveTotalY > 0) {
                        // 防止上滑越界
                        mMoveTotalY = 0;
                        mCurrentViewIndex = 0;
                    }
                    if (mMoveTotalY > 8 - mContainerHeight) {
                        // 防止事件冲突
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else {
                    canSwitchLayout = true;
                }

                mLastMoveY = ev.getY();
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                mLastMoveY = ev.getY();
                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(1000);
                // 获取Y方向的速度
                float yVelocity = mVelocityTracker.getYVelocity();
                if (mMoveTotalY == 0 || mMoveTotalY == -mContainerHeight) {
                    // 布局在上下边界
                    break;
                }
                if (Math.abs(yVelocity) < 300) {
                    // 速度小于一定值的时候当作静止释放，这时候两个View往哪移动取决于滑动的距离
                    if (mMoveTotalY <= -mContainerHeight / 4) {
                        state = AUTO_UP;
                    } else if (mMoveTotalY > -mContainerHeight / 4) {
                        state = AUTO_DOWN;
                    }
                } else {
                    // 速度大于一定值的时候释放，抬起手指时的速度方向决定两个View往哪移动
                    if (yVelocity < 0) {
                        state = AUTO_UP;
                    } else {
                        state = AUTO_DOWN;
                    }
                }

                // 手指抬起后开始周期地发送布局更新的消息，实现自动上滑或下滑
                mAutoScrollTimer.schedule(REQUEST_LAYOUT_PERIOD);
                try {
                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        super.dispatchTouchEvent(ev);
        return true;
    }

    /**
     * 整个布局滚动到顶部
     */

    public void scrollToTop() {
        mScrollToTopTimer.schedule(2);
    }

/**
 * 自定义的定时器包装类，用于定时执行
 */
class InnerTimerWrapper {

    private Handler handler;
    private Timer timer;
    private InnerTimeTask task;
    private int message;

    public InnerTimerWrapper(Handler handler, int message) {
        this.handler = handler;
        this.message = message;
        timer = new Timer();
    }

    /**
     * 间隔一定时间重复执行某操作
     *
     * @param period 重复执行的间隔，单位是毫秒
     */
    public void schedule(long period) {
        if (task != null) {
            task.cancel();
            task = null;
        }
        task = new InnerTimeTask(handler, message);
        timer.schedule(task, 0, period);
    }

    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}

/**
 * 自定义的定时器任务
 */
class InnerTimeTask extends TimerTask {

    private Handler handler;
    private int messageArg;

    public InnerTimeTask(Handler handler, int messageArg) {
        this.handler = handler;
        this.messageArg = messageArg;
    }

    @Override
    public void run() {
        Message message = handler.obtainMessage();
        message.arg1 = messageArg;
        handler.sendMessage(message);
    }
}

/**
 * 布局滑动时机监听器
 */
public interface OnViewShowListener {

    /**
     * 底部 ScrollView 滑动到完全显示时的回调
     */
    void onCompleteShowBottomView();

    /**
     * 顶部 ScrollView 开始显示时的回调
     */
    void onShowTopView();
}

    /**
     * 滑动距离的接口
     */
    interface ScrollListener {
        void Scroll(int i);
    }

}
