package com.pulamsi.security.view;

import android.content.Context;

import tr.xip.errorview.ErrorView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-04-22
 * Time: 17:49
 * FIXME
 */
public interface ISecurityView {

    void showDialogMessage(int type,String title,String message);

    String getCode();

    String getPhone();

    String getSecretCode();

    Context getContext();

    void showLoading();

    void showError(ErrorView.RetryListener onClickListener);

    void showEmpty();

    void showComplete();

    void setqueryCount(String number);

    void setfirstQueryDate(String date);

    void integralCommit();

    void showSuccess();

    void showFailure();


}
