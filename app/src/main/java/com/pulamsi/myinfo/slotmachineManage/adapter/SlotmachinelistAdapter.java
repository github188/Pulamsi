package com.pulamsi.myinfo.slotmachineManage.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.pulamsi.R;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.slotmachineManage.SlotmachineListActivity;
import com.pulamsi.myinfo.slotmachineManage.entity.VenderBean;
import com.pulamsi.myinfo.slotmachineManage.view.ICheckBoxHelper;
import com.pulamsi.myinfo.slotmachineManage.viewholder.SlotmachineListViewHolder;

import java.util.HashMap;

/**
 * 售货机列表数据适配器
 */


public class SlotmachinelistAdapter extends HaiBaseListAdapter<VenderBean> implements ICheckBoxHelper {
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;
    private boolean isVisibilty;
    private Context mContext;
    /**
     * 选中数目
     */
    int checkNum;


    public SlotmachinelistAdapter(Activity activity) {
        super(activity);
        mContext = activity;
        isSelected = new HashMap<>();
    }

    // 初始化isSelected的数据
    public void inIsSelected() {
        for (int i = 0; i < getItemCount(); i++) {
            getIsSelected().put(i, false);
        }
        checkNum = 0;//一定要初始化，切记
        ((SlotmachineListActivity) mContext).bottomColumnBar.setIsEnable(false);//默认为False
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.slotmachine_list_item, null);
        if (convertView == null) {
            return null;
        }
        return new SlotmachineListViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder == null || !(holder instanceof SlotmachineListViewHolder) || !(getItem(position) instanceof VenderBean)) {
            return;
        }

        final SlotmachineListViewHolder newHolder = (SlotmachineListViewHolder) holder;
        VenderBean newItem = (VenderBean) getItem(position);


        // 售货机名字
        if (!TextUtils.isEmpty(newItem.getTerminalName())) {
            newHolder.name.setText(newItem.getTerminalName());
        } else {
            newHolder.name.setText("暂无信息");
        }


        // 售货机地址
        if (!TextUtils.isEmpty(newItem.getTerminalAddress())) {
            newHolder.address.setText(newItem.getTerminalAddress());
        } else {
            newHolder.address.setText("暂无地址");
        }
        // 售货机在线状态
        if (newItem.isOnline()) {
            newHolder.rightimage.setImageResource(R.drawable.ic_online);
        } else {
            newHolder.rightimage.setImageResource(R.drawable.ic_offline);
        }

        newHolder.rightimage.setVisibility(View.VISIBLE);

//        newHolder.smoothCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
//            //记录到MAP里面，将选择的值
//            @Override
//            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
//                isSelected.put(position, isChecked);
//                setIsSelected(isSelected);
//            }
//        });

        // 监听checkBox并根据原来的状态来设置新的状态
        newHolder.smoothCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                    newHolder.smoothCheckBox.setChecked(getIsSelected().get(position), true);
                    ((SlotmachineListActivity) mContext).setTopBarTitle(mContext.getString(R.string.slotmachine_check_all));
                    checkNum--;
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                    newHolder.smoothCheckBox.setChecked(getIsSelected().get(position), true);
                    checkNum++;
                }
                // 用TextView显示
                dataChanged();
            }
        });

        // 根据isSelected来设置checkbox的选中状况
        if (getItemCount() == getIsSelected().size())
            newHolder.smoothCheckBox.setChecked(getIsSelected().get(position));

        if (isVisibilty) {
            if (newHolder.smoothCheckBox.getVisibility() == View.GONE) {
                newHolder.smoothCheckBox.setVisibility(View.VISIBLE);
                newHolder.smoothCheckBox.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_security_bounce_animation));
            }
        } else {
            if (newHolder.smoothCheckBox.getVisibility() == View.VISIBLE) {
                newHolder.smoothCheckBox.setVisibility(View.GONE);
                newHolder.smoothCheckBox.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_out));
            }
        }
    }


    @Override
    public void showCheckBox() {
        inIsSelected();//初始化
        isVisibilty = true;
        this.notifyDataSetChanged();
        ((SlotmachineListActivity) mContext).showCheckBox();

    }

    @Override
    public void hideCheckBox() {
        isVisibilty = false;
        this.notifyDataSetChanged();//刷新界面
        ((SlotmachineListActivity) mContext).hideCheckBox();
    }

    @Override
    public void checkAll() {

        // 遍历list的长度，将MyAdapter中的map值全部设为true
        for (int i = 0; i < getItemCount(); i++) {
            if (!SlotmachinelistAdapter.getIsSelected().get(i)) {
                SlotmachinelistAdapter.getIsSelected().put(i, true);
            }
        }
        // 数量设为list的长度
        checkNum = getItemCount();
        // 刷新listview和TextView的显示
        dataChanged();
        this.notifyDataSetChanged();

        ((SlotmachineListActivity) mContext).setTopBarTitle(mContext.getString(R.string.slotmachine_uncheck_all));
    }

    @Override
    public void unCheckAll() {
        // 遍历list的长度，将MyAdapter中的map值全部设为true
        for (int i = 0; i < getItemCount(); i++) {
            if (SlotmachinelistAdapter.getIsSelected().get(i)) {
                SlotmachinelistAdapter.getIsSelected().put(i, false);
                checkNum--;// 数量减1
            }
        }
        dataChanged();
        this.notifyDataSetChanged();

        ((SlotmachineListActivity) mContext).setTopBarTitle(mContext.getString(R.string.slotmachine_check_all));
    }


    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        SlotmachinelistAdapter.isSelected = isSelected;
    }


    // 刷新listview和TextView的显示
    private void dataChanged() {
        // TextView显示最新的选中数目
        if (checkNum == 0) {
            ((SlotmachineListActivity) mContext).setHeaderText(mContext.getString(R.string.select_entry));
            ((SlotmachineListActivity) mContext).bottomColumnBar.setIsEnable(false);
        } else {
            ((SlotmachineListActivity) mContext).setHeaderText("已选中" + checkNum + "项");
            ((SlotmachineListActivity) mContext).bottomColumnBar.setIsEnable(true);
        }
    }
}
