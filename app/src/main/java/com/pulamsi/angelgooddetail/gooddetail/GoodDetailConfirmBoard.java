package com.pulamsi.angelgooddetail.gooddetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.angel.bean.AngelProductBean;
import com.pulamsi.angelgooddetail.gooddetail.adapter.PriceListAdapter;
import com.pulamsi.myinfo.order.OrderConfirmationActivity;
import com.pulamsi.myinfo.order.entity.ProductPrice;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.ItemNumberEditView;
import com.pulamsi.views.dialog.CommonBaseSafeDialog;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.ArrayList;


/**
 * 商品详情确认面板（【立即购买】 和 【加入购物车】 公用），整个编辑面板本身是一个自定义的 dialog，封装编辑面板里的相关操作
 *
 * @author WilliamChik on 2015/9/16.
 */
public class GoodDetailConfirmBoard extends CommonBaseSafeDialog implements View.OnClickListener {

    private AngelProductBean mItemDO;

    private GoodDetailBottomOperationBarFragment goodDetailBottomOperationBarFragment;

    private TRecyclerView priceTRecyclerView;

    private PriceListAdapter priceListAdapter;

    private ArrayList<ProductPrice> productPrices;

    // 当前的确认面板是属于【加入购物车】还是【立即购买】
    private boolean mIsAddToShoppingCar;

    // 数量编辑按钮
    private ItemNumberEditView mNumberEditView;

    // sku text view
    private TextView mGoodSkuTv;

    String count;//商品数量
    String priceId;

    private Activity activity;

    public void setIsAddToShoppingCar(boolean isAddToShoppingCar) {
        mIsAddToShoppingCar = isAddToShoppingCar;
    }

    public GoodDetailConfirmBoard(Activity activity, GoodDetailBottomOperationBarFragment goodDetailBottomOperationBarFragment, AngelProductBean itemDO) {
        super(activity, R.style.Hai_AlertDialogFromBottom);
        this.goodDetailBottomOperationBarFragment = goodDetailBottomOperationBarFragment;
        this.activity = activity;
        mItemDO = itemDO;
    }

    public void initUI() {
        ImageView goodImg = (ImageView) findViewById(R.id.civ_good_detail_board_good_img);
        TextView goodPrice = (TextView) findViewById(R.id.tv_good_detail_board_good_price);
        mGoodSkuTv = (TextView) findViewById(R.id.tv_good_detail_board_sku_str);
        mNumberEditView = (ItemNumberEditView) findViewById(R.id.inev_good_detail_edit_number);
        priceTRecyclerView = (TRecyclerView) findViewById(R.id.price_trecyclerview);
        ImageView boardCloseBtn = (ImageView) findViewById(R.id.iv_good_detail_board_close_btn);
        TextView confirmBtn = (TextView) findViewById(R.id.tv_good_detail_board_confirm_btn);
        boardCloseBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        /**
         * 商品图片
         */
        String urlPath = activity.getString(R.string.serverbaseurl) + mItemDO.getPic();

        Glide.with(activity)//更改图片加载框架
                .load(urlPath)
                .centerCrop()
//              .placeholder(R.drawable.pulamsi_loding)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(goodImg);
//    PulamsiApplication.imageLoader.displayImage(urlPath, goodImg, PulamsiApplication.options);

        // 商品价格
        goodPrice.setText("¥" + mItemDO.getPrice());

        // 商品上限
        mNumberEditView.setDiplayNumAndUpperLimit("1", "200");

        mGoodSkuTv.setText(mItemDO.getName());
    }


    public void setOnItemSelect(int position) {
        for (int i = 0; i < productPrices.size(); i++) {
            if (i == position) {
                productPrices.get(i).setIsSelect(true);
            } else {
                productPrices.get(i).setIsSelect(false);
            }
        }
        priceListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_detail_confirm_board);
        // 5.0系统以上时，布局相关的属性不能通过 style 文件的方式来设置性，只能通过代码设置
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        initUI();
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_good_detail_board_close_btn) {
            hide();
        } else if (viewId == R.id.tv_good_detail_board_confirm_btn) {
            /**
             *  hide() 必须在发送消息之前，因为 dialog 显示的时候 fragment 正处于 onPause() 状态，是无法订阅消息的，
             *  只有把 dialog 隐藏了 fragment 才能显示出来
             */
            hide();
            priceId = "0";
            if (null != productPrices) {
                for (int i = 0; i < productPrices.size(); i++) {
                    if (productPrices.get(i).isSelect()) {
                        priceId = productPrices.get(i).getId() + "";
                        break;
                    }
                }
            }
            count = mNumberEditView.getItemDisplayNumString();
            if (!TextUtils.isEmpty(count) && Integer.parseInt(count) < 1) {
                ToastUtil.showToast("请您至少选择一件商品");
                return;
            }
            if (mIsAddToShoppingCar) {
                //加入购物车
                goodDetailBottomOperationBarFragment.addShoppingCar(count, priceId);
                LogUtils.e(mItemDO.getId());
            } else {
                    Intent confirmation = new Intent(activity, OrderConfirmationActivity.class);
                    confirmation.putExtra("isonce", true);
                    confirmation.putExtra("isSnapUp", false);//不是抢购
                    confirmation.putExtra("isAngel", true);//是天使商品
                    confirmation.putExtra("priceId", priceId);//价格id
                    confirmation.putExtra("sn", mItemDO.getSn());//商品编号
                    confirmation.putExtra("count", count);//商品数量
                    activity.startActivity(confirmation);
            }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
