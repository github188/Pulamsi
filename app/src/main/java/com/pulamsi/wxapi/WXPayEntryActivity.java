package com.pulamsi.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.myinfo.order.MyOrderActivity;
import com.pulamsi.myinfo.order.MyOrderFragment;
import com.pulamsi.pay.wx.simcpux.Constants;
import com.pulamsi.views.dialog.CommonAlertDialog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}
	@Override
	public void onResp(BaseResp resp) {
		Log.d("pulamsi", "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			if (resp.errCode == 0) {
//				HaiLog.d("pulamsi","提示成功");
//				ToastUtil.toastShow(com.pulamsi.constant.Constants.choicePayTypeActivity, "支付成功");
//			} else if (resp.errCode == -2) {
//				HaiLog.d("pulamsi","提示取消");
//				ToastUtil.toastShow(com.pulamsi.constant.Constants.choicePayTypeActivity, "支付取消");
//			} else {
//				HaiLog.d("pulamsi","提示失败");
//				ToastUtil.toastShow(com.pulamsi.constant.Constants.choicePayTypeActivity, "支付失败");
//			}
			String message;
			switch (resp.errCode){
				case 0:
					message = "支付成功";
					break;
				case 2:
					message = "支付取消";
					break;
				default:
					message = "支付失败";
			}
			CommonAlertDialog alertDialog = new CommonAlertDialog(com.pulamsi.constant.Constants.choicePayTypeActivity,message, "确定", "取消", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			}, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent all = new Intent(com.pulamsi.constant.Constants.choicePayTypeActivity, MyOrderActivity.class);
					all.putExtra(MyOrderActivity.KEY_OPEN_PAGE, MyOrderFragment.TYPE_ALL);
					com.pulamsi.constant.Constants.choicePayTypeActivity.startActivity(all);
					BaseAppManager.getInstance().clear();
				}
			});
			alertDialog.show();
		}

	  finish();
	}
}