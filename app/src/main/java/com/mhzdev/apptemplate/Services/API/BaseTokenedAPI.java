package com.mhzdev.apptemplate.Services.API;


import android.content.Context;

import com.mhzdev.apptemplate.Session.UserSessionManager;


public class BaseTokenedAPI {

    private String lang;
    private String token;

    protected BaseTokenedAPI(Context context){
        lang = UserSessionManager.getInstance(context).getLang();
        token = UserSessionManager.getInstance(context).getToken();
    }

}
