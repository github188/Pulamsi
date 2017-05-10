package com.pulamsi.views.bottomBar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulamsi.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-05-26
 * Time: 10:47
 * FIXME
 */

/**
 * 底部操作栏，一般用于List多选框
 */
public class BottomColumnBar extends RelativeLayout {

     private View view;
    /**
     * 分配按钮
     */
    private TextView distribution;

    public BottomColumnBar(Context context) {
        super(context);
        init(context);
    }

    public BottomColumnBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomColumnBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public BottomColumnBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.view_bottom_cloumn_bar, this);
        distribution = (TextView) view.findViewById(R.id.slotmachine_manage_list_distribution);
    }

    /***
     * 是否响应
     */
    public void setIsEnable(boolean isEnable) {
        distribution.setEnabled(isEnable);
    }

    /**
     * 设置点击事件
     * @param onclickListener
     */
    public void setOnClickListener(OnClickListener onclickListener){
        distribution.setOnClickListener(onclickListener);
    }
}
