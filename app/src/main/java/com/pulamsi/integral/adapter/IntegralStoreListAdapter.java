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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.home.entity.HotSellProduct;
import com.pulamsi.home.viewholder.HomeCateGoodViewHolder;
import com.pulamsi.myinfo.order.entity.Product;
import com.pulamsi.utils.DensityUtil;
import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * 积分商品列表数据适配器
 *
 * @author WilliamChik on 15/8/12.
 */
public class IntegralStoreListAdapter extends HaiBaseListAdapter<Product> {

    private String evaluateType;
    private Activity activity;

    public IntegralStoreListAdapter(Activity activity, String evaluateType) {
        super(activity);
        this.evaluateType = evaluateType;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = new HotSellProductItemLayout(parent.getContext());
        if (convertView == null) {
            return null;
        }

        return new HomeCateGoodViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null || !(holder instanceof HomeCateGoodViewHolder) || !(getItem(position) instanceof Product)) {
            return;
        }

        HomeCateGoodViewHolder newHolder = (HomeCateGoodViewHolder) holder;
        Product newItem = (Product) getItem(position);


        // 商品名称
        if (!TextUtils.isEmpty(newItem.getName())) {
            newHolder.explain.setText(newItem.getName());
        } else {
            newHolder.explain.setText("暂无信息");
        }
        // 商品价格

        //产品价格
        if ("1".equals(evaluateType)) {
            for (int i = 0; i < newItem.getProductPrices().size(); i++) {
                if (newItem.getProductPrices().get(i).getProductPrice().floatValue() == 0) {
                    newHolder.price.setText(newItem.getProductPrices().get(i).getIntegralPrice() + "积分");
                }
            }

        } else {
            for (int i = 0; i < newItem.getProductPrices().size(); i++) {
                if (newItem.getProductPrices().get(i).getProductPrice().floatValue() > 0) {
                    newHolder.price.setText("¥" + newItem.getProductPrices().get(i).getProductPrice().floatValue() + "+" + newItem.getProductPrices().get(i).getIntegralPrice() + "积分");
                }
            }

        }
//    String total = "¥0.00";
//    if (newItem.getProductPrice().floatValue() <= 0){
//      if (newItem.getIntegralPrice() > 0){
//        total = newItem.getIntegralPrice()+"积分";
//      }
//    }else {
//      if (newItem.getIntegralPrice() > 0){
//        total = "¥"+newItem.getProductPrice()+"\n+"+newItem.getIntegralPrice()+"积分";
//      }else {
//        total = "¥"+newItem.getProductPrice();
//      }
//    }

//    newHolder.price.setText("¥"+newItem.getPrice());

        if (!TextUtils.isEmpty(newItem.getPic())) {
            Glide.with(activity)//更改图片加载框架
                    .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + newItem.getPic())
                    .centerCrop()
                    .placeholder(R.drawable.pulamsi_loding)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(newHolder.imageView);
//            PulamsiApplication.imageLoader.displayImage(PulamsiApplication.context.getString(R.string.serverbaseurl) + newItem.getPic(), newHolder.imageView, PulamsiApplication.options);
        }


    }

    public class HotSellProductItemLayout extends LinearLayout {
        public ImageView imageView;
        public TextView explain;
        public TextView price;
        public TextView historical;

        public HotSellProductItemLayout(final Context context) {
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
            explain = new TextView(context);
            explain.setId(2);
            RelativeLayout.LayoutParams explainParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            explainParams.setMargins((int) (PulamsiApplication.ScreenWidth * 0.01), 0,
                    (int) (PulamsiApplication.ScreenWidth * 0.01), 0);
            explainParams.addRule(RelativeLayout.BELOW, 1);
            explain.setSingleLine();
            explain.setEllipsize(TextUtils.TruncateAt.END);
            explain.setTextColor(Color.parseColor("#363636"));
            explain.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    DensityUtil.dp2px(12f));
            relativeLayout.addView(explain, explainParams);
            price = new TextView(context);
            price.setId(3);
            RelativeLayout.LayoutParams priceParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            priceParams.setMargins((int) (PulamsiApplication.ScreenWidth * 0.01), 0,
                    (int) (PulamsiApplication.ScreenWidth * 0.01), 0);
            priceParams.addRule(RelativeLayout.BELOW, 2);
            price.setTextColor(Color.RED);
            price.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    DensityUtil.dp2px(14f));
            relativeLayout.addView(price, priceParams);
            historical = new TextView(context);
            historical.setId(4);
            RelativeLayout.LayoutParams historicalParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            historicalParams.setMargins((int) (PulamsiApplication.ScreenWidth * 0.01), 0,
                    (int) (PulamsiApplication.ScreenWidth * 0.01), 0);
            historicalParams.addRule(RelativeLayout.ALIGN_TOP, 3);
            historicalParams.addRule(RelativeLayout.RIGHT_OF, 3);
            historical.setTextColor(Color.GRAY);
            historical.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    DensityUtil.dp2px(12f));
            /**
             * 设置删除线
             */
            historical.getPaint().setFlags(
                    Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            relativeLayout.addView(historical, historicalParams);
            this.addView(relativeLayout);
        }
    }
}
