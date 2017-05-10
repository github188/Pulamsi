package com.pulamsi.views.PhotoView;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.BaseAppManager.BaseAppManager;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * @author daidingkang
 * @email
 */
public class PhotoviewActivity extends AppCompatActivity{
    private HackyViewPager mViewPager;
    /**
     * 魅族用此控件会出问题
     */
    private PhotoViewAdapter mAdapter;


    ArrayList<String> imagerUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewpager);
        BaseAppManager.getInstance().addActivity(this);
        setupView();
        setupData();
    }

    private void setupView(){
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
    }

    private void setupData(){
        int mCurrentUrl = getIntent().getIntExtra("photo_position",0);
        imagerUrl = getIntent().getStringArrayListExtra("imagerUrl");
        mAdapter = new PhotoViewAdapter();
        mViewPager.setAdapter(mAdapter);
        //设置当前需要显示的图片
        mViewPager.setCurrentItem(mCurrentUrl);
    }


    class PhotoViewAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(PhotoviewActivity.this)//更改图片加载框架
                    .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + imagerUrl.get(position))
                    .placeholder(R.drawable.pulamsi_loding)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photoView);
//            //使用ImageLoader加载图片
//            PulamsiApplication.imageLoader.displayImage(
//                    PulamsiApplication.context.getString(R.string.serverbaseurl)+imagerUrl.get(position), photoView, PulamsiApplication.options);
            //给图片增加点击事件
//            mAttacher = new PhotoViewAttacher(mPhotoView);
//            mAttacher.setOnViewTapListener(PhotoviewActivity.this);
            container.addView(photoView,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return imagerUrl.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
