package com.pulamsi.utils.update;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-06-13
 * Time: 16:47
 * FIXME
 */
public interface IUpdateCallBack {

    public void noUpdate();//没有新版本

    public void errorUpdate();//更新失败

    public void nowUpdate();//正在更新
}
