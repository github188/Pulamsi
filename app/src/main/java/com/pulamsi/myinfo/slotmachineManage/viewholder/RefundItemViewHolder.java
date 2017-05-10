package com.pulamsi.myinfo.slotmachineManage.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pulamsi.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-04-19
 * Time: 17:09
 * FIXME
 */
public class RefundItemViewHolder extends RecyclerView.ViewHolder {

    public TextView serialNumber;//退款编号
    public TextView refundState;//退款状态
    public TextView payment;//支付方式
    public TextView mobilePhone;//手机号码
    public TextView refundAmount;//退款金额
    public TextView createDate;//创建日期
    public TextView submit;//提交退款


    public RefundItemViewHolder(View itemView){
        super(itemView);
        serialNumber = (TextView) itemView.findViewById(R.id.tv_refund_serial_number);
        refundState = (TextView)itemView.findViewById(R.id.tv_refund_state);
        payment = (TextView)itemView.findViewById(R.id.tv_refund_payment);
        mobilePhone = (TextView)itemView.findViewById(R.id.tv_refund_phone);
        refundAmount = (TextView)itemView.findViewById(R.id.tv_refund_amount);
        createDate = (TextView)itemView.findViewById(R.id.tv_refund_create_date);
        submit = (TextView)itemView.findViewById(R.id.tv_refund_submit);
    }
}
