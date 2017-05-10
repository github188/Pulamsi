package com.pulamsi.myinfo.address.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * @author WilliamChik on 15/9/19.
 */
public class ShippingAddressItemViewHolder extends RecyclerView.ViewHolder {

  public TextView shippingUserNameTv;

  public TextView shippingPhoneNumTv;

  public TextView shippingUserAddressTv;

  public TextView shippingSetToDefaultAddressBtn;

  public TextView shippingEditAddressBtn;

  public TextView shippingDeleteAddressBtn;

  public ShippingAddressItemViewHolder(View itemView) {
    super(itemView);

    shippingUserNameTv = (TextView) itemView.findViewById(R.id.tv_shipping_address_management_user_name);
    shippingPhoneNumTv = (TextView) itemView.findViewById(R.id.tv_shipping_address_management_phone_num);
    shippingUserAddressTv = (TextView) itemView.findViewById(R.id.tv_shipping_address_management_user_address);
    shippingSetToDefaultAddressBtn =
        (TextView) itemView.findViewById(R.id.tv_shipping_address_management_set_to_default_address_btn);
    shippingEditAddressBtn = (TextView) itemView.findViewById(R.id.tv_shipping_address_management_address_edit_str);
    shippingDeleteAddressBtn = (TextView) itemView.findViewById(R.id.tv_shipping_address_management_address_delete_btn);
  }
}
