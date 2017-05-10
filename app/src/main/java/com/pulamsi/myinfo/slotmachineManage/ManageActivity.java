package com.pulamsi.myinfo.slotmachineManage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.myinfo.slotmachineManage.adapter.SlotmachineMenuListAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.SlotmachineMenu;
import com.pulamsi.start.init.entity.Role;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.pulamsi.R.id.slotmachine_manage_trecyclerview;


/**
 * 售货机管理界面
 */
public class ManageActivity extends BaseActivity implements TRecyclerView.OnItemClickListener {
    /**
     * 售货机管理菜单列表
     */
    private TRecyclerView slotmachineManageTRecyclerView;

    /**
     * 售货机管理菜单适配器
     */
    private SlotmachineMenuListAdapter slotmachineMenuListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slotmachine_manage_activity);
        initUI();
    }

    private void initUI() {
        setHeaderTitle(R.string.slotmachine_manage_title);
        slotmachineManageTRecyclerView = (TRecyclerView) findViewById(slotmachine_manage_trecyclerview);
        slotmachineMenuListAdapter = new SlotmachineMenuListAdapter(this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        slotmachineManageTRecyclerView.setLayoutManager(layoutManager);
        slotmachineManageTRecyclerView.setAdapter(slotmachineMenuListAdapter);
        slotmachineManageTRecyclerView.setHasFixedSize(true);
        slotmachineManageTRecyclerView.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        try {
            /**
             * 数据初始化
             */
            ArrayList<SlotmachineMenu> slotmachineMenus = new ArrayList<>();

            if (null != PulamsiApplication.dbUtils.findFirst(Selector.from(Role.class).where("name", "=", "售货机查询"))) {
                SlotmachineMenu slotmachine1 = new SlotmachineMenu();
                slotmachine1.setContent("售货机列表");
                slotmachine1.setTag(1);
                slotmachine1.setImageRID(R.drawable.slotmachine_list);
                slotmachineMenus.add(slotmachine1);
            }
            if (null != PulamsiApplication.dbUtils.findFirst(Selector.from(Role.class).where("name", "=", "地图信息"))) {
                SlotmachineMenu slotmachine2 = new SlotmachineMenu();
                slotmachine2.setContent("地图信息");
                slotmachine2.setTag(2);
                slotmachine2.setImageRID(R.drawable.slotmachine_map);
                slotmachineMenus.add(slotmachine2);
            }
            if (null != PulamsiApplication.dbUtils.findFirst(Selector.from(Role.class).where("name", "=", "缺货快速浏览"))) {
                SlotmachineMenu slotmachine3 = new SlotmachineMenu();
                slotmachine3.setContent("缺货查看");
                slotmachine3.setTag(3);
                slotmachine3.setImageRID(R.drawable.slotmachine_lack);
                slotmachineMenus.add(slotmachine3);
            }
            if (null != PulamsiApplication.dbUtils.findFirst(Selector.from(Role.class).where("name", "=", "故障快速浏览"))) {
                SlotmachineMenu slotmachine4 = new SlotmachineMenu();
                slotmachine4.setTag(4);
                slotmachine4.setContent("故障查看");
                slotmachine4.setImageRID(R.drawable.slotmachine_malfunction);
                slotmachineMenus.add(slotmachine4);
            }
            if (null != PulamsiApplication.dbUtils.findFirst(Selector.from(Role.class).where("name", "=", "交易查询"))) {
                SlotmachineMenu slotmachine5 = new SlotmachineMenu();
                slotmachine5.setTag(5);
                slotmachine5.setContent("交易查询");
                slotmachine5.setImageRID(R.drawable.slotmachine_transaction_query);
                slotmachineMenus.add(slotmachine5);
            }
            if (null != PulamsiApplication.dbUtils.findFirst(Selector.from(Role.class).where("name", "=", "售货机列表"))) {
                SlotmachineMenu slotmachine6 = new SlotmachineMenu();
                slotmachine6.setTag(6);
                slotmachine6.setContent("添加售货机");
                slotmachine6.setImageRID(R.drawable.slotmachine_add);
                slotmachineMenus.add(slotmachine6);

                SlotmachineMenu slotmachine7 = new SlotmachineMenu();
                slotmachine7.setTag(7);
                slotmachine7.setContent("商品打折");
                slotmachine7.setImageRID(R.drawable.slotmachine_transaction);
                slotmachineMenus.add(slotmachine7);
            }
            if (null != PulamsiApplication.dbUtils.findFirst(Selector.from(Role.class).where("name", "=", "退款管理"))) {
                SlotmachineMenu slotmachine8 = new SlotmachineMenu();
                slotmachine8.setTag(8);
                slotmachine8.setContent("退款管理");
                slotmachine8.setImageRID(R.drawable.app_shop);
                slotmachineMenus.add(slotmachine8);
            }
            initListData(slotmachineMenus);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 装配售货机菜单数据
     */
    public void initListData(List<SlotmachineMenu> data) {
        //  状态还原
        int formerItemCount = slotmachineMenuListAdapter.getItemCount();
        slotmachineMenuListAdapter.clearDataList();
        slotmachineMenuListAdapter.notifyItemRangeRemoved(0, formerItemCount);
        slotmachineMenuListAdapter.addDataList(data);
        slotmachineMenuListAdapter.notifyItemRangeInserted(0, data.size());
    }

    /**
     * 点击事件跳转
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */

    @Override
    public void onItemClick(TRecyclerView parent, View view, int position, long id) {
        HaiLog.d("售货机管理菜单", position + "");
        SlotmachineMenu slotmachineMenu = slotmachineMenuListAdapter.getItem(position);
        switch (slotmachineMenu.getTag()) {
            case 1:
                /**
                 * 售货机列表
                 */
                Intent list = new Intent(ManageActivity.this, SlotmachineListActivity.class);
                startActivity(list);
                break;
            case 2:
                /**
                 * 地图信息
                 */
                Intent map = new Intent(ManageActivity.this, SlotmachineMapListActivity.class);
                startActivity(map);
                break;
            case 3:
                /**
                 * 缺货查看
                 */
                Intent stockoutConditionActivity = new Intent(ManageActivity.this, StockoutConditionActivity.class);
                startActivity(stockoutConditionActivity);
                break;
            case 4:
                /**
                 * 故障查看
                 */
                Intent fault = new Intent(ManageActivity.this, FaultActivity.class);
                startActivity(fault);
                break;
            case 5:
                /**
                 * 交易查询
                 */
                Intent deal = new Intent(ManageActivity.this, DealListActivity.class);
                startActivity(deal);
                break;
            case 6:
                /**
                 * 添加售货机
                 */
                Intent addvender = new Intent(ManageActivity.this, AddVenderActivity.class);
                startActivity(addvender);
                break;
            case 7:
                /**
                 * 商品打折
                 */
                Intent discount = new Intent(ManageActivity.this, DiscountActivity.class);
                startActivity(discount);
                break;
            case 8:
                /**
                 * 退款管理
                 */
                Intent refund = new Intent(ManageActivity.this, RefundManagementActivity.class);
                startActivity(refund);
                break;

        }
    }

    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }
}
