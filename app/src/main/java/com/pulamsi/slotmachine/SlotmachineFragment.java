package com.pulamsi.slotmachine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseFragment;
import com.pulamsi.base.baseAdapter.TabFragmentAdapter;
import com.pulamsi.slotmachine.entity.Area;
import com.pulamsi.slotmachine.fragment.SlotmachineListFragment;
import com.pulamsi.slotmachine.fragment.SlotmachineMapFragment;
import com.pulamsi.slotmachine.interfaces.CascadingMenuViewOnSelectListener;
import com.pulamsi.slotmachine.utils.SlotmachineDBhelper;
import com.pulamsi.slotmachine.views.CascadingMenuPopWindow;
import com.pulamsi.views.MyViewPager;

import java.util.ArrayList;
import java.util.List;

public class SlotmachineFragment extends BaseFragment implements OnClickListener {

    /**
     * 下拉列表省份数据
     */
    private ArrayList<Area> areaProvinces;
    /**
     * popwindow
     */
    private CascadingMenuPopWindow cascadingMenuPopWindow = null;
    /**
     * fragment切换按钮
     */
    private TextView mode;
    private TextView city;
    private TextView district;
    private TextView province;
    private int index = 0;
    private MyViewPager viewPager;

    private SlotmachineMapFragment slotmachineMapFragment;
    private SlotmachineListFragment slotmachineListFragment;

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slotmachine_mode:
                slotmachineMapFragment.hideSlotMachintInfo();//隐藏售货机信息
                if (index == 0) {
                    viewPager.setCurrentItem(1);
                    mode.setText("地图");
                } else {
                    viewPager.setCurrentItem(0);
                    mode.setText("列表");
                }
                index = viewPager.getCurrentItem();
                break;
            case R.id.slotmachine_city:
            case R.id.slotmachine_district:
            case R.id.slotmachine_province:
                showPopMenu();
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slotmachine_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SlotmachineDBhelper dBhelper = new SlotmachineDBhelper(SlotmachineFragment.this.getActivity());
        areaProvinces = dBhelper.getProvince();
        mode = (TextView) view.findViewById(R.id.slotmachine_mode);
        city = (TextView) view.findViewById(R.id.slotmachine_city);
        district = (TextView) view.findViewById(R.id.slotmachine_district);
        province = (TextView) view.findViewById(R.id.slotmachine_province);
        mode.setOnClickListener(this);
        city.setOnClickListener(this);
        district.setOnClickListener(this);
        province.setOnClickListener(this);


        /**
         * fragment跳转列表和viewpager
         */
        slotmachineMapFragment = new SlotmachineMapFragment();
        slotmachineListFragment = new SlotmachineListFragment();
        List<Fragment> tabFragments = new ArrayList<Fragment>();
        tabFragments.add(slotmachineListFragment);
        tabFragments.add(slotmachineMapFragment);
        viewPager = (MyViewPager) view.findViewById(R.id.slotmachine_viewpager);
        TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(PulamsiApplication.fragmentActivity.getSupportFragmentManager(), tabFragments);
        viewPager.setAdapter(tabFragmentAdapter);
    }

    private void showPopMenu() {
        if (cascadingMenuPopWindow == null) {
            cascadingMenuPopWindow = new CascadingMenuPopWindow(getActivity(), areaProvinces);
            cascadingMenuPopWindow
                    .setMenuViewOnSelectListener(new CascadingMenuViewOnSelectListener() {

                        @Override
                        public void getValue(Area province, Area city, Area district) {
                            SlotmachineFragment.this.district.setText(district.getName());
                            SlotmachineFragment.this.city.setText(city.getName());
                            SlotmachineFragment.this.province.setText(province.getName());
                            if (index == 0) {
                                slotmachineListFragment.initData(province.getId(),city.getId(),district.getId());
                            } else if (index == 1) {
                                slotmachineMapFragment.geocode(province.getName() + city.getName(), district.getName());
                                slotmachineMapFragment.getverderList(province.getId(),city.getId(),district.getId());
                            }
                        }
                    });
            cascadingMenuPopWindow.showAsDropDown(mode, 5, 5);
        } else if (cascadingMenuPopWindow != null
                && cascadingMenuPopWindow.isShowing()) {
            cascadingMenuPopWindow.dismiss();
        } else {
            cascadingMenuPopWindow.showAsDropDown(mode, 5, 5);
        }
    }

}

