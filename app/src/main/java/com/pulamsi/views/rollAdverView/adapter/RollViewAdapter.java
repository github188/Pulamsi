package com.pulamsi.views.rollAdverView.adapter;


import android.view.View;

import com.pulamsi.views.rollAdverView.RollAdverView;

/**
 * <ul>
 * 广告栏数据适配器
 * <ul>
 * 改版履历：
 * 1.0.0 Created by Administrator on 2016/3/20.
 * 1.1.0 daidingkang 2016/4/12 作为基类扩展用
 *
 */

public abstract class RollViewAdapter {

    /**
     * 获取数据的条数
     * @return
     */
    public abstract int getCount();

    /**
     * 获取摸个数据
     * @param position
     * @return
     */
    public abstract Object getItem(int position);

    /**
     * 获取条目布局
     * @param parent: 父控件
     * @param contentView: 缓存的视图项
     * 
     * @return
     */
    public abstract View getView(RollAdverView parent, View contentView);

    /**
     * 条目数据适配
     * @param view: 视图
     * @param data: 对应的数据项
     */
    public abstract void setItem(final View view, final Object data);
}
