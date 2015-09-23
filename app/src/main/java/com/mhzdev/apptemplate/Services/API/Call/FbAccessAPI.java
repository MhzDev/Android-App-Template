package com.mhzdev.apptemplate.Services.API.Call;


import android.content.Context;

import com.mhzdev.apptemplate.Services.API.BasicAPI;

public class FbAccessAPI extends BasicAPI {
    public String u_email;
    public String u_name;
    public String u_sex;
    public String token;
    public String user_id;

    public FbAccessAPI(Context context) {
        super(context);
    }
}
