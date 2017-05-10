package com.pulamsi.myinfo.myteam.viewholer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * 我的团队子项
 */
public class MyTeamViewHolder extends RecyclerView.ViewHolder {

  public TextView name; //名字
  public TextView phone; //电话
  public TextView level; //等级

  public MyTeamViewHolder(View itemView) {
    super(itemView);

    name = (TextView) itemView.findViewById(R.id.my_team_name);
    phone = (TextView) itemView.findViewById(R.id.my_team_phone);
    level = (TextView) itemView.findViewById(R.id.my_team_level);
  }
}
