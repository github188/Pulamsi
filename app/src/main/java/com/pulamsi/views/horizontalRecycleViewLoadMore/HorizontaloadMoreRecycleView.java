package com.pulamsi.views.horizontalRecycleViewLoadMore;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.pulamsi.activity.PulamsiApplication;
import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-07-02
 * Time: 10:42
 * FIXME
 * 横向滑动加载更多的RecycleView
 */
public class HorizontaloadMoreRecycleView extends TRecyclerView {
    GridLayoutManager manager;
    boolean lastPosition;
    Context context;

    /**
     * 加载更多接口
     */
    private LoadMoreListener loadMoreListener;

    public HorizontaloadMoreRecycleView(Context context) {
        super(context);
        init(context);
    }


    public HorizontaloadMoreRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setOnLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    float xMove = 0;
    float x = 0;

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (lastPosition) {
                    x = e.getX();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = e.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (lastPosition) {
                    float xDistance = x - xMove;
                    if ((xDistance) > PulamsiApplication.ScreenWidth / 4) {//可以判断滑动距离，而且通过正负判断为左滑还是右滑,如果超过屏幕四分之一就触发事件
                        if (loadMoreListener != null)
                            loadMoreListener.LoadMore();
                    }
                }
                break;
        }

        return super.onTouchEvent(e);
    }

    private void init(final Context context) {
        this.context = context;
        manager = new GridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(manager);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {//当前的recycleView不滑动(滑动已经停止时)
                    int lastVisiblePosition = manager.findLastVisibleItemPosition();//最后一个可见的位置
                    if (lastVisiblePosition >= manager.getItemCount() - 1) {//如果是最后一个位置就是滑到底部了
                        lastPosition = true;
                    } else {
                        if (lastPosition) {
                            lastPosition = false;
                        }
                    }
                }
            }
        });
    }


    /**
     * 回调加载更多接口
     */
    public interface LoadMoreListener {
        void LoadMore();
    }


}
