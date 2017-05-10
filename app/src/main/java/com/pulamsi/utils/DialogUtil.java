package com.pulamsi.utils;

import android.app.Activity;
import android.content.Context;

import com.pulamsi.views.dialog.LoadingDialog;

/**
 * @author lanqiang on 2015/11/2.
 */
public class DialogUtil {


    private static LoadingDialog mLoadingDialog;

    public static void showLoadingDialog(Context context, String content) {
        hideLoadingDialog();
        mLoadingDialog = new LoadingDialog((Activity) context, content);
        mLoadingDialog.show();
    }

    public static void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
