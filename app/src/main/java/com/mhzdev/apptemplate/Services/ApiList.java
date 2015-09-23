package com.mhzdev.apptemplate.Services;

import com.mhzdev.apptemplate.Services.API.Call.*;
import com.mhzdev.apptemplate.Services.API.Response.*;

import retrofit.http.Body;
import retrofit.http.POST;

public interface ApiList {

    //Main response
    @SuppressWarnings("unused")
    String OK = "OK";
    @SuppressWarnings("unused")
    String KO = "KO";

    /*Auto Login*/
    @POST("/autologin") //Make a test call to check if token is already valid
    void autoLogin(
            @Body AutoLoginAPI json, // Token, Lang
            ApiCallback<TokenResponse> callback
    );

    /*Login*/
    @POST("/login") //Make the login
    void login(
            @Body LoginAPI loginAPI,
            ApiCallback<LoginResponse> callback
    );

    /*Register*/
    @POST("/registration") //Make the registration
    void register(
            @Body RegistrationAPI registrationAPI,
            ApiCallback<RegistrationResponse> callback
    );

    /*Password recover*/
    @POST("/passwordRecover") //Communicate to the server a password reset request
    void recoverPassword(
            @Body RecoverPasswordAPI registrationAPI,
            ApiCallback<RecoverPasswordResponse> callback
    );

    /*Facebook Access (Also Registration)*/
    @POST("/facebookAccess") //Register to server the new fb user / Request login to server with fb
    void fbAccess(
            @Body FbAccessAPI fbAccessAPI,
            ApiCallback<FbAccessResponse> callback
    );
}


