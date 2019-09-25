package com.example.ngoadmin.GeneralFunctions;

import android.content.Context;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class transitions {


    public static int TransitionTitleBar(int y, int oldy,int flag_title_scroll,
                                          ViewGroup Parent, View BackArrow,View TitleBar,
                                          Animation animTitleSlideup, Animation animTitleSlideDown){
        int diff = y-oldy;
        if(diff<0)
            diff*=-1;

        if(diff>10){
            if(y>oldy && flag_title_scroll == 0 ){
                flag_title_scroll = 1;
                SlideUpDown(Parent,BackArrow,true,1);
                //transitions.FadeInOut(ll_toolbar_addevent,true,ngo_add_event.this);
                TitleBar.startAnimation(animTitleSlideup);

            }
            else if(y<oldy && flag_title_scroll==1 && y <100){
                flag_title_scroll = 0;
                SlideUpDown(Parent,BackArrow,false,1);
                //transitions.FadeInOut(ll_toolbar_addevent,false,ngo_add_event.this);
                TitleBar.startAnimation(animTitleSlideDown);
            }
            else{}

        }

        if(y==0 && flag_title_scroll==1){
            flag_title_scroll = 0;
            SlideUpDown(Parent,BackArrow,false,1);
            //transitions.FadeInOut(ll_toolbar_addevent,false,ngo_add_event.this);
            TitleBar.startAnimation(animTitleSlideDown);
        }


        return flag_title_scroll;
    }


    public static Boolean SlideUpDown(ViewGroup parent, View target, Boolean isVisible,int gravity){
        Transition transition = new Slide(Gravity.BOTTOM);
        if(gravity == 1)
            transition = new Slide(Gravity.TOP);
        else if(gravity == 2)
            transition = new Slide(Gravity.CENTER);
        transition.setDuration(200);
        transition.addTarget(target.getId());
        TransitionManager.beginDelayedTransition(parent,transition);
        isVisible = !isVisible;
        target.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        return isVisible;
    }

    public static Boolean FadeInOut( View target, Boolean isVisible, Context context,int duration){

        Animation mLoadAnimation;
        if(isVisible == true){
            mLoadAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        }
        else
        {
           mLoadAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        }

        mLoadAnimation.setDuration(duration);
        target.startAnimation(mLoadAnimation);
        isVisible = !isVisible;
        target.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        return isVisible;
    }
}
