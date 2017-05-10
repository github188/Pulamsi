package com.pulamsi.evaluate.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.evaluate.EvaluateFragment;
import com.pulamsi.evaluate.entity.Estimate;
import com.pulamsi.evaluate.viewholder.EvaluateViewHolder;
import com.pulamsi.myinfo.order.ChoicePayTypeActivity;
import com.pulamsi.myinfo.order.MyOrderFragment;
import com.pulamsi.myinfo.order.OrderDetailActivity;
import com.pulamsi.myinfo.order.TraceOrderActivity;
import com.pulamsi.myinfo.order.adapter.OrderProductAdapter;
import com.pulamsi.myinfo.order.entity.Order;
import com.pulamsi.myinfo.order.entity.PickupBean;
import com.pulamsi.myinfo.order.viewholder.OrderItemViewHolder;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.dialog.CommonAlertDialog;
import com.taobao.uikit.feature.view.TRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 商品评论适配器
 *
 */
public class EvaluateListAdapter extends HaiBaseListAdapter<Estimate> {

  private Activity activity;


  public EvaluateListAdapter(Activity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.evaluatelist_item, null);
    if (convertView == null) {
      return null;
    }

    return new EvaluateViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    if (holder == null || !(holder instanceof EvaluateViewHolder) || !(getItem(position) instanceof Estimate)) {
      return;
    }

    EvaluateViewHolder newHolder = (EvaluateViewHolder) holder;
    final Estimate newItem = (Estimate) getItem(position);

    //用户名
      newHolder.userName.setText(newItem.getUserName());
    //内容
      newHolder.content.setText(newItem.getContent());

    //时间
      newHolder.time.setText(newItem.getCreateDate());

    //评分
    newHolder.ratingBar.setRating(newItem.getProductStars());




  }
}
