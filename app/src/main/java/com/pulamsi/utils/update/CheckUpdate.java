package com.pulamsi.utils.update;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.utils.PulamsiHelper;
import com.pulamsi.utils.update.bean.Apkinfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-06-13
 * Time: 14:11
 * FIXME
 */
public class CheckUpdate {
    //更新状态回调
    private IUpdateCallBack iUpdateCallBack;

    //单例化检查更新类
    private CheckUpdate() {
    }

    private Context mcontext;
    private static CheckUpdate checkUpdate = null;


    public static CheckUpdate getInstance() {
        if (checkUpdate == null) {
            checkUpdate = new CheckUpdate();
        }
        return checkUpdate;
    }


    public void setOnIUpdateCallBack(IUpdateCallBack iUpdateCallBack) {
        this.iUpdateCallBack = iUpdateCallBack;
    }


    /**
     * 校验版本
     *
     * @param context
     */
    public void startCheck(final Context context) {
        mcontext = context;
        if (PulamsiApplication.updateIgnore) {
            return;
        }
        if (DownloadService.isDownload) {
            if (iUpdateCallBack != null)
                iUpdateCallBack.nowUpdate();
            return;
        }
        OkHttpUtils
                .post()
                .url(mcontext.getString(R.string.serverbaseurl) + "android/apkinfo/updateApkinfo.html")
                .build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                if (iUpdateCallBack != null)
                    iUpdateCallBack.errorUpdate();
            }

            @Override
            public void onResponse(String response, int id) {
                //使用gson解析json数据，注意写好bean
                if (response != null) {
                    try {
                        Gson gson = new Gson();
                        Apkinfo apkinfo = gson.fromJson(response, Apkinfo.class);
                        if (apkinfo != null && !TextUtils.isEmpty(apkinfo.getVersion()))
                            compareVersion(Integer.parseInt(apkinfo.getVersion()), apkinfo.getIntroduction(), mcontext.getString(R.string.serverbaseurl) + apkinfo.getUrl()); //与本地版本进行比较
                    } catch (Exception e) {
                        HaiLog.e("更新数据装填错误");
                    }
                }
            }
        });
    }

    /**
     * 比较版本大小
     *
     * @param newVersion 新版本
     * @param intro      内容
     * @param url        下载地址
     */
    private void compareVersion(int newVersion, String intro, final String url) {
        int versionCode = PulamsiHelper.getVersionCode(mcontext);

        if (newVersion > versionCode) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
            builder.setTitle(mcontext.getString(R.string.found_new_version));
            builder.setMessage(intro);
            builder.setPositiveButton(mcontext.getString(R.string.update_now), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(mcontext, DownloadService.class);
                    intent.putExtra("url", url);
                    mcontext.startService(intent);
                }
            });
            builder.setNegativeButton(mcontext.getString(R.string.later_update), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PulamsiApplication.updateIgnore = true;
                }
            });
            builder.setCancelable(false).show();
        } else {
            if (iUpdateCallBack != null)
                iUpdateCallBack.noUpdate();
            return;
        }
    }
}
