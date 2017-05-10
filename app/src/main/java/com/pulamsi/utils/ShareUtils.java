package com.pulamsi.utils;

import android.app.Activity;
import android.content.Context;

import com.pulamsi.R;
import com.pulamsi.angel.bean.AngelProductBean;
import com.pulamsi.myinfo.order.entity.Product;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


/**
 * Created by lanqiang on 15/12/1.
 */
public class ShareUtils {

    /**
     * 分享售货机工具类
     * @param context
     * @param yqcode
     */
    public static void showSlotmachineShare(Context context,String yqcode){
        // 首先在您的Activity中添加如下成员变量
        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        // 设置分享内容
        mController.setShareContent(context.getString(R.string.share_content));
        // 设置分享图片, 参数2为图片的url地址
//					mController.setShareMedia(new UMImage(GainJFActivity.this, "http://www.pulamsi.com/resouce/shop/images/pls_logo.jpg"));
        // 设置分享图片，参数2为本地图片的资源引用
        mController.setShareMedia(new UMImage(context,
                R.drawable.share_image));

        mController.setAppWebSite(SHARE_MEDIA.RENREN, context.getString(R.string.serverbaseurl) +context.getString(R.string.share_targetUrl )+ "?yqCode=" + yqcode);
        // 设置分享图片，参数2为本地图片的路径(绝对路径)
        // mController.setShareMedia(new UMImage(getActivity(),
        // BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

        String appID = "wx36144aed40383a83";
        String appSecret = "e8125a0b2edca16a5321bb45763e7fcb";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context,appID,appSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context,appID,appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) context, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();

        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);

        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字
        weixinContent.setShareContent(context.getString(R.string.share_content));
        //设置title
        weixinContent.setTitle(context.getString(R.string.share_title));
        //设置分享内容跳转URL
        weixinContent.setTargetUrl(context.getString(R.string.serverbaseurl) +context.getString(R.string.share_targetUrl )+ "?yqCode=" + yqcode);
        //设置分享图片
        weixinContent.setShareImage(new UMImage(context,
                R.drawable.share_image));
        mController.setShareMedia(weixinContent);


        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(context.getString(R.string.share_content));
        //设置朋友圈title
        circleMedia.setTitle(context.getString(R.string.share_title));
        circleMedia.setShareImage(new UMImage(context,
                R.drawable.share_image));
        circleMedia.setTargetUrl(context.getString(R.string.serverbaseurl) +context.getString(R.string.share_targetUrl )+ "?yqCode=" + yqcode);
        mController.setShareMedia(circleMedia);

        QQShareContent qqShareContent = new QQShareContent();
        //设置分享文字
        qqShareContent.setShareContent(context.getString(R.string.share_content));
        //设置分享title
        qqShareContent.setTitle(context.getString(R.string.share_title));
        //设置分享图片
        qqShareContent.setShareImage(new UMImage(context,
                R.drawable.share_image));
        //设置点击分享内容的跳转链接
        qqShareContent.setTargetUrl(context.getString(R.string.serverbaseurl) +context.getString(R.string.share_targetUrl )+ "?yqCode=" + yqcode);
        mController.setShareMedia(qqShareContent);

        QZoneShareContent qzone = new QZoneShareContent();
        //设置分享文字
        qzone.setShareContent(context.getString(R.string.share_content));
        //设置点击消息的跳转URL
        qzone.setTargetUrl(context.getString(R.string.serverbaseurl) +context.getString(R.string.share_targetUrl )+ "?yqCode=" + yqcode);
        //设置分享内容的标题
        qzone.setTitle(context.getString(R.string.share_title));
        //设置分享图片
        qzone.setShareImage(new UMImage(context,
                R.drawable.share_image));
        mController.setShareMedia(qzone);
        // 是否只有已登录用户才能打开分享选择页
        mController.openShare((Activity) context, false);
    }


    /**
     * 分享商品工具类
     */
    public static void showProductShare(Context context,AngelProductBean angelProductBean){

        // 首先在您的Activity中添加如下成员变量
        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        // 设置分享内容
        mController.setShareContent(context.getString(R.string.share_content));
        // 设置分享图片, 参数2为图片的url地址
//					mController.setShareMedia(new UMImage(GainJFActivity.this, "http://www.pulamsi.com/resouce/shop/images/pls_logo.jpg"));
        // 设置分享图片，参数2为本地图片的资源引用
        mController.setShareMedia(new UMImage(context, context.getString(R.string.serverbaseurl) +angelProductBean.getPic()));

        mController.setAppWebSite(SHARE_MEDIA.RENREN, context.getString(R.string.serverbaseurl) +context.getString(R.string.share_productUrl )+ "?id=" + angelProductBean.getId());
        // 设置分享图片，参数2为本地图片的路径(绝对路径)
        // mController.setShareMedia(new UMImage(getActivity(),
        // BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

        String appID = "wx36144aed40383a83";
        String appSecret = "e8125a0b2edca16a5321bb45763e7fcb";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context,appID,appSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context,appID,appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) context, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();

        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);

        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字
        weixinContent.setShareContent(context.getString(R.string.share_content));
        //设置title
        weixinContent.setTitle("普兰氏" + angelProductBean.getName());
        //设置分享内容跳转URL
        weixinContent.setTargetUrl(context.getString(R.string.serverbaseurl) + context.getString(R.string.share_productUrl) + "?id=" + angelProductBean.getId());
        //设置分享图片
        weixinContent.setShareMedia(new UMImage(context, context.getString(R.string.serverbaseurl) + angelProductBean.getPic()));
        mController.setShareMedia(weixinContent);


        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(context.getString(R.string.share_content));
        //设置朋友圈title
        circleMedia.setTitle("普兰氏"+angelProductBean.getName());
        circleMedia.setShareMedia(new UMImage(context, context.getString(R.string.serverbaseurl) + angelProductBean.getPic()));
        circleMedia.setTargetUrl(context.getString(R.string.serverbaseurl) +context.getString(R.string.share_productUrl )+ "?id=" + angelProductBean.getId());
        mController.setShareMedia(circleMedia);

        QQShareContent qqShareContent = new QQShareContent();
        //设置分享文字
        qqShareContent.setShareContent(context.getString(R.string.share_content));
        //设置分享title
        qqShareContent.setTitle("普兰氏"+angelProductBean.getName());
        //设置分享图片
        qqShareContent.setShareMedia(new UMImage(context, context.getString(R.string.serverbaseurl) + angelProductBean.getPic()));
        //设置点击分享内容的跳转链接
        qqShareContent.setTargetUrl(context.getString(R.string.serverbaseurl) +context.getString(R.string.share_productUrl )+ "?id=" + angelProductBean.getId());
        mController.setShareMedia(qqShareContent);

        QZoneShareContent qzone = new QZoneShareContent();
        //设置分享文字
        qzone.setShareContent(context.getString(R.string.share_content));
        //设置点击消息的跳转URL
        qzone.setTargetUrl(context.getString(R.string.serverbaseurl) +context.getString(R.string.share_productUrl )+ "?id=" + angelProductBean.getId());
        //设置分享内容的标题
        qzone.setTitle("普兰氏"+angelProductBean.getName());
        //设置分享图片
        qzone.setShareMedia(new UMImage(context, context.getString(R.string.serverbaseurl) + angelProductBean.getPic()));
        mController.setShareMedia(qzone);
        // 是否只有已登录用户才能打开分享选择页
        mController.openShare((Activity) context, false);


    }

    /**
     * 分享商品工具类
     */
    public static void showProductShare(Context context,Product product){

        // 首先在您的Activity中添加如下成员变量
        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        // 设置分享内容
        mController.setShareContent(context.getString(R.string.share_content));
        // 设置分享图片, 参数2为图片的url地址
//					mController.setShareMedia(new UMImage(GainJFActivity.this, "http://www.pulamsi.com/resouce/shop/images/pls_logo.jpg"));
        // 设置分享图片，参数2为本地图片的资源引用
        mController.setShareMedia(new UMImage(context, context.getString(R.string.serverbaseurl) +product.getPic()));

        mController.setAppWebSite(SHARE_MEDIA.RENREN, context.getString(R.string.serverbaseurl) +context.getString(R.string.share_productUrl )+ "?id=" + product.getId());
        // 设置分享图片，参数2为本地图片的路径(绝对路径)
        // mController.setShareMedia(new UMImage(getActivity(),
        // BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

        String appID = "wx36144aed40383a83";
        String appSecret = "e8125a0b2edca16a5321bb45763e7fcb";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context,appID,appSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context,appID,appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) context, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();

        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);

        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字
        weixinContent.setShareContent(context.getString(R.string.share_content));
        //设置title
        weixinContent.setTitle("普兰氏" + product.getName());
        //设置分享内容跳转URL
        weixinContent.setTargetUrl(context.getString(R.string.serverbaseurl) + context.getString(R.string.share_productUrl) + "?id=" + product.getId());
        //设置分享图片
        weixinContent.setShareMedia(new UMImage(context, context.getString(R.string.serverbaseurl) + product.getPic()));
        mController.setShareMedia(weixinContent);


        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(context.getString(R.string.share_content));
        //设置朋友圈title
        circleMedia.setTitle("普兰氏"+product.getName());
        circleMedia.setShareMedia(new UMImage(context, context.getString(R.string.serverbaseurl) + product.getPic()));
        circleMedia.setTargetUrl(context.getString(R.string.serverbaseurl) +context.getString(R.string.share_productUrl )+ "?id=" + product.getId());
        mController.setShareMedia(circleMedia);

        QQShareContent qqShareContent = new QQShareContent();
        //设置分享文字
        qqShareContent.setShareContent(context.getString(R.string.share_content));
        //设置分享title
        qqShareContent.setTitle("普兰氏"+product.getName());
        //设置分享图片
        qqShareContent.setShareMedia(new UMImage(context, context.getString(R.string.serverbaseurl) + product.getPic()));
        //设置点击分享内容的跳转链接
        qqShareContent.setTargetUrl(context.getString(R.string.serverbaseurl) +context.getString(R.string.share_productUrl )+ "?id=" + product.getId());
        mController.setShareMedia(qqShareContent);

        QZoneShareContent qzone = new QZoneShareContent();
        //设置分享文字
        qzone.setShareContent(context.getString(R.string.share_content));
        //设置点击消息的跳转URL
        qzone.setTargetUrl(context.getString(R.string.serverbaseurl) +context.getString(R.string.share_productUrl )+ "?id=" + product.getId());
        //设置分享内容的标题
        qzone.setTitle("普兰氏"+product.getName());
        //设置分享图片
        qzone.setShareMedia(new UMImage(context, context.getString(R.string.serverbaseurl) + product.getPic()));
        mController.setShareMedia(qzone);
        // 是否只有已登录用户才能打开分享选择页
        mController.openShare((Activity) context, false);


    }

}
