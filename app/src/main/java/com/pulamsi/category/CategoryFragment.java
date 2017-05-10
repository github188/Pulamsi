package com.pulamsi.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseFragment;
import com.pulamsi.base.baseAdapter.TabFragmentAdapter;
import com.pulamsi.category.fragment.CategoryCatFragment;
import com.pulamsi.search.SearchDoorActivity;
import com.pulamsi.views.qrcode.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends BaseFragment implements OnClickListener {

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search_edit:
                //点击搜索
                Intent search = new Intent(getActivity(), SearchDoorActivity.class);
                getActivity().startActivity(search);
                break;
            case R.id.category_search_rightimage:
                //点击扫描二维码
                startActivity(new Intent(getActivity(), CaptureActivity.class));
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout searchEdit = (RelativeLayout) view.findViewById(R.id.home_search_edit);
        searchEdit.setOnClickListener(this);
        ImageView rightimage = (ImageView) view.findViewById(R.id.category_search_rightimage);
        rightimage.setOnClickListener(this);

        List<Fragment> tabFragments = new ArrayList<Fragment>();
        tabFragments.add(new CategoryCatFragment());

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.category_viewpager);
        TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(PulamsiApplication.fragmentActivity.getSupportFragmentManager(), tabFragments);
        viewPager.setAdapter(tabFragmentAdapter);
    }

}

