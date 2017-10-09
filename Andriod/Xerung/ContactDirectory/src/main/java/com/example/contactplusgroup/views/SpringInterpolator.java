package com.example.contactplusgroup.views;

import android.view.animation.Interpolator;

/**
 * Created by ten-thousand-hours on 8/3/16.
 */
public class SpringInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
        /*
                sin(x * pi) e⁻ˣ
         */
        return  (float)(-Math.sin(Math.PI * (8*input))*Math.pow(Math.PI, -(2*input))*1.2);

    }
}
