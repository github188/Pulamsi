package com.pulamsi.myinfo.setting.view;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-05-19
 * Time: 09:24
 * FIXME
 */
public interface IChangePasswordView {

    void setChangePasswordPhone(String phone);

    void startCountDown();

    String getVerificationCode();

    String getPassWord();

}
