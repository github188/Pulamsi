package com.pulamsi.pay.wx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.pulamsi.pay.wx.simcpux.Constants;
import com.pulamsi.pay.wx.simcpux.MD5;
import com.pulamsi.pay.wx.simcpux.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("ALL")
public class WxPay {

  private static WxPay wxPay;
  private static final String TAG = "MicroMsg.SDKSample.WxPay";
  private Activity context;
  private PayReq req;
  private IWXAPI msgApi;
  private String productNameName;
  public static  String totalPrice;
  public static  String orderNum;
  public static String orderId;
  Map<String, String> resultunifiedorder;
  StringBuffer sb;

  public void destroy(){
    wxPay = null;
  }

  public static WxPay getInstance(){
    if(wxPay == null){
      wxPay = new WxPay();
    }
    return wxPay;
  }

  public void initWxPay(Activity context) {
    this.context = context;
    msgApi = WXAPIFactory.createWXAPI(context, null);// *获取微信对象
    req = new PayReq();
    sb = new StringBuffer();
    msgApi.registerApp(Constants.APP_ID);// *注册app
  }

  public void wxPay(String totalPrice,String orderNum,String orderId,String productNameName) {
    this.orderNum = orderNum;
    this.totalPrice = totalPrice;
    this.orderId = orderId;
    this.productNameName = productNameName;
    GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
    getPrepayId.execute();
  }

  /**
   * 生成签名
   */

  private String genPackageSign(List<NameValuePair> params) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < params.size(); i++) {
      sb.append(params.get(i).getName());
      sb.append('=');
      sb.append(params.get(i).getValue());
      sb.append('&');
    }
    sb.append("key=");
    sb.append(Constants.API_KEY);

    String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
    Log.e("orion", packageSign);
    return packageSign;
  }

  private String genAppSign(List<NameValuePair> params) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < params.size(); i++) {
      sb.append(params.get(i).getName());
      sb.append('=');
      sb.append(params.get(i).getValue());
      sb.append('&');
    }
    sb.append("key=");
    sb.append(Constants.API_KEY);

    this.sb.append("sign str\n" + sb.toString() + "\n\n");
    String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
    Log.e("orion", appSign);
    return appSign;
  }

  private String toXml(List<NameValuePair> params) {
    StringBuilder sb = new StringBuilder();
    sb.append("<xml>");
    for (int i = 0; i < params.size(); i++) {
      sb.append("<" + params.get(i).getName() + ">");

      sb.append(params.get(i).getValue());
      sb.append("</" + params.get(i).getName() + ">");
    }
    sb.append("</xml>");

    Log.e("orion", sb.toString());
    return sb.toString();
  }

  private ProgressDialog dialog;

  private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

    @Override
    protected void onPreExecute() {
      dialog = ProgressDialog.show(context, "提示", "正在获取预支付订单...");
      dialog.setCancelable(false);
    }

    @Override
    protected void onPostExecute(Map<String, String> result) {
      // if (dialog != null) {
      // dialog.dismiss();
      // }
      sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
//      show.setText(sb.toString());

      resultunifiedorder = result;

      genPayReq();
      sendPayReq();
    }

    @Override
    protected void onCancelled() {
      super.onCancelled();
    }

    @Override
    protected Map<String, String> doInBackground(Void... params) {

      String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
      String entity = genProductArgs();

      Log.e("orion", entity);

      byte[] buf = Util.httpPost(url, entity);

      String content = new String(buf);
      Log.e("orion", content);
      Map<String, String> xml = decodeXml(content);

      return xml;
    }
  }


  public void dismiss() {
    if(dialog != null)
    dialog.dismiss();
  }


  public Map<String, String> decodeXml(String content) {

    try {
      Map<String, String> xml = new HashMap<String, String>();
      XmlPullParser parser = Xml.newPullParser();
      parser.setInput(new StringReader(content));
      int event = parser.getEventType();
      while (event != XmlPullParser.END_DOCUMENT) {

        String nodeName = parser.getName();
        switch (event) {
          case XmlPullParser.START_DOCUMENT:

            break;
          case XmlPullParser.START_TAG:

            if ("xml".equals(nodeName) == false) {
              // 实例化student对象
              xml.put(nodeName, parser.nextText());
            }
            break;
          case XmlPullParser.END_TAG:
            break;
        }
        event = parser.next();
      }

      return xml;
    } catch (Exception e) {
      Log.e("orion", e.toString());
    }
    return null;

  }

  private String genNonceStr() {
    Random random = new Random();
    return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
  }

  private long genTimeStamp() {
    return System.currentTimeMillis() / 1000;
  }

  private String genOutTradNo() {
    Random random = new Random();
    return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
  }

  //
  private String genProductArgs() {
    StringBuffer xml = new StringBuffer();

    try {
      String nonceStr = genNonceStr();

      xml.append("</xml>");
      List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
      packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
      packageParams.add(new BasicNameValuePair("body", "weixin"));// 商品或支付单简要描述
      packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));// 商户号
      packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));// 随机字符串32位以内

      packageParams.add(new BasicNameValuePair("notify_url", Constants.BACK_URL));// 回调地址

      packageParams.add(new BasicNameValuePair("out_trade_no", orderId));// 内部订单号32字符以内 可含字母
      packageParams.add(new BasicNameValuePair("spbill_create_ip", judgeNetType(context)));// 机器ip
      packageParams.add(new BasicNameValuePair("total_fee", ((int)(Float.valueOf(totalPrice)*100))+""));// 价格 单位分
      packageParams.add(new BasicNameValuePair("trade_type", "APP"));// 交易类型

      String sign = genPackageSign(packageParams);
      packageParams.add(new BasicNameValuePair("sign", sign));// 签名

      String xmlstring = toXml(packageParams);

      return xmlstring;

    } catch (Exception e) {
      Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
      return null;
    }

  }

  private void genPayReq() {

    req.appId = Constants.APP_ID;
    req.partnerId = Constants.MCH_ID;
    req.prepayId = resultunifiedorder.get("prepay_id");
    req.packageValue = "Sign=WXPay";
    req.nonceStr = genNonceStr();
    req.timeStamp = String.valueOf(genTimeStamp());

    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
    signParams.add(new BasicNameValuePair("appid", req.appId));
    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
    signParams.add(new BasicNameValuePair("package", req.packageValue));
    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

    req.sign = genAppSign(signParams);

    sb.append("sign\n" + req.sign + "\n\n");

//    show.setText(sb.toString());

    Log.e("orion", signParams.toString());

  }

  private void sendPayReq() {
    msgApi.registerApp(Constants.APP_ID);
    msgApi.sendReq(req);
    dismiss();
  }

  // 获取本机WIFI
  private String getLocalIpAddress() {
    WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    // 获取32位整型IP地址
    int ipAddress = wifiInfo.getIpAddress();
    // 返回整型地址转换成“*.*.*.*”地址
    return String
        .format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
  }

  // 3G网络IP
  public static String getIpAddress() {
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
        NetworkInterface intf = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
          InetAddress inetAddress = enumIpAddr.nextElement();
          if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
            // if (!inetAddress.isLoopbackAddress() && inetAddress
            // instanceof Inet6Address) {
            return inetAddress.getHostAddress().toString();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  // 判断网络类型
  private String judgeNetType(Context context) {
    // -1 ：没网络 0：wifi 1：手机网络
    String ipAddress = null;
    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
    if (networkInfo != null && networkInfo.isConnected()) {
      String type = networkInfo.getTypeName();
      if (type.equalsIgnoreCase("WIFI")) {
        ipAddress = getLocalIpAddress();
      } else if (type.equalsIgnoreCase("MOBILE")) {
        ipAddress = getIpAddress();
      }
    }
    if (ipAddress == null || ipAddress.length() < 6) {
      ipAddress = "127.0.0.1";
    }
    return ipAddress;
  }

}
