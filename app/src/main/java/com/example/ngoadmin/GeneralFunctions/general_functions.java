package com.example.ngoadmin.GeneralFunctions;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.ngoadmin.R;


public class general_functions {


    public static String SingleQuoteString(String str){
        if (str != null) {
            if(str.indexOf('\'') > 0){
                str = str.replace("'","\\'");
            }
        }
        return str;
    }

    public static void DisabledView(Context context, View view, TextView text ){
        view.setBackgroundResource(R.drawable.disabled_view);
        view.setClickable(false);
        text.setClickable(false);
        text.setTextColor(ContextCompat.getColor(context,R.color.black));
    }

    public static void EnabledView(Context context, View view, TextView text ){
        view.setBackgroundResource(R.drawable.submit_button_back);
        view.setClickable(true);
        text.setClickable(true);
        text.setTextColor(ContextCompat.getColor(context,R.color.white));
    }

}
