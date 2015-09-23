package com.mhzdev.apptemplate.Services.API;


import android.content.Context;

import com.mhzdev.apptemplate.Session.UserSessionManager;


public class BasicAPI {

    public String lang;

    public BasicAPI(Context context){
        lang = UserSessionManager.getInstance(context).getLang();
    }

}
