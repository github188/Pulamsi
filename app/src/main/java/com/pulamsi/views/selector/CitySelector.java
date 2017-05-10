package com.pulamsi.views.selector;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.base.otto.MessageEvent;
import com.pulamsi.slotmachine.utils.SlotmachineDBhelper;
import com.pulamsi.views.dialog.LoadingDialog;

import java.util.HashMap;

/**
 * Created by DaiDingKang on 2016/3/8.
 */
public class CitySelector {

    LoadingDialog loadingDialog;
    OptionsPickerView  pvOptions;

    Context context;
    TextView view;
    Handler handler;
    public static boolean ThreadTag = false;
    SlotmachineDBhelper dBhelper;
    String provinceId,cityId,districtId;
    public CitySelector(Context context,TextView view){
        this.context = context;
        this.view = view;
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setCancelable(false);
    }
    public void init() {
        dBhelper = new SlotmachineDBhelper(context);
        //选项选择器
        pvOptions = new OptionsPickerView(context);
        //三级联动效果
        pvOptions.setPicker(PulamsiApplication.options1Items, PulamsiApplication.options2Items, PulamsiApplication.options3Items, true);
        //设置选择的三级单位
//        pvOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        pvOptions.setCancelable(true);
        //是否循环显示
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String province = PulamsiApplication.options1Items.get(options1).getPickerViewText();
                String city = PulamsiApplication.options2Items.get(options1).get(option2);
                String district = PulamsiApplication.options3Items.get(options1).get(option2).get(options3);

                String city_info = province+"-"+city+"-"+ district;
                view.setText(city_info);

                provinceId = dBhelper.getProvinceId(province);
                cityId = dBhelper.getCityId(city);
                districtId = dBhelper.getDistrictId(district);

                HashMap<String,String> map = new HashMap<String, String>();
                provinceId = PulamsiApplication.options1Items.get(options1).getOthers();
                map.put("provinceId",provinceId);
                map.put("cityId",cityId);
                map.put("districtId",districtId);
                BusProvider.getInstance().post(new MessageEvent(map, "area"));
            }
        });
    }

    public void show(){
        pvOptions.show();
    }
}
