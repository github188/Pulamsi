package com.pulamsi.pay.wx.simcpux;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;

public class Constants {

	//appid
	//请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
	public static final String APP_ID = "wx36144aed40383a83";

	//商户号
	public static final String MCH_ID = "1248084301";

	//  API密钥，在商户平台设置
	public static final  String API_KEY="412fde4e9c2e2bb619514ecompulamsi";
	
	public static final String BACK_URL = PulamsiApplication.context.getString(R.string.serverbaseurl)+PulamsiApplication.context.getString(R.string.wechat_notify);
}

