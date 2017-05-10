package com.pulamsi.start.init.utils;

import java.util.ArrayList;


import com.pulamsi.slotmachine.entity.Area;
import com.pulamsi.start.init.entity.City;
import com.pulamsi.start.init.entity.District;
import com.pulamsi.start.init.entity.Province;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBhelper {
	private SQLiteDatabase db;
	private Context context;
	private DBManager dbm;
	
	public DBhelper(Context context) {
		super();
		this.context = context;
		dbm = new DBManager(context);
	}



	public District getDistrictByID(String districtID) {
		dbm.openDatabase();
		db = dbm.getDatabase();
		District district= null;
		try {
			String sql = "select * from district where id='"+districtID+"'";
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			String id = cursor.getString(cursor.getColumnIndex("id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String postCode = cursor.getString(cursor.getColumnIndex("postCode"));
			district = new District();
			district.setId(id);
			district.setPostCode(postCode);
			district.setName(name);
		} catch (Exception e) {
			Log.i("wer", e.toString());
			return null;
		}
		dbm.closeDatabase();
		db.close();
		return district;

	}

	public City getCitybycityID(String cityId) {
		dbm.openDatabase();
		db = dbm.getDatabase();
		City city= null;
		try {
			String sql = "select * from city where id='"+cityId+"'";
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			String id =cursor.getString(cursor.getColumnIndex("id"));
			String name =cursor.getString(cursor.getColumnIndex("name"));
			String areaCode =cursor.getString(cursor.getColumnIndex("areaCode"));
			String orderid =cursor.getString(cursor.getColumnIndex("orderid"));
			city = new City();
			city.setName(name);
			city.setId(id);
			city.setAreaCode(areaCode);
			city.setOrderid(orderid);
		} catch (Exception e) {
			return null;
		}
		dbm.closeDatabase();
		db.close();
		return city;

	}

	public Province getProvinceByID(String provinceId) {
		dbm.openDatabase();
		db = dbm.getDatabase();
		Province  province = null;
		try {
			String sql = "select * from province where id='" +provinceId +"'";
			Cursor cursor = db.rawQuery(sql,null);
			cursor.moveToFirst();
			String id=cursor.getString(cursor.getColumnIndex("id"));
			String name=cursor.getString(cursor.getColumnIndex("name"));
			String orderid=cursor.getString(cursor.getColumnIndex("orderid"));
			province=new Province();
			province.setId(id);
			province.setName(name);
			province.setOrderid(orderid);
		} catch (Exception e) {
			return null;
		}
		dbm.closeDatabase();
		db.close();
		return province;

	}



	public ArrayList<City> getCity(String provinceId) {
		dbm.openDatabase();
		db = dbm.getDatabase();
		ArrayList<City> list = new ArrayList<City>();
		
	 	try {    
	        String sql = "select * from city where provinceId='"+provinceId+"'";
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
	        	String id =cursor.getString(cursor.getColumnIndex("id"));
	        	String name =cursor.getString(cursor.getColumnIndex("name"));
	        	String areaCode =cursor.getString(cursor.getColumnIndex("areaCode"));
	        	String orderid =cursor.getString(cursor.getColumnIndex("orderid"));
				City city=new City();
				city.setName(name);
				city.setId(id);
				city.setAreaCode(areaCode);
				city.setOrderid(orderid);
				city.setProvinceId(provinceId);
				list.add(city);
				cursor.moveToNext();
			}
			String id =cursor.getString(cursor.getColumnIndex("id"));
			String name =cursor.getString(cursor.getColumnIndex("name"));
			String areaCode =cursor.getString(cursor.getColumnIndex("areaCode"));
			String orderid =cursor.getString(cursor.getColumnIndex("orderid"));
			City city=new City();
			city.setName(name);
			city.setId(id);
			city.setAreaCode(areaCode);
			city.setOrderid(orderid);
			city.setProvinceId(provinceId);
			list.add(city);
	        
	    } catch (Exception e) {  
	    	return null;
	    } 
	 	dbm.closeDatabase();
	 	db.close();	

		return list;

	}
	public ArrayList<Province> getProvince() {
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	ArrayList<Province> list = new ArrayList<Province>();
		
	 	try {    
	        String sql = "select * from province";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
	        	String id=cursor.getString(cursor.getColumnIndex("id"));
	        	String name=cursor.getString(cursor.getColumnIndex("name"));
	        	String orderid=cursor.getString(cursor.getColumnIndex("orderid"));
				Province province=new Province();
				province.setId(id);
				province.setName(name);
				province.setOrderid(orderid);
		        list.add(province);
		        cursor.moveToNext();
	        }
			String id=cursor.getString(cursor.getColumnIndex("id"));
			String name=cursor.getString(cursor.getColumnIndex("name"));
			String orderid=cursor.getString(cursor.getColumnIndex("orderid"));
			Province province=new Province();
			province.setId(id);
			province.setName(name);
			province.setOrderid(orderid);
			list.add(province);
	        
	    } catch (Exception e) {  
	    	return null;
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
		return list;
		
	}
	public ArrayList<District> getDistrict(String cityId) {
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	ArrayList<District> list = new ArrayList<District>();
	 	try {    
	        String sql = "select * from district where cityId='"+cityId+"'";
	        Cursor cursor = db.rawQuery(sql,null);
	        if (cursor.moveToFirst()) {
				while (!cursor.isLast()) {
					String id = cursor.getString(cursor.getColumnIndex("id"));
					String name = cursor.getString(cursor.getColumnIndex("name"));
					String postCode = cursor.getString(cursor.getColumnIndex("postCode"));
					District district = new District();
					district.setId(id);
					district.setPostCode(postCode);
					district.setCityId(cityId);
					district.setName(name);
					list.add(district);
					cursor.moveToNext();
				}
				String id = cursor.getString(cursor.getColumnIndex("id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String postCode = cursor.getString(cursor.getColumnIndex("postCode"));
				District district = new District();
				district.setId(id);
				district.setPostCode(postCode);
				district.setCityId(cityId);
				district.setName(name);
				list.add(district);
			}
	        
	    } catch (Exception e) { 
	    	Log.i("wer", e.toString());
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
		return list;
		
	}


}
