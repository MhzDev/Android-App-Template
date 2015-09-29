package com.mhzdev.apptemplate.Services.API.Call;


import android.content.Context;

import com.mhzdev.apptemplate.Services.API.BasicAPI;

public class FbAccessAPI extends BasicAPI {
    public String user_email;
    public String username;
    public String token;
    public String user_id;

    public FbAccessAPI(Context context) {
        super(context);
    }
}
