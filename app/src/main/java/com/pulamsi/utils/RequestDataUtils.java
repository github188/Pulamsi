package com.pulamsi.utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.setting.entity.Member;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-05-07
 * Time: 16:44
 * FIXME
 */
public class RequestDataUtils {
    public static void getinfo(Context context) {
        String showAccount = context.getString(R.string.serverbaseurl) + context.getString(R.string.showMember) + Constants.userId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, showAccount, new Response.Listener<String>() {
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        PulamsiApplication.dbUtils.deleteAll(Member.class);
                        Gson gson = new Gson();
                        Member member = gson.fromJson(result, Member.class);
                        PulamsiApplication.dbUtils.saveOrUpdate(member);//这里需要注意的是Member对象必须有id属性，或者有通过@ID注解的属性
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "帐户信息装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError arg0) {
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }
}
