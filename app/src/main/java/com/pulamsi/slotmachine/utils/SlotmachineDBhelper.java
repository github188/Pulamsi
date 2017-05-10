package com.pulamsi.slotmachine.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pulamsi.slotmachine.entity.Area;
import com.pulamsi.start.init.utils.DBManager;

import java.util.ArrayList;

/**
 * Created by lanqiang on 15/11/26.
 */
public class SlotmachineDBhelper {
    private SQLiteDatabase db;
    private Context context;
    private DBManager dbm;

    public SlotmachineDBhelper(Context context) {
        super();
        this.context = context;
        dbm = new DBManager(context);
    }

    public ArrayList<Area> getCity(String provinceId) {
        dbm.openDatabase();
        db = dbm.getDatabase();
        ArrayList<Area> list = new ArrayList<Area>();
        Cursor cursor = null;

        try {
            String sql = "select * from city where provinceId='"+provinceId+"'";
            cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            while (!cursor.isLast()){
                String id =cursor.getString(cursor.getColumnIndex("id"));
                String name =cursor.getString(cursor.getColumnIndex("name"));
                Area area=new Area();
                area.setName(name);
                area.setId(id);
                area.setPid(provinceId);
                list.add(area);
                cursor.moveToNext();
            }
            String id =cursor.getString(cursor.getColumnIndex("id"));
            String name =cursor.getString(cursor.getColumnIndex("name"));
            Area area=new Area();
            area.setName(name);
            area.setId(id);
            area.setPid(provinceId);
            list.add(area);

        } catch (Exception e) {
            return null;
        }
        dbm.closeDatabase();
        db.close();
        cursor.close();//加上这句，否则会内存溢出

        return list;

    }
    public ArrayList<Area> getProvince() {
        dbm.openDatabase();
        db = dbm.getDatabase();
        ArrayList<Area> list = new ArrayList<Area>();
        Cursor cursor = null;

        try {
            String sql = "select * from province";
            cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            while (!cursor.isLast()){
                String id =cursor.getString(cursor.getColumnIndex("id"));
                String name =cursor.getString(cursor.getColumnIndex("name"));
                Area area=new Area();
                area.setName(name);
                area.setId(id);
                list.add(area);
                cursor.moveToNext();
            }
            String id =cursor.getString(cursor.getColumnIndex("id"));
            String name =cursor.getString(cursor.getColumnIndex("name"));
            Area area=new Area();
            area.setName(name);
            area.setId(id);
            list.add(area);

        } catch (Exception e) {
            return null;
        }
        dbm.closeDatabase();
        db.close();
        cursor.close();//加上这句，否则会内存溢出

        return list;

    }
    public ArrayList<Area> getDistrict(String cityId) {
        dbm.openDatabase();
        db = dbm.getDatabase();
        ArrayList<Area> list = new ArrayList<Area>();
        Cursor cursor = null;
        try {
            String sql = "select * from district where cityId='"+cityId+"'";
            cursor = db.rawQuery(sql,null);
            if (cursor.moveToFirst()) {
                while (!cursor.isLast()) {
                    String id =cursor.getString(cursor.getColumnIndex("id"));
                    String name =cursor.getString(cursor.getColumnIndex("name"));
                    Area area=new Area();
                    area.setName(name);
                    area.setId(id);
                    area.setPid(cityId);
                    list.add(area);
                    cursor.moveToNext();
                }
                String id =cursor.getString(cursor.getColumnIndex("id"));
                String name =cursor.getString(cursor.getColumnIndex("name"));
                Area area=new Area();
                area.setName(name);
                area.setId(id);
                area.setPid(cityId);
                list.add(area);
            }

        } catch (Exception e) {
            Log.i("wer", e.toString());
        }
        dbm.closeDatabase();
        db.close();
        cursor.close();//加上这句，否则会内存溢出

        return list;
    }


    public String getDistrictId(String District){
        dbm.openDatabase();
        db = dbm.getDatabase();
        Cursor cursor = db.rawQuery("select * from district where name=?",new String[]{District});
        if(cursor.moveToFirst()) {
            String result = cursor.getString(cursor.getColumnIndex("id"));
            dbm.closeDatabase();
            db.close();
            cursor.close();
            return result;
        }
        return null;
    }

    public String getCityId(String city){
        dbm.openDatabase();
        db = dbm.getDatabase();
        Cursor cursor = db.rawQuery("select * from city where name=?",new String[]{city});
        if(cursor.moveToFirst()) {
            String result = cursor.getString(cursor.getColumnIndex("id"));
            dbm.closeDatabase();
            db.close();
            cursor.close();
            return result;
        }
        return null;
    }

    public String getProvinceId(String province){
        dbm.openDatabase();
        db = dbm.getDatabase();
        Cursor cursor = db.rawQuery("select * from province where name=?",new String[]{province});
        if(cursor.moveToFirst()) {
            String result = cursor.getString(cursor.getColumnIndex("id"));
            dbm.closeDatabase();
            db.close();
            cursor.close();
            return result;
        }
        return null;
    }
}
