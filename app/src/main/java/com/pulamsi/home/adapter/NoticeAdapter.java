package com.pulamsi.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.constant.Constants;
import com.pulamsi.home.entity.AdverNotice;
import com.pulamsi.views.rollAdverView.RollAdverView;
import com.pulamsi.views.rollAdverView.adapter.RollViewAdapter;
import com.pulamsi.webview.CommonWebViewActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/3/20.
 * 京东广告栏数据适配器
 */

public class NoticeAdapter extends RollViewAdapter {
    private List<AdverNotice> mDatas;
    Context context;

    public class ViewHolder {
        /**
         * 描述信息
         */
        public TextView title;
        /**
         * tag
         */
        public TextView tag;
        /**
         * url
         */
        public String url;

    }

    public NoticeAdapter(List<AdverNotice> mDatas , Context context) {
        this.mDatas = mDatas;
        this.context = context;
        if (mDatas == null || mDatas.isEmpty()) {
            throw new RuntimeException("nothing to show");
        }
    }


    public void setList(List<AdverNotice> mDatas) {
        this.mDatas = mDatas;
    }

    /**
     * 获取数据的条数
     *
     * @return
     */
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 获取摸个数据
     *
     * @param position
     * @return
     */
    public AdverNotice getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public View getView(RollAdverView parent, View convertView) {
        View view = null;
        if (null == convertView) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adver_notice_item, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //比如打开url
                    ViewHolder holder = (ViewHolder) v.getTag();
                    if (null != holder) {
                        Intent taobao = new Intent(context, CommonWebViewActivity.class);
                        taobao.putExtra(Constants.WEBVIEW_LOAD_URL,  holder.url);
                        taobao.putExtra(Constants.WEBVIEW_TITLE, "普兰氏快报");
                        context.startActivity(taobao);
                    }
                }
            });
            ViewHolder holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.tag = (TextView) view.findViewById(R.id.tag);
            view.setTag(holder);
        } else {
            view = convertView;
        }
        return view;
    }

    @Override
    public void setItem(View view, Object data) {
        AdverNotice item = (AdverNotice) data;
        if (null != view) {
            ViewHolder holder = (ViewHolder) view.getTag();
            if (null != item && null != holder) {
                holder.title.setText(item.getTitle());
                holder.tag.setText(item.getTag());
                holder.url = item.getUrl();
            }
        }
    }
}
