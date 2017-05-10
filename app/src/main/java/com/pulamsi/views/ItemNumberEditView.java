package com.pulamsi.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.utils.ToastUtil;


/**
 * 数目编辑控件
 *
 * @author WilliamChik on 15/9/7.
 */
public class ItemNumberEditView extends LinearLayout implements View.OnClickListener {

  private static final int DEFAULT_ITEM_NUM = 1;

  private LinearLayout mMainLayout;

  private TextView mItemNumMinusBtn;

  private TextView mItemNumPlusBtn;

  private EditText mItemNumTv;

  private int mupperLimitNum;


  private NumberEditViewClickListener mNumberEditViewClickListener;

  public ItemNumberEditView(Context context) {
    super(context);
    init(context);
  }

  public ItemNumberEditView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public ItemNumberEditView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setNumberEditViewClickListener(NumberEditViewClickListener numberEditViewClickListener) {
    mNumberEditViewClickListener = numberEditViewClickListener;
  }

  private void init(final Context context) {
    LayoutInflater.from(context).inflate(R.layout.item_number_edit_view_layout, this);
    mMainLayout = (LinearLayout) findViewById(R.id.ll_item_edit_main_layout);
    mItemNumMinusBtn = (TextView) findViewById(R.id.tv_item_edit_item_num_minus);
    mItemNumPlusBtn = (TextView) findViewById(R.id.tv_item_edit_item_num_plus);
    mItemNumTv = (EditText) findViewById(R.id.tv_item_edit_item_num);
    mItemNumTv.setText(Integer.toString(DEFAULT_ITEM_NUM));
    mItemNumMinusBtn.setOnClickListener(this);
    mItemNumPlusBtn.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    int viewId = v.getId();
    if (viewId == R.id.tv_item_edit_item_num_minus) {
      itemNumMinusBtnClickLogic();
    } else if (viewId == R.id.tv_item_edit_item_num_plus) {
      itemNumPlusBtnClickLogic();
    }
  }

  /**
   * 设置显示的数量和数量显示的上限
   *
   * @param displayNum the number diplayed on the number edit view
   * @param upperLimit the upper limit number diplayed on the number edit view
   */
  public void setDiplayNumAndUpperLimit(final String displayNum, String upperLimit) {
    mItemNumTv.setText(displayNum);
    try {
      mupperLimitNum = Integer.valueOf(upperLimit);
      if (Integer.valueOf(displayNum) > 1) {
        enableMinusBtn();
      } else {
        disableMinusBtn();
      }

      if (Integer.valueOf(displayNum) >= mupperLimitNum) {
        disablePlusBtn();
      } else {
        enablePlusBtn();
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    //新增可编辑数量返回
    mItemNumTv.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if ("".equals(mItemNumTv.getText().toString().trim())) {
          return;
        }
        if (Integer.parseInt(mItemNumTv.getText().toString().trim()) > mupperLimitNum) {
          Toast.makeText(PulamsiApplication.fragmentActivity,"数量不能超过" + mupperLimitNum,Toast.LENGTH_SHORT).show();
          mItemNumTv.setText(displayNum);
          return;
        } else if (Integer.parseInt(mItemNumTv.getText().toString().trim()) == mupperLimitNum) {
          disablePlusBtn();
        }
        if (Integer.valueOf(mItemNumTv.getText().toString().trim()) > 1) {
          enableMinusBtn();
        } else {
          disableMinusBtn();
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });
  }

  /**
   * 点击【+】按钮，数目加一
   */
  private void itemNumPlusBtnClickLogic() {
    try {
      int newNum = Integer.valueOf(mItemNumTv.getText().toString()) + 1;
      mItemNumTv.setText(Integer.toString(newNum));
      if (newNum > DEFAULT_ITEM_NUM) {
        enableMinusBtn();
      }
      if (newNum == mupperLimitNum) {
        disablePlusBtn();
      }
      if (mNumberEditViewClickListener != null) {
        mNumberEditViewClickListener.onPlusBtnClick();
        mNumberEditViewClickListener.onNumberChange();
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
  }

  /**
   * 点击【-】按钮，数目减一
   */
  private void itemNumMinusBtnClickLogic() {
    try {
      int newNum = Integer.valueOf(mItemNumTv.getText().toString()) - 1;
      mItemNumTv.setText(Integer.toString(newNum));
      if (newNum == DEFAULT_ITEM_NUM) {
        disableMinusBtn();
      }
      if (newNum < mupperLimitNum) {
        enablePlusBtn();
      }
      if (mNumberEditViewClickListener != null) {
        mNumberEditViewClickListener.onMinusBtnClick();
        mNumberEditViewClickListener.onNumberChange();
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param heightPixal height of the main layout in pixal which you want to set
   */
  public void setMainLayoutHeight(int heightPixal) {
    mMainLayout.getLayoutParams().height = heightPixal;
  }

  /**
   * @param bgResId background resource id
   */
  public void setMinusBtnBackground(int bgResId) {
    mItemNumMinusBtn.setBackgroundResource(bgResId);
  }

  /**
   * @param bgResId background resource id
   */
  public void setPlusBtnBackground(int bgResId) {
    mItemNumPlusBtn.setBackgroundResource(bgResId);
  }

  /**
   * @param bgResId background resource id
   */
  public void setItemNumTvBackground(int bgResId) {
    mItemNumTv.setBackgroundResource(bgResId);
  }

  private void disableMinusBtn() {
    mItemNumMinusBtn.setBackgroundResource(R.drawable.layer_list_bg_stroke_c2_1px_1px_0_1px_solid_transparent);
    mItemNumMinusBtn.setTextColor(PulamsiApplication.context.getResources().getColor(R.color.app_font_color_c2));
    mItemNumMinusBtn.setClickable(false);
  }

  private void enableMinusBtn() {
    mItemNumMinusBtn.setBackgroundResource(R.drawable.layer_list_bg_stroke_2f_1px_1px_1px_0_solid_transparent);
    mItemNumMinusBtn.setTextColor(PulamsiApplication.context.getResources().getColor(R.color.app_font_color_2f));
    mItemNumMinusBtn.setClickable(true);
  }

  private void disablePlusBtn() {
    mItemNumPlusBtn.setBackgroundResource(R.drawable.layer_list_bg_stroke_c2_0_1px_1px_1px_solid_transparent);
    mItemNumPlusBtn.setTextColor(PulamsiApplication.context.getResources().getColor(R.color.app_font_color_c2));
    mItemNumPlusBtn.setClickable(false);
  }

  private void enablePlusBtn() {
    mItemNumPlusBtn.setBackgroundResource(R.drawable.layer_list_bg_stroke_2f_0_1px_1px_1px_solid_transparent);
    mItemNumPlusBtn.setTextColor(PulamsiApplication.context.getResources().getColor(R.color.app_font_color_2f));
    mItemNumPlusBtn.setClickable(true);
  }

  public String getItemDisplayNumString() {
    return mItemNumTv.getText().toString();
  }

  /**
   * @return the number diplayed on the number edit view now
   */
  public int getItemDisplayNum() {
    return Integer.valueOf(mItemNumTv.getText().toString());
  }

  /**
   * 数目编辑按钮【-】和【+】两个按钮的点击监听器
   */
  public interface NumberEditViewClickListener {

    /**
     * 【-】按钮或【+】按钮任意一个的点击回调
     */
    void onNumberChange();

    /**
     * 【-】按钮的点击回调
     */
    void onMinusBtnClick();

    /**
     * 【+】按钮的点击回调
     */
    void onPlusBtnClick();

  }

}
