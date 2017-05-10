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
import com.pulamsi.myinfo.slotmachineManage.adapter.SelectProductAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.GoodsInfo;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.ArrayList;


/**
 * 选择商品界面
 */
public class DiscountSelectProductActivity extends BaseActivity {

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
  private SelectProductAdapter selectProductAdapter;
  /**
   * 全选
   */
  private CheckBox checkAll;
  /**
   * 选择商品数据
   */
  private ArrayList<GoodsInfo> goodsInfos;

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
    goodsInfos = (ArrayList<GoodsInfo>) getIntent().getSerializableExtra("goodsInfos");
    setContentView(R.layout.slotmachine_manage_discount_selectproduct);
    initUI();
  }

  private void initUI() {
    setHeaderTitle(R.string.slotmachine_manage_discountselectproduct_title);

    affirm = (TextView) findViewById(R.id.slotmachine_manage_discount_selectgoods_affirm);
    checkAll = (CheckBox) findViewById(R.id.slotmachine_manage_discount_selectgoods_checkbox);
    tRecyclerView = (TRecyclerView) findViewById(R.id.slotmachine_manage_discount_selectgoods_trecyclerview);


    selectProductAdapter = new SelectProductAdapter(DiscountSelectProductActivity.this,handler);
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    tRecyclerView.setLayoutManager(layoutManager);
    tRecyclerView.setAdapter(selectProductAdapter);
    tRecyclerView.setHasFixedSize(true);
    selectProductAdapter.addDataList(goodsInfos);
    selectProductAdapter.notifyItemRangeInserted(0, goodsInfos.size());

    checkAll.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        for (int i = 0; i < goodsInfos.size(); i++) {
          goodsInfos.get(i).setIsselect(checkAll.isChecked());
        }
        selectProductAdapter.notifyDataSetChanged();
      }
    });
    affirm.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectProduct", getSelectProductIndex());
        resultIntent.putExtras(bundle);
        DiscountSelectProductActivity.this.setResult(RESULT_OK, resultIntent);
        DiscountSelectProductActivity.this.finish();
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
    for (int i = 0; i < goodsInfos.size(); i++) {
      if (!goodsInfos.get(i).isIsselect()) {
        flag = false;
        break;
      }
    }
    return flag;
  }

  private ArrayList<Integer> getSelectProductIndex() {
    ArrayList<Integer>  list = new ArrayList<Integer>();
    for (int i = 0; i < goodsInfos.size(); i++) {
      if (goodsInfos.get(i).isIsselect()) {
        list.add(i);
      }
    }
    return list;
  }
}
