package com.mhzdev.apptemplate.Services.API.Call;


import android.content.Context;

import com.mhzdev.apptemplate.Services.API.BasicAPI;

public class RegistrationAPI extends BasicAPI {
    public String u_email;
    public String u_password;
    public String u_name;

    public RegistrationAPI(Context context) {
        super(context);
    }
}
