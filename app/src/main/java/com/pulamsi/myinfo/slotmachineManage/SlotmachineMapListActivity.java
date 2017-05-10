package com.pulamsi.myinfo.slotmachineManage;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.slotmachineManage.entity.VenderBean;

public class SlotmachineMapListActivity extends BaseActivity {
	// 定位相关
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;// 定位模式
	private boolean isFirstLoc = true;// 是否首次定位
	private BaiduMap mBaiduMap;
	private MapView mapView;
	private List<VenderBean> venderBeans;
	private BitmapDescriptor mCurrentMarker;// Marker图标

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getverderList();
		SDKInitializer.initialize(PulamsiApplication.context);
		mapView = new MapView(this);
		// 地图初始化
		mapView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		setContentView(mapView);
		setHeaderTitle("地图信息");

		mBaiduMap = mapView.getMap();


		mapView.setClickable(true);
		mCurrentMode = LocationMode.NORMAL;// 设置定位模式为普通
		mCurrentMarker = BitmapDescriptorFactory// 构建mark图标
				.fromResource(R.drawable.icon_marka);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		 mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
		 mCurrentMode, true, null));
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);// 注册监听函数：
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocClient.setLocOption(option);
		mLocClient.start();
		mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(final Marker arg0) {
				final VenderBean venderBean = venderBeans.get(arg0.getZIndex());
				MapMarkerinfoLayout layout = new MapMarkerinfoLayout(SlotmachineMapListActivity.this);
				TextView terminalName = (TextView) layout.findViewById(1);
				TextView sn = (TextView) layout.findViewById(2);
				TextView terminalAddress = (TextView) layout.findViewById(3);
				terminalName.setText("售货机名称:" + venderBean.getTerminalName());
				if (venderBean.isOnline()) {
					sn.setText("连接状态:在线");
				} else {
					sn.setText("连接状态:离线");
				}
				terminalAddress.setText("地址:" + venderBean.getTerminalAddress());
				InfoWindow infowindow_park = new InfoWindow(layout,
						arg0.getPosition(), -55);
				mBaiduMap.showInfoWindow(infowindow_park);
				layout.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent goodsRoadList = new Intent(SlotmachineMapListActivity.this, GoodsRoadListActivity.class);
						goodsRoadList.putExtra("searchMachineid", venderBean.getId());
						SlotmachineMapListActivity.this.startActivity(goodsRoadList);
					}
				});
				moveCenter(arg0);
				return false;
			}
		});
	}

	/**
	 * 移动中心位置
	 *
	 * @param marker
	 */
	private void moveCenter(Marker marker) {
		// 设置缩放比例,更新地图状态
		double latitude = marker.getPosition().latitude;
		double longitude = marker.getPosition().longitude;
		LatLng ll = new LatLng(latitude, longitude);
		float f = mBaiduMap.getMaxZoomLevel();// 19.0
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, f - 6.5f);
		mBaiduMap.animateMapStatus(u);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mapView == null)
				return;

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(location.getDirection()).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				LatLng ll = new LatLng(latitude, longitude);
				// 设置缩放比例,更新地图状态
				float f = mBaiduMap.getMaxZoomLevel();// 19.0
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,
						f - 2);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		mapView = null;
		super.onDestroy();
	}
	private void getverderList() {
		String getVenderList = getString(R.string.serverbaseurl) + getString(R.string.getVenderListurl) + "?mid=" + Constants.userId;
		StringRequest stringRequest = new StringRequest(Method.GET,
				getVenderList, new Listener<String>() {

					public void onResponse(String result) {
						if (result != null) {
							try {
								Gson gson = new Gson();
								venderBeans = gson.fromJson(result, new TypeToken<List<VenderBean>>() {}.getType());
								setMarker();
							} catch (Exception e) {
								Log.e("pulamsi", "售货机地图数据装配出错"); 
							}
						}
					}
				}, new Response.ErrorListener() {

					public void onErrorResponse(VolleyError arg0) {
					}
				});
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
		PulamsiApplication.requestQueue.add(stringRequest);
	}
	
	private void setMarker(){
		for (int i = 0; i < venderBeans.size(); i++) {
			VenderBean venderBean = venderBeans.get(i);
			LatLng point = new LatLng(Double.parseDouble(venderBean.getWeidu()),Double.parseDouble(venderBean.getJindu()));
			// 构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option = new MarkerOptions().position(point).icon(
					mCurrentMarker).zIndex(i);
			// 在地图上添加Marker，并显示
			mBaiduMap.addOverlay(option);
		}
	}
}
