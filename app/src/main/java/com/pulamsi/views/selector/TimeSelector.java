package com.pulamsi.views.selector;

import android.content.Context;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.pulamsi.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DaiDingKang on 2016/3/7.
 */
public class TimeSelector {
    private Context context;
    TimePickerView pvTime;
    TextView textView;
    public TimeSelector(Context context,TextView textView){
        this.context = context;
        this.textView = textView;
        init();
    }

    private void init() {
        //时间选择器
        pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 80, calendar.get(Calendar.YEAR));

        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //标题
        pvTime.setTitle(context.getString(R.string.choose_birthday));
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                textView.setText(getTime(date));
            }
        });
    }
    //弹出选择器
    public void show(){
        pvTime.show();
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
