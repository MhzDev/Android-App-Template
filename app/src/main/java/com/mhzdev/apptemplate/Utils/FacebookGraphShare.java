package com.mhzdev.apptemplate.Utils;

import android.app.Activity;
import android.content.Context;

import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;


public class FacebookGraphShare {


    public static void shareOnFbStart(Context context){
        // Create an object
        ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                .putString("og:type", "paths:path")
                .putString("og:title", "MODEL TO SHARE")
                .putString("og:description", "MODEL TO SHARE")
                .build();
        // Create an action
        ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                .setActionType("paths.start")
                .putObject("path", object)
                .build();
        // Create the content
        ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                .setPreviewPropertyName("path")
                .setAction(action)
                .build();
        ShareDialog.show((Activity) context, content);
    }
}
