package com.mhzdev.apptemplate.Utils;

import android.content.Context;

/**
 * Created by Michele Stefanelli (MhzDev) on 15/09/2015.
 */
public class CMuffin {
    public static void make(Context context, String text){
        Muffin.makeText(context, text, Muffin.LENGTH_SHORT).show();
    }
    public static void make(Context context, int text){
        Muffin.makeText(context, text, Muffin.LENGTH_SHORT).show();
    }
    public static void make(Context context, CharSequence text) {
        Muffin.makeText(context, text, Muffin.LENGTH_LONG).show();
    }


    public static void makeLong(Context context, String text){
        Muffin.makeText(context, text, Muffin.LENGTH_LONG).show();
    }
    public static void makeLong(Context context, int text){
        Muffin.makeText(context, text, Muffin.LENGTH_LONG).show();
    }
    public static void makeLong(Context context, CharSequence text) {
        Muffin.makeText(context, text, Muffin.LENGTH_LONG).show();
    }
}
