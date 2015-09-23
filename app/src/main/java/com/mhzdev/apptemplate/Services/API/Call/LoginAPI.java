package com.mhzdev.apptemplate.Services.API.Call;


import android.content.Context;

import com.mhzdev.apptemplate.Services.API.BasicAPI;

public class LoginAPI extends BasicAPI {
    public String u_email;
    public String u_password;

    public LoginAPI(Context context) {
        super(context);
    }
}
