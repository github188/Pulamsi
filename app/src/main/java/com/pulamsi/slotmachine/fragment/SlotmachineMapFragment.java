package com.pulamsi.slotmachine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
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
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.myinfo.slotmachineManage.entity.VenderBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 售货机地图界面
 */
public class SlotmachineMapFragment extends Fragment implements OnGetGeoCoderResultListener {


    // 定位相关
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;// 定位模式
    private boolean isFirstLoc = true;// 是否首次定位
    private List<VenderBean> venderBeans;
    private BitmapDescriptor mCurrentMarker;// Marker图标

    private BaiduMap mBaiduMap;

    private MapView mapView;

    private GeoCoder geoCoder;

    private List<Marker> markers = new ArrayList<>();
    /**
     * 售货机信息控件
     */
    private LinearLayout slotMachintInfo;

    TextView slotMachintName;//售货机名称
    TextView slotMachintConnectionStatus;//售货机连接状态
    TextView slotMachintAddress;//售货机地址



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SDKInitializer.initialize(PulamsiApplication.context);
        View view = View.inflate(getActivity(), R.layout.fragment_slotmachint_baidumap, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        slotMachintInfo = (LinearLayout) view.findViewById(R.id.ll_slotMachint_info);

        slotMachintName = (TextView) view.findViewById(R.id.tv_name);
        slotMachintConnectionStatus = (TextView) view.findViewById(R.id.tv_connectionStatus);
        slotMachintAddress = (TextView) view.findViewById(R.id.tv_address);

        mapView = (MapView) view.findViewById(R.id.mv_MapView);
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        // 地图初始化
        mBaiduMap = mapView.getMap();

        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);

        getverderList("", "", "");

        mapView.setClickable(true);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;// 设置定位模式为普通
        mCurrentMarker = BitmapDescriptorFactory// 构建mark图标
                .fromResource(R.drawable.icon_marka);
        //设置缩放级别大小
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(25).build()));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, null));
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
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
            public boolean onMarkerClick(final Marker marker) {
                /*ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.drawable.share_image);
                InfoWindow infowindow_park = new InfoWindow(imageView,
                        marker.getPosition(), -55);
                mBaiduMap.showInfoWindow(infowindow_park);*/
                VenderBean venderBean = venderBeans.get(marker.getZIndex());
                showSlotMachintInfo();//显示售货机
                setSlotMachintInfo(venderBean);//设置详细信息
                moveCenter(marker);//移动中心点
                return false;
            }
        });

    }

    /**
     * 设置详细信息
     * @param venderBean
     */
    private void setSlotMachintInfo(VenderBean venderBean) {
        if (venderBean.isOnline()) {
            slotMachintConnectionStatus.setText("连接状态: 在线");
        } else {
            slotMachintConnectionStatus.setText("连接状态: 离线");
        }
        slotMachintName.setText("售货机名称: " + venderBean.getTerminalName());
        slotMachintAddress.setText("地址: " + venderBean.getTerminalAddress());
    }

    /**
     * 显示售货机信息
     */
    private void showSlotMachintInfo() {
        if (slotMachintInfo.getVisibility() == View.GONE) {
            slotMachintInfo.setVisibility(View.VISIBLE);
            Animation showViewAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.app_down2up_in);
            slotMachintInfo.startAnimation(showViewAnimation);
        }
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


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        //地址解析成经纬度返回
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        LatLng ll = geoCodeResult.getLocation();
        // 设置缩放比例,更新地图状态
        float f = mBaiduMap.getMaxZoomLevel();// 19.0
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,
                f - 8);
        mBaiduMap.animateMapStatus(u);
//        String strInfo = String.format("纬度：%f 经度：%f",
//                geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude);
//        Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        //经纬度解析成地址返回
    }

    /**
     * 进行地址解析成经纬度并把地图位置相对于的移动
     *
     * @param city
     * @param address
     */
    public void geocode(String city, String address) {
        geoCoder.geocode(new GeoCodeOption().city(city).address(address));
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
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, f - 6.5f);
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


    public void getverderList(String provinceId, String cityId, String districtId) {
        String getVenderList = getString(R.string.serverbaseurl) + getString(R.string.getIndexVenderList) + "?provinceId=" + provinceId + "&cityId=" + cityId + "&districtId=" + districtId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                getVenderList, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        venderBeans = gson.fromJson(result, new TypeToken<List<VenderBean>>() {
                        }.getType());
                        cleanMarker();
                        setMarker();
                        hideSlotMachintInfo();
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

    /**
     * 隐藏售货机信息
     */
    public void hideSlotMachintInfo() {
        if (slotMachintInfo.getVisibility() == View.VISIBLE)
            slotMachintInfo.setVisibility(View.GONE);
    }

    private void cleanMarker() {
        for (int i = 0; i < markers.size(); i++) {
            Marker marker = markers.get(i);
            marker.remove();
        }
        markers.clear();
    }

    private void setMarker() {
        for (int i = 0; i < venderBeans.size(); i++) {
            VenderBean venderBean = venderBeans.get(i);
            LatLng point = new LatLng(Double.parseDouble(venderBean.getWeidu()), Double.parseDouble(venderBean.getJindu()));
            // 构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(point).icon(
                    mCurrentMarker).zIndex(i);
            // 在地图上添加Marker，并显示
            Marker marker = (Marker) mBaiduMap.addOverlay(option);
            markers.add(marker);
        }
    }

}
