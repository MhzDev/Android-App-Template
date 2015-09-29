package com.mhzdev.apptemplate.Services.API.Call;


import android.content.Context;

import com.mhzdev.apptemplate.Services.API.BasicAPI;

public class RegistrationAPI extends BasicAPI {
    public String user_email;
    public String user_password;
    public String username;

    public RegistrationAPI(Context context) {
        super(context);
    }
}
