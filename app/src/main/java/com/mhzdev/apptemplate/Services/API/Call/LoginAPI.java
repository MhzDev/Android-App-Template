package com.mhzdev.apptemplate.Services.API.Call;


import android.content.Context;

import com.mhzdev.apptemplate.Services.API.BasicAPI;

public class LoginAPI extends BasicAPI {
    public String user_email;
    public String user_password;

    public LoginAPI(Context context) {
        super(context);
    }
}
