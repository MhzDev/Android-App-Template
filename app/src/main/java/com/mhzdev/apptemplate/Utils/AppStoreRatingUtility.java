package com.mhzdev.apptemplate.Utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class AppStoreRatingUtility {
    public static void openAppRating(Context context){
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            String myUrl ="https://play.google.com/store/apps/details?id=" + context.getPackageName();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(myUrl)));
        }
    }
}
