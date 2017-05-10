package com.pulamsi.views.qrcode.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.security.SecurityActivity;
import com.pulamsi.utils.Utils;
import com.pulamsi.views.SweetAlert.SweetAlertDialog;
import com.pulamsi.views.qrcode.decode.DecodeThread;


public class ResultActivity extends BaseActivity {

    private ImageView mResultImage;
    private TextView mResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setHeaderTitle("扫描结果");
        Bundle extras = getIntent().getExtras();

        mResultImage = (ImageView) findViewById(R.id.result_image);
        mResultText = (TextView) findViewById(R.id.result_text);

        if (null != extras) {
            int width = extras.getInt("width");
            int height = extras.getInt("height");

            LayoutParams lps = new LayoutParams(width, height);
            lps.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            lps.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
            lps.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

            mResultImage.setLayoutParams(lps);

            String result = extras.getString("result");
            mResultText.setText(result);


            Bitmap barcode = null;
            byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                // Mutable copy:
                barcode = barcode.copy(Bitmap.Config.RGB_565, true);
            }

            mResultImage.setImageBitmap(barcode);
            //判断是不是为url
            if (result != null) {
                String replaceResult = result.replaceAll("[\n\r]", "");//去掉空格和换行
                if (replaceResult.contains("code") && replaceResult.length() > 20) {
                    Intent intent = new Intent(this, SecurityActivity.class);
                    intent.putExtra("code", replaceResult.substring(replaceResult.length() - 16, replaceResult.length()));
                    startActivity(intent);
                    finish();
                } else {
                    if (Utils.isTopURL(replaceResult)) {
                        if (result.indexOf("www") != -1 || result.indexOf("http://") != -1)//包含http或者www
                            if (result.indexOf("http://") == -1 && result.indexOf("https://") == -1) {
                                StringBuilder sb = new StringBuilder(result);
                                sb.insert(0, "http://");
                                openBrower(sb.toString().trim());
                            } else {
                                openBrower(result.trim());
                            }
                    }
                }
            }
        }
    }

    /**
     * 跳转浏览器
     */
    public void openBrower(final String url) {
        if (url.indexOf("pulamsi") == -1){
            new SweetAlertDialog(ResultActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("未知网站，可能有含有危险!")
                    .setConfirmText("继续")
                    .setCancelText("返回")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    })
                    .showCancelButton(false)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            goToUrl(url);
                        }
                    })
                    .show();
        }else {
            goToUrl(url);
        }
    }

    public void goToUrl(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        finish();
    }
}
