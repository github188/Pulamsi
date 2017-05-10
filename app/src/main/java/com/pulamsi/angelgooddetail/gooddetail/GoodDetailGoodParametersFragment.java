package com.pulamsi.angelgooddetail.gooddetail;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.angel.bean.AngelProductBean;
import com.pulamsi.base.baseActivity.BaseFragment;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.base.otto.MessageEvent;
import com.pulamsi.evaluate.EvaluateActivity;
import com.pulamsi.evaluate.entity.Estimate;
import com.pulamsi.evaluate.entity.EstimateList;
import com.pulamsi.utils.ShareUtils;
import com.pulamsi.utils.bean.CoutDownBean;
import com.pulamsi.utils.bean.DatePoorBean;
import com.pulamsi.views.PhotoView.PhotoviewActivity;
import com.pulamsi.views.SnapUpCountDownTimerView.SnapUpCountDownTimerView;
import com.pulamsi.views.gallery.BannerAdapter;
import com.pulamsi.views.gallery.DotContainer;
import com.pulamsi.views.gallery.SliderBanner;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class GoodDetailGoodParametersFragment extends BaseFragment implements OnClickListener {


    /**
     * 商品对象
     */
    private AngelProductBean angelProductBean;
    private CoutDownBean coutDownBean;

    /**
     * 图片轮播控件
     */
    private SliderBanner mSliderBanner;
    /**
     * 图片轮播点
     */
    private DotContainer mDotContainer;

    /**
     * 商品名字
     */
    private TextView productName;
    /**
     * 商品价格
     */
    private TextView productPrice;
    /**
     * 商品历史价格
     */
    private TextView productMarketPrice;
    /**
     * 是否支持自取
     */
    private TextView isAutoSell;

    /**
     * 更多价格
     */
    private TextView morePrice;
    /**
     * 送积分
     */
    private TextView jf;
    /**
     * 销量
     */
    private TextView sales;
    /**
     * 抢购条
     */
    private View snapUpBar;
    /**
     * 商品价格条目
     */
    private RelativeLayout productPriceBar;

    /**
     * 抢购的价格显示
     */
    private TextView snapUpSales;
    private TextView snapUpPrice;
    private TextView snapUpPanicBuyPrice;
    private TextView coutDownHint;
    /**
     * 抢购倒计时
     */
    private SnapUpCountDownTimerView countDownTimer;
    private RelativeLayout SnapBar;
    private DatePoorBean datePoor;


    private TextView evaluateUsername, evaluateContent, evaluateTime, evaluateEmpty;

    private RatingBar evaluateRatingBar;

    ImageView imageView;


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.good_detail_good_parameters_share:
                //分享按钮
                ShareUtils.showProductShare(getActivity(), angelProductBean);
                break;
            case R.id.good_detail_evaluate_all:
                //查看全部评价
                Intent evaluate = new Intent(getActivity(), EvaluateActivity.class);
                evaluate.putExtra("productId", angelProductBean.getId());
                evaluate.putExtra("isAngelProduct", true);//判断是天使商品还是普通商品
                getActivity().startActivity(evaluate);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.good_detail_good_parameters_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        snapUpBar = view.findViewById(R.id.snap_up_bar);
        productPriceBar = (RelativeLayout) view.findViewById(R.id.rl_product_price_bar);
        //抢购价格
        snapUpPanicBuyPrice = (TextView) view.findViewById(R.id.snap_up_panic_buy_price);
        snapUpSales = (TextView) view.findViewById(R.id.snap_up_sales);
        snapUpPrice = (TextView) view.findViewById(R.id.snap_up_price);
        coutDownHint = (TextView) view.findViewById(R.id.tv_cout_down_hint);
        //抢购倒计时
        countDownTimer = (SnapUpCountDownTimerView) view.findViewById(R.id.sudtv_countDownTimerView);

        mSliderBanner = (SliderBanner) view.findViewById(R.id.good_detail_sbanner);
        mDotContainer = (DotContainer) view.findViewById(R.id.good_detail_dotcontainer);
        productName = (TextView) view.findViewById(R.id.good_detail_productname);
        productPrice = (TextView) view.findViewById(R.id.good_detail_productprice);
        productMarketPrice = (TextView) view.findViewById(R.id.good_detail_productMarketPrice);
        isAutoSell = (TextView) view.findViewById(R.id.good_detail_isAutoSell);
        isAutoSell.setText("不支持");
        morePrice = (TextView) view.findViewById(R.id.good_detail_morePrice);
        morePrice.setVisibility(View.GONE);
        morePrice.setOnClickListener(this);
        TextView evaluateAll = (TextView) view.findViewById(R.id.good_detail_evaluate_all);
        evaluateAll.setOnClickListener(this);
        LinearLayout share = (LinearLayout) view.findViewById(R.id.good_detail_good_parameters_share);
        share.setOnClickListener(this);
        evaluateRatingBar = (RatingBar) view.findViewById(R.id.good_detail_evaluate_ratingbar);
        evaluateUsername = (TextView) view.findViewById(R.id.good_detail_evaluate_username);
        evaluateContent = (TextView) view.findViewById(R.id.good_detail_evaluate_content);
        evaluateTime = (TextView) view.findViewById(R.id.good_detail_evaluate_time);
        evaluateEmpty = (TextView) view.findViewById(R.id.evaluate_empty);
        jf = (TextView) view.findViewById(R.id.good_detail_good_parameters_jf);
        sales = (TextView) view.findViewById(R.id.good_detail_sales);
    }

    private void initData() {
        if (null == angelProductBean) {
            return;
        }
        updateImgBanner(angelProductBean.getProductImages());
        if (angelProductBean.getIsMarketable() == null || angelProductBean.getIsMarketable()) {
            productName.setText(angelProductBean.getName());
        } else {
            productName.setText(angelProductBean.getName() + "(已下架)");
            productName.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }
        productPrice.setText("¥" + angelProductBean.getPrice());

        sales.setText("销量" + angelProductBean.getSales());
        productMarketPrice.setText("¥" + angelProductBean.getMarketPrice());
        productMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);


        String url = getString(R.string.serverbaseurl) + getString(R.string.angelGoodsEvaluate)
                + "?productId=" + angelProductBean.getId() + "&pageSize=1&productStars=0&pageNumber=1";
        StringRequest dataRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        EstimateList estimateList = gson.fromJson(result, EstimateList.class);
                        Estimate estimate = estimateList.getList().get(0);
                        if (null != estimate) {
                            evaluateEmpty.setVisibility(View.GONE);
                            evaluateRatingBar.setRating(estimate.getProductStars());
                            evaluateUsername.setText(estimate.getUserName());
                            evaluateTime.setText(estimate.getCreateDate());
                            evaluateContent.setText(estimate.getContent());
                        } else {
                            evaluateEmpty.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        evaluateEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                evaluateEmpty.setVisibility(View.VISIBLE);
            }
        });
        PulamsiApplication.requestQueue.add(dataRequest);
    }



    private void initCountDown() {
        datePoor = coutDownBean.getDatePoorBean();
        if (!coutDownBean.getIsEnd()) {
            countDownTimer.setTime((int) datePoor.getHour(), (int) datePoor.getMin(), (int) datePoor.getSec());
            countDownTimer.start();
        }
    }

    //把商品信息传递进来，给按钮
    public void setData(AngelProductBean angelProductBean) {
        this.angelProductBean = angelProductBean;
        initData();
    }

    /**
     * 更新商品详情图片 banner 数据
     *
     * @param imgBannerItems 图片 banner 数据
     */
    public void updateImgBanner(List<String> imgBannerItems) {
        if (imgBannerItems == null || imgBannerItems.size() == 0) {
            return;
        }

        GoodImgBannerAdapter bannerAdapter = new GoodImgBannerAdapter();
        bannerAdapter.setData(imgBannerItems);
        mSliderBanner.setAdapter(bannerAdapter);
        mDotContainer.setNum(imgBannerItems.size());
        mSliderBanner.stopPlay();
        mSliderBanner.beginPlay();
    }


    /**
     * 商品详情图片 banner 数据适配器
     */
    private class GoodImgBannerAdapter extends BannerAdapter<String> {

        @Override
        public View getView(LayoutInflater layoutInflater, final int position) {
            View convertView = layoutInflater.inflate(R.layout.home_banner_item, null);
            final String item = getItem(position);
            if (item == null) {
                return convertView;
            }

            imageView = (ImageView) convertView.findViewById(R.id.home_banner_pic);
            if (!TextUtils.isEmpty(item)) {
                Glide.with(getActivity())//更改图片加载框架
                        .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + item)
                        .fitCenter()
                        .placeholder(R.drawable.pulamsi_loding)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
//                PulamsiApplication.imageLoader.displayImage(PulamsiApplication.context.getString(R.string.serverbaseurl) + item, imageView, PulamsiApplication.options);
            }

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PhotoviewActivity.class);
                    intent.putExtra("photo_position", position);
                    intent.putStringArrayListExtra("imagerUrl", (ArrayList<String>) mDataList);
                    getActivity().startActivity(intent);
                }
            });


            convertView.setTag(new InnerBannerItemDataWrapper(getPositionForIndicator(position), item));
            return convertView;
        }
    }

    private static class InnerBannerItemDataWrapper {

        private String mItem;
        private int mIndex;

        private InnerBannerItemDataWrapper(int index, String item) {
            mIndex = index;
            mItem = item;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }


    @Subscribe   //订阅事件DataChangedEvent
    public void OnSnapupEvent(MessageEvent event) {
        if (event.getTag().equals("complete")) {
            if (!coutDownBean.getIsBegin()) {
                snapUpBar.setBackgroundColor(getResources().getColor(R.color.app_btn_main_color));
                DatePoorBean datePoorTotalBean = coutDownBean.getDatePoorTotalBean();
                LogUtils.e(datePoorTotalBean.toString());
                //倒计时重置为抢购开始
                countDownTimer.setTime((int) datePoorTotalBean.getHour(), (int) datePoorTotalBean.getMin(), (int) datePoorTotalBean.getSec());
                countDownTimer.start();
                /**
                 * 延迟一秒，防止另外一个订阅者么有收到通知
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        coutDownBean.setIsBegin(true);
                    }
                }, 2000);
            } else {
                coutDownHint.setText("抢购结束");
                snapUpBar.setBackgroundColor(getResources().getColor(R.color.app_pulamsi_main_color));
            }
        }
    }
}

