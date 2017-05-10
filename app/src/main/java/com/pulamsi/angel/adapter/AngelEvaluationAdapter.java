package com.pulamsi.angel.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.angel.adapter.viewHolder.AngelEvaluationViewHolder;
import com.pulamsi.angel.bean.AngelEvaluationBean;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.utils.GlideCircleTransform;


/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-23
 * Time: 15:25
 * FIXME
 */
public class AngelEvaluationAdapter extends HaiBaseListAdapter<AngelEvaluationBean> {

    private Activity activity;

    public AngelEvaluationAdapter(Activity activity) {
        super(activity);
        this.activity =activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.angel_evaluation_item, null);
        if (convertView == null) {
            return null;
        }
        return new AngelEvaluationViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null || !(holder instanceof AngelEvaluationViewHolder) || !(getItem(position) instanceof AngelEvaluationBean)) {
            return;
        }
        AngelEvaluationBean itemBean = getItem(position);
        AngelEvaluationViewHolder viewHolder =(AngelEvaluationViewHolder)holder;


        //头像
        if (!TextUtils.isEmpty(itemBean.getMemberimg())) {
            Glide.with(activity)//更改图片加载框架
                    .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + itemBean.getMemberimg())
//                    .centerCrop()圆形图片不能设置此参数，否则无效
                    .placeholder(R.drawable.angel_default_photo)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new GlideCircleTransform(activity))//glide设置成圆形图片
                    .into(viewHolder.aevaluationImg);
        }


        //用户名
        if (!TextUtils.isEmpty(itemBean.getUserName()))
            viewHolder.evaluationName.setText(itemBean.getUserName());


        //内容
        if (!TextUtils.isEmpty(itemBean.getEstimateContent()))
            viewHolder.evaluationContent.setText(itemBean.getEstimateContent());


        //时间
        if (!TextUtils.isEmpty(itemBean.getCreateDate()))
            viewHolder.evaluationNTime.setText(itemBean.getCreateDate());

    }
}
