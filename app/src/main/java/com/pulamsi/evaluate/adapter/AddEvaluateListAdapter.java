package com.pulamsi.evaluate.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.evaluate.entity.Estimate;
import com.pulamsi.evaluate.viewholder.AddEvaluateViewHolder;
import com.pulamsi.myinfo.order.entity.OrderItem;
import com.pulamsi.myinfo.order.entity.Product;

import java.util.List;

/**
 * 新增商品评论适配器
 */
public class AddEvaluateListAdapter extends HaiBaseListAdapter<OrderItem> implements View.OnClickListener {

    private Activity activity;
    private List<Estimate> estimateList;


    public AddEvaluateListAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void setEstimateList(List<Estimate> estimateList) {
        this.estimateList = estimateList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.add_evaluatelist_item, null);
        if (convertView == null) {
            return null;
        }
        return new AddEvaluateViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder == null || !(holder instanceof AddEvaluateViewHolder) || !(getItem(position) instanceof OrderItem)) {
            return;
        }

        final AddEvaluateViewHolder newHolder = (AddEvaluateViewHolder) holder;
        final OrderItem newItem = (OrderItem) getItem(position);

        //把评分默认成5
        newItem.setPoint(5);
        newItem.setContent("好评!非常好用");

        // 默认是1没评论，2是全部评论完成，3是部分评论
        /**
         * 设置评论内容
         * 遍历订单，orderItem.estimateStatus != 2时 商品评价
         * 内部遍历estimateList，estimate != null and estimate.orderItem_id==orderItem.id用于显示已评价信息内容
         */
        if (newItem.getEstimateStatus() != 1 && estimateList.size() != 0) {
            for (Estimate estimate : estimateList) {
                if (estimate.getOrderItem_id().equals(newItem.getId())) {
                    newHolder.content.setText(estimate.getContent());
                    newHolder.ratingBar.setRating(estimate.getProductStars());
                    newHolder.hintContent.setVisibility(View.GONE);
                    newHolder.state.setText("已评价");
                    newHolder.content.setEnabled(false);
                    newHolder.ratingBar.setEnabled(false);
                    newHolder.more.setVisibility(View.VISIBLE);
                    newHolder.state.setEnabled(true);
                }
            }
        }

        /**
         * 有天使商品需要做此区分
         */
        Product product = null;
        if (newItem.getProduct() != null) {
            product = newItem.getProduct();
        } else if (newItem.getSellerProduct() != null) {
            product = newItem.getSellerProduct();
        }


        //商品图片
        Glide.with(activity)//更改图片加载框架
                .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + product.getPic())
                .centerCrop()
                .placeholder(R.drawable.pulamsi_loding)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(newHolder.image);
//    PulamsiApplication.imageLoader.displayImage(PulamsiApplication.context.getString(R.string.serverbaseurl) + product.getPic(), newHolder.image, PulamsiApplication.options);

        //商品名字
        newHolder.name.setText(product.getName());


        newHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (newItem.getPoint() != rating) {
                    newItem.setPoint((int) rating);
                }
            }
        });
        newHolder.content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!newItem.getContent().equals(s)) {
                    newItem.setContent(s.toString());
                }
                if (s.toString().length() == 0) {
                    newItem.setContent("好评!非常好用");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        newHolder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newHolder.hintContent.getVisibility() == View.VISIBLE) {
                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(200);
                    animation.setFillAfter(true);
                    newHolder.more.startAnimation(animation);

                    newHolder.hintContent.setVisibility(View.GONE);
                } else {
                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(200);
                    animation.setFillAfter(true);
                    newHolder.more.startAnimation(animation);

                    newHolder.hintContent.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
