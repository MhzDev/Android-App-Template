package com.mhzdev.apptemplate.Services;

import com.mhzdev.apptemplate.Services.API.Call.AutoLoginAPI;
import com.mhzdev.apptemplate.Services.API.Call.FbAccessAPI;
import com.mhzdev.apptemplate.Services.API.Call.GetDataListAPI;
import com.mhzdev.apptemplate.Services.API.Call.LoginAPI;
import com.mhzdev.apptemplate.Services.API.Call.RecoverPasswordAPI;
import com.mhzdev.apptemplate.Services.API.Call.RegistrationAPI;
import com.mhzdev.apptemplate.Services.API.Response.FbAccessResponse;
import com.mhzdev.apptemplate.Services.API.Response.GetDataListResponse;
import com.mhzdev.apptemplate.Services.API.Response.LoginResponse;
import com.mhzdev.apptemplate.Services.API.Response.RecoverPasswordResponse;
import com.mhzdev.apptemplate.Services.API.Response.RegistrationResponse;
import com.mhzdev.apptemplate.Services.API.Response.TokenResponse;

import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Example response
 * (This part is mandatory, is used to catch the errors)
 * The real response is inserted in the {} of "response"
 * {
 * "status": "KO",
 * "error_code": "101",
 * "error_message": "Error Message",
 * "lang": "en_US",
 * "response": {}
 * }
 */
public interface ApiList {

    //Main response
    @SuppressWarnings("unused")
    String OK = "OK";
    @SuppressWarnings("unused")
    String KO = "KO";

    /*Auto Login*/
    @POST("/autoLogin.php")
    //Make a test call to check if token is already valid
    void autoLogin(
            @Body AutoLoginAPI json, // Token, Lang
            ApiCallback<TokenResponse> callback
    );

    /*Login*/
    @POST("/login.php")
    //Make the login
    void login(
            @Body LoginAPI loginAPI,
            ApiCallback<LoginResponse> callback
    );

    /*Register*/
    @POST("/registration.php")
    //Make the registration
    void register(
            @Body RegistrationAPI registrationAPI,
            ApiCallback<RegistrationResponse> callback
    );

    /*Password recover*/
    @POST("/passwordRecover.php")
    //Communicate to the server a password reset request
    void recoverPassword(
            @Body RecoverPasswordAPI registrationAPI,
            ApiCallback<RecoverPasswordResponse> callback
    );

    /*Facebook Access (Also Registration)*/
    @POST("/facebookAccess.php")
    //Register to server the new fb user / Request login to server with fb
    void fbAccess(
            @Body FbAccessAPI fbAccessAPI,
            ApiCallback<FbAccessResponse> callback
    );


    /* Get a list of data */
    @POST("/getDataList.php")
    //Register to server the new fb user / Request login to server with fb
    void getDataList(
            @Body GetDataListAPI getDataListAPI,
            ApiCallback<GetDataListResponse> callback
    );
}


