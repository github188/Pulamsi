package com.pulamsi.myinfo.slotmachineManage;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;

/**
 * 售货机地图弹出框
 */
public class MapMarkerinfoLayout extends LinearLayout {

    public MapMarkerinfoLayout(final Context context) {
        super(context);
        setBackgroundResource(R.drawable.baidumap_icon);
//		setLayoutParams(new LayoutParams((int) (PulamsiApplication.ScreenWidth * 0.5),
//				(int) (PulamsiApplication.ScreenWidth * 0.2)));
        setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        setPadding((int) (PulamsiApplication.ScreenWidth * 0.03), (int) (PulamsiApplication.ScreenWidth * 0.02), (int) (PulamsiApplication.ScreenWidth * 0.02), (int) (PulamsiApplication.ScreenWidth * 0.08));
//        setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        TextView terminalName = new TextView(context);
        terminalName.setLayoutParams(new LayoutParams((int) (PulamsiApplication.ScreenWidth * 0.4),
                (int) (PulamsiApplication.ScreenWidth * 0.05)));
        terminalName.setId(1);
        this.addView(terminalName);
        TextView isonline = new TextView(context);
        isonline.setLayoutParams(new LayoutParams((int) (PulamsiApplication.ScreenWidth * 0.4),
                (int) (PulamsiApplication.ScreenWidth * 0.05)));
        isonline.setId(2);
        this.addView(isonline);
        TextView terminalAddress = new TextView(context);
        terminalAddress.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        terminalAddress.setId(3);
        terminalAddress.setMaxEms(10);
        terminalAddress.setEllipsize(TruncateAt.END);
        this.addView(terminalAddress);
    }
}
