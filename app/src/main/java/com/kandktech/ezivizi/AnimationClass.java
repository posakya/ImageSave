package com.kandktech.ezivizi;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;

public class AnimationClass {
   public static Animation animation;

    public static void Startanimation(int duration){
        animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(duration);
        animation.setInterpolator(new AccelerateInterpolator());
    }

    public static void bounceAnimation(int duration){
        animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(duration);
        animation.setInterpolator(new BounceInterpolator());
    }
}

/*
 android:duration="5000"
    android:fromAlpha="0.0"
    android:toAlpha="1.0"
    android:interpolator="@android:anim/accelerate_interpolator"/>

 */