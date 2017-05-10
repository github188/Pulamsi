package com.pulamsi.myinfo.myteam.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.pulamsi.R;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.myteam.entity.TeamBean;
import com.pulamsi.myinfo.myteam.viewholer.MyTeamViewHolder;
import com.pulamsi.myinfo.order.viewholder.OrderItemViewHolder;

/**
 *
 * 我的团队
 *
 */
public class MyTeamListAdapter extends HaiBaseListAdapter<TeamBean> {

  private Activity activity;


  public MyTeamListAdapter(Activity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.myteam_item, null);
    if (convertView == null) {
      return null;
    }

    return new MyTeamViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    if (holder == null || !(holder instanceof MyTeamViewHolder) || !(getItem(position) instanceof TeamBean)) {
      return;
    }

    MyTeamViewHolder newHolder = (MyTeamViewHolder) holder;
    final TeamBean newItem = (TeamBean) getItem(position);

    //姓名
    if (!TextUtils.isEmpty(newItem.getUsername())){
      newHolder.name.setText(newItem.getUsername());
    }else {
      newHolder.name.setText("暂无信息");
    }

    //电话
//    if (!TextUtils.isEmpty(newItem.getMobile())){
//      newHolder.phone.setText(newItem.getMobile());
//    }

    //等级
    if (!TextUtils.isEmpty(newItem.getVenderRankName())){
      newHolder.level.setText(newItem.getVenderRankName());
    }else {
      newHolder.level.setText("暂无等级");
    }



  }
}
