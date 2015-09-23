package com.mhzdev.apptemplate.Services;

import com.mhzdev.apptemplate.Config.MainConfig;

import retrofit.RestAdapter;

public class ApiAdapterBuilder {

    public ApiAdapterBuilder(){}

    /**
     * Build the ApiManager
     * @return
     */
    public static ApiList getApiAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MainConfig.API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL) //TODO Log level setted to full
                .build();
        return restAdapter.create(ApiList.class);
    }

}


