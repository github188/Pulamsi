package com.pulamsi.myinfo.slotmachineManage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.myinfo.slotmachineManage.adapter.SelectSlotmachineAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.VenderBean;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.ArrayList;


/**
 * 选择售货机界面
 */
public class DiscountSelectSlotmachineActivity extends BaseActivity {

  /**
   * 确认按钮
   */
  private TextView affirm;
  /**
   * 选择商品列表
   */
  private TRecyclerView tRecyclerView;
  /**
   * 适配器
   */
  private SelectSlotmachineAdapter selectSlotmachineAdapter;
  /**
   * 全选
   */
  private CheckBox checkAll;
  /**
   * 选择商品数据
   */
  private ArrayList<VenderBean> venderBeans;

  @SuppressLint("HandlerLeak")
  Handler handler = new Handler() {

    public void handleMessage(android.os.Message msg) {
      switch (msg.what) {
        case 100:
          checkAll.setChecked(isAllSelected());
          break;
      }
    };
  };


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    venderBeans = (ArrayList<VenderBean>) getIntent().getSerializableExtra("venderBeans");
    setContentView(R.layout.slotmachine_manage_discount_selectslotmachine);
    initUI();
  }

  private void initUI() {
    setHeaderTitle(R.string.slotmachine_manage_discountselectproduct_title);

    affirm = (TextView) findViewById(R.id.slotmachine_manage_discount_selectslotmachine_affirm);
    checkAll = (CheckBox) findViewById(R.id.slotmachine_manage_discount_selectslotmachine_checkbox);
    tRecyclerView = (TRecyclerView) findViewById(R.id.slotmachine_manage_discount_selectslotmachine_trecyclerview);


    selectSlotmachineAdapter = new SelectSlotmachineAdapter(DiscountSelectSlotmachineActivity.this,handler);
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    tRecyclerView.setLayoutManager(layoutManager);
    tRecyclerView.setAdapter(selectSlotmachineAdapter);
    tRecyclerView.setHasFixedSize(true);
    selectSlotmachineAdapter.addDataList(venderBeans);
    selectSlotmachineAdapter.notifyItemRangeInserted(0, venderBeans.size());

    checkAll.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        for (int i = 0; i < venderBeans.size(); i++) {
          venderBeans.get(i).setIsselect(checkAll.isChecked());
        }
        selectSlotmachineAdapter.notifyDataSetChanged();
      }
    });
    affirm.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectSlotmachine", getSelectSlotmachineIndex());
        resultIntent.putExtras(bundle);
        DiscountSelectSlotmachineActivity.this.setResult(RESULT_OK, resultIntent);
        DiscountSelectSlotmachineActivity.this.finish();
      }
    });
  }

  /**
   * 判断是否所有的商品全部被选中
   *
   * @return true所有条目全部被选中 false还有条目没有被选中
   */
  private boolean isAllSelected() {
    boolean flag = true;
    for (int i = 0; i < venderBeans.size(); i++) {
      if (!venderBeans.get(i).isIsselect()) {
        flag = false;
        break;
      }
    }
    return flag;
  }

  private ArrayList<Integer> getSelectSlotmachineIndex() {
    ArrayList<Integer>  list = new ArrayList<Integer>();
    for (int i = 0; i < venderBeans.size(); i++) {
      if (venderBeans.get(i).isIsselect()) {
        list.add(i);
      }
    }
    return list;
  }
}
