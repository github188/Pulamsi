package com.pulamsi.views.selector;

import android.content.Context;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.pulamsi.slotmachine.entity.Area;
import com.pulamsi.views.selector.bean.ProvinceBean;

import java.util.ArrayList;

/**
 * Created by DaiDingKang on 2016/3/8.
 */
public class SexSelector {

    OptionsPickerView  pvOptions;
    private ArrayList<ProvinceBean> chooseSex = new ArrayList<ProvinceBean>();
    Context context;
    TextView view;
    private ArrayList<Area> areaProvinces;
    public SexSelector(Context context, TextView view){
        this.context = context;
        this.view = view;
        init();
    }

    private void init() {
        //选项选择器
        pvOptions = new OptionsPickerView(context);

        pvOptions.setCancelable(true);

        chooseSex.add(new ProvinceBean(0, "男", "", "male"));
        chooseSex.add(new ProvinceBean(0, "女", "", "female"));

        //三级联动效果
        pvOptions.setPicker(chooseSex);
        //设置选择的三级单位
        pvOptions.setTitle("选择性别");
        pvOptions.setCyclic(false, true, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        //默认选择第一选项的第0个
        pvOptions.setSelectOptions(0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = chooseSex.get(options1).getPickerViewText();
                String others = chooseSex.get(options1).getOthers();
                view.setTag(others);
                view.setText(tx);
            }
        });
    }

    public void show(){
        pvOptions.show();
    }
}
