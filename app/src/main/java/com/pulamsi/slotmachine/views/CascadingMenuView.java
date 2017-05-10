package com.pulamsi.slotmachine.views;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pulamsi.R;
import com.pulamsi.slotmachine.adapter.MenuItemAdapter;
import com.pulamsi.slotmachine.entity.Area;
import com.pulamsi.slotmachine.interfaces.CascadingMenuViewOnSelectListener;
import com.pulamsi.slotmachine.utils.SlotmachineDBhelper;

/**
 * 三级级联动ListView
 * 
 */
public class CascadingMenuView extends LinearLayout {
	private static final String TAG = CascadingMenuView.class.getSimpleName();
	// 三级菜单选择后触发的接口，即最终选择的内容
	private CascadingMenuViewOnSelectListener mOnSelectListener;
	private ListView provinceMenuListView;
	private ListView cityMenuListView;
	private ListView districtMenuListView;

	// 每次选择的子菜单内容
	private ArrayList<Area> districts = new ArrayList<Area>();//区域
	private ArrayList<Area> citys = new ArrayList<Area>();//城市
	private ArrayList<Area> provinces;//省份

	private MenuItemAdapter provinceMenuListViewAdapter;

	private MenuItemAdapter cityMenuListViewAdapter;

	private MenuItemAdapter districtMenuListViewAdapter;

	private int firstPosition = 0;
	private int cityPosition = 0;
	private int districtPosition = 0;

	private SlotmachineDBhelper dBhelper;

	/**
	 * 选中的地区对象
	 */
	private Area provine = null;
	private Area city = null;
	private Area district = null;


	/**
	 * @param context
	 *            上下文
	 */
	public CascadingMenuView(Context context, ArrayList<Area> menuList) {
		super(context);
		this.provinces = menuList;
		dBhelper = new SlotmachineDBhelper(context);
		/**
		 * 省份默认值
		 */
		provine = provinces.get(0);
		init(context);
	}

	public CascadingMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		dBhelper = new SlotmachineDBhelper(context);
		init(context);
	}

	private void init(final Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.slotmachine_view_region, this, true);
		provinceMenuListView = (ListView) findViewById(R.id.listView);
		cityMenuListView = (ListView) findViewById(R.id.listView2);
		districtMenuListView = (ListView) findViewById(R.id.listView3);

		// 初始化一级主菜单
		provinceMenuListViewAdapter = new MenuItemAdapter(context, provinces,
				R.drawable.choose_item_selected,
				R.drawable.choose_eara_item_selector);
		provinceMenuListViewAdapter.setTextSize(17);
		provinceMenuListViewAdapter.setSelectedPositionNoNotify(firstPosition,
				provinces);
		provinceMenuListView.setAdapter(provinceMenuListViewAdapter);
		provinceMenuListViewAdapter
				.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, int position) {
						// 选择主菜单，清空原本子菜单内容，增加新内容
						provine = provinces.get(position);
						citys.clear();
						citys = getCityItem(provine.getId());
						city = citys.get(0);

						// 通知适配器刷新
						cityMenuListViewAdapter.notifyDataSetChanged();
						cityMenuListViewAdapter.setSelectedPositionNoNotify(
								0, citys);

						districts.clear();
						districts = getDistrictItem(citys.get(0)
								.getId());
						// 通知适配器刷新
						districtMenuListViewAdapter.notifyDataSetChanged();
						districtMenuListViewAdapter.setSelectedPositionNoNotify(0,
								districts);
					}
				});
		// 初始化二级主菜单

		citys = getCityItem(provinces.get(firstPosition)
				.getId());
		/**
		 * 请求城市列表的时候，给城市一个默认值第一个
		 */
		city = citys.get(0);
		districts = getDistrictItem(citys.get(cityPosition)
				.getId());
		cityMenuListViewAdapter = new MenuItemAdapter(context, citys,
				R.drawable.choose_item_selected,
				R.drawable.choose_eara_item_selector);
		cityMenuListViewAdapter.setTextSize(15);
		cityMenuListViewAdapter.setSelectedPositionNoNotify(cityPosition,
				citys);
		cityMenuListView.setAdapter(cityMenuListViewAdapter);
		cityMenuListViewAdapter
				.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, final int position) {
						// 选择主菜单，清空原本子菜单内容，增加新内容
						city = citys.get(position);
						districts.clear();
						districts = getDistrictItem(city.getId());
						// 通知适配器刷新
						districtMenuListViewAdapter.notifyDataSetChanged();
						districtMenuListViewAdapter.setSelectedPositionNoNotify(0,
								districts);
					}
				});

		// 初始化三级主菜单
		districts = getDistrictItem(citys.get(cityPosition)
				.getId());
		districtMenuListViewAdapter = new MenuItemAdapter(context, districts,
				R.drawable.choose_item_right,
				R.drawable.choose_plate_item_selector);
		districtMenuListViewAdapter.setTextSize(13);
		districtMenuListViewAdapter.setSelectedPositionNoNotify(districtPosition,
				districts);
		districtMenuListView.setAdapter(districtMenuListViewAdapter);
		districtMenuListViewAdapter
				.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, final int position) {
						district = districts.get(position);
						if (mOnSelectListener != null) {
							if (null != provine && null != city && null != district)
								mOnSelectListener.getValue(provine, city, district);
						}
						Log.e(TAG, provinces.toString());
					}
				});
		// 设置默认选择
		setDefaultSelect();
	}

	public ArrayList<Area> getCityItem(String pcode) {
		
		ArrayList<Area> list = dBhelper.getCity(pcode);
		
		return list;

	}

	public ArrayList<Area> getDistrictItem(String pcode) {
		ArrayList<Area> list = dBhelper.getDistrict(pcode);
		
		return list;

	}

	public void setDefaultSelect() {
		provinceMenuListView.setSelection(firstPosition);
		cityMenuListView.setSelection(cityPosition);
		districtMenuListView.setSelection(districtPosition);
	}

	public void setCascadingMenuViewOnSelectListener(
			CascadingMenuViewOnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}
}
