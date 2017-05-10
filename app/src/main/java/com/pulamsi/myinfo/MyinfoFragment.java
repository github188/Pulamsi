package com.pulamsi.myinfo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseFragment;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.integral.IntegralActivity;
import com.pulamsi.loginregister.LoginActivity;
import com.pulamsi.myinfo.AccreditQuery.AccreditQueryActivity;
import com.pulamsi.myinfo.address.AddressListActivity;
import com.pulamsi.myinfo.myteam.MyTeamActivity;
import com.pulamsi.myinfo.order.MyOrderActivity;
import com.pulamsi.myinfo.order.MyOrderFragment;
import com.pulamsi.myinfo.setting.SettingActivity;
import com.pulamsi.myinfo.setting.UserInfoActivity;
import com.pulamsi.myinfo.setting.entity.Member;
import com.pulamsi.myinfo.slotmachineManage.ManageActivity;
import com.pulamsi.myinfo.taobaostore.TaobaoStoreActivity;
import com.pulamsi.myinfo.wallet.WalletActivity;
import com.pulamsi.myinfo.wallet.entity.MemberAccount;
import com.pulamsi.start.init.entity.Role;
import com.pulamsi.start.init.entity.User;
import com.pulamsi.utils.ShareUtils;
import com.pulamsi.views.avatarView.AvatarImageView;
import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyinfoFragment extends BaseFragment implements OnClickListener {

    /**
     * 注册登录
     */
    private TextView loginorRegister;
    /**
     * 登录后显示的view
     */
    private LinearLayout loginBoard;
    /**
     * 记录用户对象
     */
    private User mUser;
    /**
     * 钱包对象
     */
    private MemberAccount memberAccount;
    /**
     * 售货机对象，需要做隐藏
     */
    private TextView slotmachine;
    /**
     * 库存对象，需要做隐藏
     */
    private TextView warehouse;

    /**
     * scrollview
     */
    private ScrollView scrollView;

    /**
     * 余额
     */
    private TextView balance;
    /**
     * 用户名
     */
    private TextView userName;
    /**
     * 头像
     */
    private AvatarImageView userIcon;
    /**
     * 待付款
     */
    private TextView paid;
    /**
     * 待发货
     */
    private TextView send;
    /**
     * 待收货
     */
    private TextView received;
    /**
     * 全部订单
     */
    private TextView returned;
    /**
     * 待付款角标数量
     */
    private BadgeView paidMark;
    /**
     * 待发货角标数量
     */
    private BadgeView sendMark;
    /**
     * 待收货角标数量
     */
    private BadgeView receivedMark;
    /**
     * 待评价角标数量
     */
    private BadgeView evaluationMark;
    /**
     * 查看全部订单
     */
    private LinearLayout allOrder;

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_my_info_login_or_register:
                //登录和注册按钮
                Intent loginandregister = new Intent(MyinfoFragment.this.getActivity(), LoginActivity.class);
                startActivity(loginandregister);
                break;
            case R.id.iv_my_info_avatar:
                //用户头像
                if (mUser.isHasLogin()) {
                    Intent userinfo = new Intent(MyinfoFragment.this.getActivity(), UserInfoActivity.class);
                    startActivity(userinfo);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.tv_my_info_my_footprint_btn:
                //我的团队
                if (mUser.isHasLogin()) {
                    Intent myteam = new Intent(MyinfoFragment.this.getActivity(), MyTeamActivity.class);
                    startActivity(myteam);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.tv_my_info_my_collection_btn:
                //余额
                if (mUser.isHasLogin()) {
                    Intent wallet = new Intent(MyinfoFragment.this.getActivity(), WalletActivity.class);
                    startActivity(wallet);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.ll_my_info_all_order:
                //查看全部
                if (mUser.isHasLogin()) {
                    Intent notReceive = new Intent(MyinfoFragment.this.getActivity(), MyOrderActivity.class);
                    notReceive.putExtra(MyOrderActivity.KEY_OPEN_PAGE, MyOrderFragment.TYPE_ALL);
                    startActivity(notReceive);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.tv_my_info_to_be_paid:
                //待付款
                if (mUser.isHasLogin()) {
                    Intent notPay = new Intent(MyinfoFragment.this.getActivity(), MyOrderActivity.class);
                    notPay.putExtra(MyOrderActivity.KEY_OPEN_PAGE, MyOrderFragment.TYPE_NOT_PAY);
                    startActivity(notPay);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.tv_my_info_to_be_send:
                //待发货
                if (mUser.isHasLogin()) {
                    Intent notShip = new Intent(MyinfoFragment.this.getActivity(), MyOrderActivity.class);
                    notShip.putExtra(MyOrderActivity.KEY_OPEN_PAGE, MyOrderFragment.TYPE_NOT_SHIP);
                    startActivity(notShip);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.tv_my_info_to_be_received:
                //待收货
                if (mUser.isHasLogin()) {
                    Intent notReceive = new Intent(MyinfoFragment.this.getActivity(), MyOrderActivity.class);
                    notReceive.putExtra(MyOrderActivity.KEY_OPEN_PAGE, MyOrderFragment.TYPE_NOT_RECEIVE);
                    startActivity(notReceive);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.tv_my_info_to_be_returned:
                //待评价
                if (mUser.isHasLogin()) {
                    Intent notReceive = new Intent(MyinfoFragment.this.getActivity(), MyOrderActivity.class);
                    notReceive.putExtra(MyOrderActivity.KEY_OPEN_PAGE, MyOrderFragment.TYPE_NOT_EVALUATION);
                    startActivity(notReceive);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.tv_my_info_my_bank:
                //我的钱包
                if (mUser.isHasLogin()) {
                    Intent wallet = new Intent(MyinfoFragment.this.getActivity(), WalletActivity.class);
                    startActivity(wallet);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.tv_my_info_my_jf:
                //我的积分
                if (mUser.isHasLogin()) {
                    Intent integral = new Intent(MyinfoFragment.this.getActivity(), IntegralActivity.class);
                    startActivity(integral);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.tv_my_info_my_address:
                //收货地址
                if (mUser.isHasLogin()) {
                    Intent address = new Intent(MyinfoFragment.this.getActivity(), AddressListActivity.class);
                    startActivity(address);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.tv_my_info_my_taobao:
                //淘宝商家
                Intent taobaostore = new Intent(MyinfoFragment.this.getActivity(), TaobaoStoreActivity.class);
                startActivity(taobaostore);
                break;
            case R.id.tv_my_info_my_accredit:
                //授权查询
                Intent aaccreditquery = new Intent(MyinfoFragment.this.getActivity(), AccreditQueryActivity.class);
                startActivity(aaccreditquery);
                break;
            case R.id.tv_my_info_my_setting:
                //设置
                Intent seting = new Intent(MyinfoFragment.this.getActivity(), SettingActivity.class);
                startActivity(seting);
                break;
            case R.id.tv_my_info_my_slotmachine:
                //智能售货机
                if (mUser.isHasLogin()) {
                    Intent slotmachinemanage = new Intent(MyinfoFragment.this.getActivity(), ManageActivity.class);
                    startActivity(slotmachinemanage);
                } else {
                    onClick(loginorRegister);
                }
                break;
            case R.id.tv_my_info_slotmachine_share:
                //售货机分享
                if (mUser.isHasLogin()) {
                    ShareUtils.showSlotmachineShare(getActivity(), mUser.getUserName());
                } else {
                    ShareUtils.showSlotmachineShare(getActivity(), "");
                }
            case R.id.tv_my_info_my_warehouse:
                //库存管理
                break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.myinfo_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
//        getBalance();修改这里不能进行获取余额，不确定拿到UserId没有
    }


    private void initView(View view) {
        loginorRegister = (TextView) view.findViewById(R.id.tv_my_info_login_or_register);
        loginorRegister.setOnClickListener(this);
        allOrder = (LinearLayout) view.findViewById(R.id.ll_my_info_all_order);
        allOrder.setOnClickListener(this);
        scrollView = (ScrollView) view.findViewById(R.id.myinfo_scrollview);
        userIcon = (AvatarImageView) view.findViewById(R.id.iv_my_info_avatar);
        userIcon.setOnClickListener(this);
        LinearLayout myteam = (LinearLayout) view.findViewById(R.id.tv_my_info_my_footprint_btn);
        myteam.setOnClickListener(this);
        LinearLayout balanceLayout = (LinearLayout) view.findViewById(R.id.tv_my_info_my_collection_btn);
        balanceLayout.setOnClickListener(this);
        balance = (TextView) view.findViewById(R.id.my_info_balance);
        paid = (TextView) view.findViewById(R.id.tv_my_info_to_be_paid);
        paid.setOnClickListener(this);
        send = (TextView) view.findViewById(R.id.tv_my_info_to_be_send);
        send.setOnClickListener(this);
        received = (TextView) view.findViewById(R.id.tv_my_info_to_be_received);
        received.setOnClickListener(this);
        returned = (TextView) view.findViewById(R.id.tv_my_info_to_be_returned);
        returned.setOnClickListener(this);
        TextView bank = (TextView) view.findViewById(R.id.tv_my_info_my_bank);
        bank.setOnClickListener(this);
        TextView jf = (TextView) view.findViewById(R.id.tv_my_info_my_jf);
        jf.setOnClickListener(this);
        TextView address = (TextView) view.findViewById(R.id.tv_my_info_my_address);
        address.setOnClickListener(this);
        TextView taobao = (TextView) view.findViewById(R.id.tv_my_info_my_taobao);
        taobao.setOnClickListener(this);
        TextView accredit = (TextView) view.findViewById(R.id.tv_my_info_my_accredit);
        accredit.setOnClickListener(this);
        TextView setting = (TextView) view.findViewById(R.id.tv_my_info_my_setting);
        setting.setOnClickListener(this);

        slotmachine = (TextView) view.findViewById(R.id.tv_my_info_my_slotmachine);
        slotmachine.setOnClickListener(this);
        TextView slotmachineShare = (TextView) view.findViewById(R.id.tv_my_info_slotmachine_share);
        slotmachineShare.setOnClickListener(this);
        warehouse = (TextView) view.findViewById(R.id.tv_my_info_my_warehouse);
        warehouse.setOnClickListener(this);
        loginBoard = (LinearLayout) view.findViewById(R.id.ll_my_info_login_board);
        userName = (TextView) view.findViewById(R.id.tv_my_info_user_name);
        try {
            mUser = PulamsiApplication.dbUtils.findFirst(Selector.from(User.class));
            setViewVisible();
        } catch (DbException e) {
            e.printStackTrace();
        }
        /**
         * 在onCreate()里面获取控件的高度是0，那是因为当onCreate方法执行完以后，
         * 我们定义的控件才会被度量(measure)，所以我们在onCreate方法里面通过view.getHeight()获取控件的高度或者宽度是0。
         */
        ViewTreeObserver vto2 = paid.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                paid.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int h = paid.getHeight();
                int w = paid.getWidth();
                int badgeHorizontal = (int) (w / 4);
                int badgeVertical = h / 8;
                initCornerMmark(badgeHorizontal, badgeVertical);//初始化角标
            }
        });
    }

    /**
     * 初始化角标
     *
     * @param badgeHorizontal
     * @param badgeVertical
     */
    private void initCornerMmark(int badgeHorizontal, int badgeVertical) {
        paidMark = getCornerMark(badgeHorizontal, badgeVertical, paid);
        sendMark = getCornerMark(badgeHorizontal, badgeVertical, send);
        receivedMark = getCornerMark(badgeHorizontal, badgeVertical, received);
        evaluationMark = getCornerMark(badgeHorizontal, badgeVertical, returned);
    }

    /**
     * 获取角标
     *
     * @param badgeHorizontal
     * @param badgeVertical
     * @param view
     * @return
     */
    private BadgeView getCornerMark(int badgeHorizontal, int badgeVertical, TextView view) {
        BadgeView badge = new BadgeView(MyinfoFragment.this.getActivity(), view);
        badge.setTypeface(Typeface.DEFAULT);//不会变粗
        badge.setTextColor(Color.RED);
        badge.setBadgeMargin(badgeHorizontal, badgeVertical);
        badge.setBackgroundResource(R.drawable.order_badge_view_bg);
        return badge;
    }


    /**
     * 重新获取前端，需要进行权限控制
     * 每次都需要重新更新数据和界面
     */
    @Override
    public void onResume() {
        super.onResume();
        setAvatar();//设置头像
        try {
            mUser = PulamsiApplication.dbUtils.findFirst(Selector.from(User.class));
            setViewVisible();
            if (mUser.isHasLogin()) {
                getBalance();//获取余额
                getMarkCount();//获取角标数量
            } else {
                hideAllMark();//隐藏所有角标
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 未登录隐藏角标
     */
    private void hideAllMark() {
        if (paidMark == null || sendMark == null || receivedMark == null || evaluationMark == null)
            return;
        paidMark.hide();
        sendMark.hide();
        receivedMark.hide();
        evaluationMark.hide();
    }

    /**
     * 获取订单角标数量
     */
    private void getMarkCount() {
        String showAccount = getString(R.string.serverbaseurl) + getString(R.string.order_count);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, showAccount, new Response.Listener<String>() {
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject jb = new JSONObject(result);
                        String paidCount = jb.getString("待付款");
                        String sendCount = jb.getString("待发货");
                        String receivedCount = jb.getString("待收货");
                        String evaluationCount = jb.getString("待评价");
                        setOrderCount(paidCount, sendCount, receivedCount, evaluationCount);
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "角标数量装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mid", Constants.userId);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    /**
     * 设置角标数量
     *
     * @param paidCount
     * @param sendCount
     * @param receivedCount
     * @param evaluationCount
     */
    private void setOrderCount(String paidCount, String sendCount, String receivedCount, String evaluationCount) {
        if (TextUtils.isEmpty(paidCount) || TextUtils.isEmpty(sendCount) || TextUtils.isEmpty(receivedCount) || TextUtils.isEmpty(evaluationCount))
            return;
        if (paidCount.equals("0")) {
            paidMark.hide();
        } else {
            paidMark.setText(paidCount);
            paidMark.show();
        }
        if (sendCount.equals("0")) {
            sendMark.hide();
        } else {
            sendMark.setText(sendCount);
            sendMark.show();
        }
        if (receivedCount.equals("0")) {
            receivedMark.hide();
        } else {
            receivedMark.setText(receivedCount);
            receivedMark.show();
        }
        if (evaluationCount.equals("0")) {
            evaluationMark.hide();
        } else {
            evaluationMark.setText(evaluationCount);
            evaluationMark.show();
        }
    }

    private void setAvatar() {
        try {
            Member member = PulamsiApplication.dbUtils.findFirst(Member.class);
            if (member != null) {
                PulamsiApplication.imageLoader.displayImage(
                        getResources().getString(R.string.serverbaseurl) + member.getImgUrl(), userIcon, new DisplayImageOptions.Builder()
                                .showImageOnLoading(R.drawable.default_avatar) //设置图片在下载期间显示的图片
                                .showImageForEmptyUri(R.drawable.default_avatar)//设置图片Uri为空或是错误的时候显示的图片
                                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                                .build());
            } else {
                userIcon.setImageResource(R.drawable.default_avatar);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化用户名，权限控制和登录
     */
    private void setViewVisible() {
        if (null != mUser && mUser.isHasLogin()) {
            loginorRegister.setVisibility(View.GONE);
            loginBoard.setVisibility(View.VISIBLE);
            userName.setText(mUser.getUserName());
        } else {
            loginBoard.setVisibility(View.GONE);
            loginorRegister.setVisibility(View.VISIBLE);
        }

        try {
            Role slotmachinerole = PulamsiApplication.dbUtils.findFirst(Selector.from(Role.class).where("pname", "=", "售货机"));
            if (null != slotmachinerole) {
                slotmachine.setVisibility(View.VISIBLE);
            } else {
                slotmachine.setVisibility(View.GONE);
            }
//            Role kucunrole = PulamsiApplication.dbUtils.findFirst(Selector.from(Role.class).where("pname", "=", "库存"));
//            if (null != kucunrole) {
//                warehouse.setVisibility(View.VISIBLE);
//                warehouseLine.setVisibility(View.VISIBLE);
//            } else {
//                warehouse.setVisibility(View.GONE);
//                warehouseLine.setVisibility(View.GONE);
//            }
//            if (null == kucunrole && null == slotmachinerole){
//                warehouseLine2.setVisibility(View.GONE);
//            }else {
//                warehouseLine2.setVisibility(View.VISIBLE);
//            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void getBalance() {
        String showAccount = getString(R.string.serverbaseurl) + getString(R.string.showAccount);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, showAccount, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        memberAccount = gson.fromJson(result, MemberAccount.class);
                        balance.setText("余额:¥" + memberAccount.getMemberAccountBalance());
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "我的余额装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mid", Constants.userId);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }
}

