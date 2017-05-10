package com.pulamsi.views.secucrity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pulamsi.R;

public class SecuritySuccessView extends RelativeLayout {

    private ImageView security_fly_star;
    private ImageView security_star;
    private ImageView security_zp;
    private Animation scaleAnimation;
    private Animation bounceAnimation;
    private Animation translateAnimation;
    RelativeLayout rl_security_failure;
    RelativeLayout rl_security_success;

    public SecuritySuccessView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SecuritySuccessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public SecuritySuccessView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        //在构造函数中将Xml中定义的布局解析出来。
        View view = LayoutInflater.from(context).inflate(R.layout.security_success, this, true);
        rl_security_success = (RelativeLayout) view.findViewById(R.id.rl_security_success);
        rl_security_failure = (RelativeLayout) view.findViewById(R.id.rl_security_failure);
        security_fly_star = (ImageView) view.findViewById(R.id.security_fly_star);
        security_star = (ImageView) view.findViewById(R.id.security_star);
        security_zp = (ImageView) view.findViewById(R.id.security_zp);

        scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_security_scale_animation);
        bounceAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_security_bounce_animation);
        translateAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_security_translate_animation);
    }

    public void Start() {
        rl_security_success.setVisibility(View.VISIBLE);
        security_star.startAnimation(scaleAnimation);
        security_fly_star.startAnimation(bounceAnimation);
        security_zp.startAnimation(translateAnimation);
    }

    public void Success() {
        rl_security_success.setVisibility(View.VISIBLE);
        rl_security_failure.setVisibility(View.GONE);

    }

    public void Failure() {
        rl_security_success.setVisibility(View.GONE);
        rl_security_failure.setVisibility(View.VISIBLE);
    }

}
