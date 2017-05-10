package com.pulamsi.integral.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.home.viewholder.HomeCateGoodViewHolder;
import com.pulamsi.integral.entity.IntegralDetail;
import com.pulamsi.integral.viewholder.IntegralDetailViewHolder;
import com.pulamsi.myinfo.order.entity.Product;
import com.pulamsi.utils.DensityUtil;
import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * 积分明细数据适配器
 *
 */
public class IntegralDetailListAdapter extends HaiBaseListAdapter<IntegralDetail> {


    public IntegralDetailListAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.integraldetail_item,null);
        if (convertView == null) {
            return null;
        }
        return new IntegralDetailViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null || !(holder instanceof IntegralDetailViewHolder) || !(getItem(position) instanceof IntegralDetail)) {
            return;
        }

        IntegralDetailViewHolder newHolder = (IntegralDetailViewHolder) holder;
        IntegralDetail newItem = (IntegralDetail) getItem(position);


        newHolder.createDate.setText(newItem.getCreateDate());

        if (newItem.getState() == 1){
            newHolder.num.setText("+"+newItem.getNum());
        }else if(newItem.getState() == 0){
            newHolder.num.setText("-"+newItem.getNum());
        }

        newHolder.description.setText(newItem.getDescription());

    }
}
