package com.pulamsi.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import com.pulamsi.base.baseUtil.HaiLog;

/**
 * Created by lanqiang on 15/11/27.
 */
public class PulamsiHelper {

    /**
     * 获取 app 版本号
     *
     * @param context context
     * @return app 版本号
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0 ;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            HaiLog.e("pulamsi", "version code not found!");
        }
        return versionCode;
    }

    /**
     * 获取 app 版本号
     *
     * @param context context
     * @return app 版本号
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
             versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            HaiLog.e("pulamsi", "version name not found!");
        }
        return versionName;
    }
}
