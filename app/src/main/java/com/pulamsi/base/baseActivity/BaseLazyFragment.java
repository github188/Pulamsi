package com.pulamsi.base.baseActivity;

/**
 * Created by DAI on 16/4/10.
 * 禁止ViewPager预加载的基类
 */
public abstract class BaseLazyFragment extends android.support.v4.app.Fragment {

    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible=true;
            onVisible();
        }else {
            isVisible=false;
            onInvisible();
        }
    }

    /**
     * visible->lazyLoad
     */
    private void onVisible() {
        lazyLoad();
    }

    private void onInvisible() {

    }

    protected abstract void lazyLoad();
}
