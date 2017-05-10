package com.pulamsi.utils.update;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.pulamsi.R;
import com.pulamsi.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;


/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-06-13
 * Time: 14:11
 * FIXME
 */
public class DownloadService extends IntentService {

    public static boolean isDownload = false;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int oldprogress = 0;

    public DownloadService() {
        super("DownloadService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setTicker(getString(R.string.start_download));
        mBuilder.setOngoing(true);//设置他为一个正在进行的通知。
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        String appName = getString(getApplicationInfo().labelRes);
        int icon = getApplicationInfo().icon;
        mBuilder.setContentTitle(getString(R.string.is_downloading)).setSmallIcon(icon);
        String apkurl = intent.getStringExtra("url");
        final String apkName = apkurl.substring(apkurl.lastIndexOf("/") + 1, apkurl.length());
        final String path = StorageUtils.getCacheDirectory(this).getPath();
        //使用Okhttp下载文件，抛弃之前的httpUrlConnetction，提升效率
        OkHttpUtils.get().url(apkurl).build().execute(new FileCallBack(path, apkName) {
            @Override
            public void onError(Call call, Exception e, int id) {
            ToastUtil.showToast(R.string.notice_networkerror);
            }

            @Override
            public void onResponse(File response, int id) {
                mBuilder.setContentTitle(getString(R.string.download_success));
                mNotifyManager.notify(0, mBuilder.build());
                mNotifyManager.cancel(0);
                isDownload = false;
                //直接清除通知栏状态
                Intent installAPKIntent = new Intent(Intent.ACTION_VIEW);
                installAPKIntent.setDataAndType(Uri.fromFile(new File(path, apkName)), "application/vnd.android.package-archive");
                installAPKIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(installAPKIntent);
                //弹出安装界面
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                isDownload = true;
                int p = (int) (100 * progress);
                if (p == oldprogress) {
                    //当进度一样时不更新通知栏，避免过度操作卡顿
                } else {
                    updateProgress(p);
                }
                oldprogress = p;
            }
        });
    }

    private void updateProgress(int progress) {
        mBuilder.setContentText(this.getString(R.string.download_progress, progress)).setProgress(100, progress, false);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingintent);
        Notification notify = mBuilder.build();
        mNotifyManager.notify(0, notify);
    }

}
