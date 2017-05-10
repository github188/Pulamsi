package com.pulamsi.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.home.entity.ChannelMobile;
import com.pulamsi.home.viewholder.HomeDianzhuViewHolder;
import com.pulamsi.utils.DensityUtil;
import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * 首页店主推荐列表数据适配器
 *
 * @author WilliamChik on 15/8/12.
 */
public class HomeDianzhuListAdapter extends HaiBaseListAdapter<ChannelMobile> {

  private Activity activity;

  public HomeDianzhuListAdapter(Activity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = new DianzhuItemLayout(parent.getContext());
    if (convertView == null) {
      return null;
    }

    return new HomeDianzhuViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder == null || !(holder instanceof HomeDianzhuViewHolder) || !(getItem(position) instanceof ChannelMobile)) {
      return;
    }

    HomeDianzhuViewHolder newHolder = (HomeDianzhuViewHolder) holder;
    ChannelMobile newItem = (ChannelMobile) getItem(position);

    // 店铺名字
    if (!TextUtils.isEmpty(newItem.getName())) {
      newHolder.dianzhuName.setText(newItem.getName());
    } else {
      newHolder.dianzhuName.setText("暂无信息");
    }



    if (!TextUtils.isEmpty(newItem.getLogo())) {
      Glide.with(activity)//更改图片加载框架
              .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + newItem.getLogo())
              .centerCrop()
              .placeholder(R.drawable.pulamsi_loding)
              .crossFade()
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .into(newHolder.imageView);
//      PulamsiApplication.imageLoader.displayImage(PulamsiApplication.context.getString(R.string.serverbaseurl) + newItem.getLogo(), newHolder.imageView, PulamsiApplication.options);
    }
  }
  @SuppressWarnings("ResourceType")
  public class DianzhuItemLayout extends LinearLayout {
    public ImageView imageView;
    public TextView dianzhuName;

    public DianzhuItemLayout(final Context context) {
      super(context);
      this.setLayoutParams(new TRecyclerView.LayoutParams(
              (int) (PulamsiApplication.ScreenWidth * 0.5),
              TRecyclerView.LayoutParams.WRAP_CONTENT));
      this.setPadding((int) (PulamsiApplication.ScreenWidth * 0.01), (int) (PulamsiApplication.ScreenWidth * 0.01), (int) (PulamsiApplication.ScreenWidth * 0.01), (int) (PulamsiApplication.ScreenWidth * 0.01));
      RelativeLayout relativeLayout = new RelativeLayout(context);
      relativeLayout.setBackgroundColor(Color.WHITE);
      relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
      imageView = new ImageView(context);
      imageView.setId(1);
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
      setBackgroundColor(Color.parseColor("#f1f1f1"));
      setGravity(RelativeLayout.CENTER_IN_PARENT);
      LayoutParams imageParams = new LayoutParams(
              (int) (PulamsiApplication.ScreenWidth * 0.49), (int) (PulamsiApplication.ScreenWidth * 0.49));
      relativeLayout.addView(imageView, imageParams);
      dianzhuName = new TextView(context);
      dianzhuName.setId(2);
      RelativeLayout.LayoutParams explainParams = new RelativeLayout.LayoutParams(
              RelativeLayout.LayoutParams.WRAP_CONTENT, (int) (PulamsiApplication.ScreenWidth * 0.1));
      explainParams.addRule(RelativeLayout.BELOW, 1);
      dianzhuName.setSingleLine();
      dianzhuName.setPadding((int) (PulamsiApplication.ScreenWidth * 0.02), 0, 0, (int) (PulamsiApplication.ScreenWidth * 0.02));
      dianzhuName.setEllipsize(TextUtils.TruncateAt.END);
      dianzhuName.setTextColor(Color.parseColor("#363636"));
      dianzhuName.setGravity(Gravity.CENTER_VERTICAL);
      dianzhuName.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtil.dp2px(16f));
      relativeLayout.addView(dianzhuName, explainParams);
      this.addView(relativeLayout);
    }
  }
}
