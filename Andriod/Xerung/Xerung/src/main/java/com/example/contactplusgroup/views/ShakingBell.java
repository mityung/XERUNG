package com.example.contactplusgroup.views;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mityung.contactdirectory.R;

/**
 * Created by ten-thousand-hours on 8/3/16.
 */
public class ShakingBell extends FrameLayout {

    private View rootView;
    private CircleView circleView;
    ImageView iv ;
    ImageView iv2;
    Animation animation, animation2;
    ValueAnimator animation3;

    public ShakingBell(Context context) {
        this(context, null);
    }

    public ShakingBell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShakingBell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.shakingbell, this, true);
        iv = (ImageView) findViewById(R.id.iv_bell);
        iv2 = (ImageView) findViewById(R.id.iv_bell2);
        circleView = (CircleView) findViewById(R.id.circleView);

        animation = new RotateAnimation(0, -20, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.3f);
        animation.setInterpolator(new SpringInterpolator());
        animation.setDuration(500);

        animation2 = new TranslateAnimation(0, 20, 0, 0);
        animation2.setInterpolator(new SpringInterpolator());
        animation2.setDuration(500);
        animation2.setStartOffset(100);

        animation3 = ValueAnimator.ofFloat(1);
        animation3.setDuration(300);
        animation3.setInterpolator(new OvershootInterpolator());
        animation3.setStartDelay(300);
        animation3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleView.setScaleX((Float) animation.getAnimatedValue());
                circleView.setScaleY((Float) animation.getAnimatedValue());
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void shake (int i) {
        iv.startAnimation(animation);
        iv2.startAnimation(animation2);
        circleView.setMessageNum(i);
        animation3.start();
    }
}
