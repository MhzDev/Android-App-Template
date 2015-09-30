package com.mhzdev.apptemplate.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.mhzdev.apptemplate.Config.MainConfig;
import com.mhzdev.apptemplate.Controller.Dialog.LoadingDialog;
import com.mhzdev.apptemplate.Controller.Login.LoginActivity;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Services.API.BaseResponse;
import com.mhzdev.apptemplate.Utils.CMuffin;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * ApiCallback
 * Main implementation of the Callback.
 * Provide 3 method when extended:
 * - OnSuccess - Server return OK
 * - OnResponseKO - Server return KO for some reason
 * - OnFail - Network fail
 * @param <T>
 */
public abstract class ApiCallback<T> implements Callback<BaseResponse<T>> {

    private final String LOG_TAG = "ApiCallback";
    private final Context context;

    //Loading dialog shown if enabled
    private AlertDialog progressDialog;
    //Error will be notified if enabled
    private boolean notifyError;

    /**
     * Constructor to extend
     * @param context app context
     * @param requestLoading Show a loading dialog
     * @param notifyErrors Enable toast error notification
     */
    public ApiCallback(Context context, boolean requestLoading, boolean notifyErrors) {
        this.context = context;
        this.notifyError = notifyErrors;

        if(requestLoading)
            this.progressDialog =  LoadingDialog.showDialog(context);
    }

    /**
     * OK returned from server
     *
     * @param response server response
     * @param httpResponse network response
     */
    @Override
    public void success(BaseResponse<T> response, Response httpResponse) {
        dismissDialog();

        //Catch KO and pass to method
        if(!response.status.equals(ApiList.OK)){
            if (notifyError){
                //Toast with KO message (Mapped by the server)
                CharSequence errorMessage = response.error_message;
                CMuffin.makeLong(context, errorMessage);
            }

            //Go to onResponseKO()
            onKO(response, httpResponse);
            return;
        }

        //Go to onSuccess()
        onSuccess(response, httpResponse);
    }

    /**
     * Called when KO from server occur
     * If token not valid return to {@link LoginActivity}
     *
     * @param response server response
     * @param httpResponse network response
     */
    private void onKO(BaseResponse<T> response, Response httpResponse) {

        if (response.error_code.equals(MainConfig.ERROR_INVALID_TOKEN)) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            Activity activity = (Activity) context;
            if (!(activity instanceof LoginActivity)) {
                context.startActivity(loginIntent);
                return;
            }
        }

        onResponseKO(response, httpResponse);
    }

    /**
     * Error from Network
     * @param error Network error
     */
    @Override
    public void failure(RetrofitError error) {
        dismissDialog();

        error.printStackTrace(); //to see if you have errors
        Log.d(LOG_TAG, "[Service Call.failure] Call Fail: " + error.getMessage());

        //If notify enabled sho a toast
        if (notifyError)
            CMuffin.makeLong(context, R.string.error_connection);

        //TO-FAIL
        onFail(error);
    }

    /**
     * Dismiss the loading
     */
    private void dismissDialog(){
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (final IllegalArgumentException e){
            //If dialog are dismissed when context doesn't exist anymore
            e.printStackTrace();
        }
    }


    /* Callback To Implement */
    public abstract void onSuccess(BaseResponse<T> response, Response httpResponse);
    public abstract void onResponseKO(BaseResponse<T> response, Response httpResponse);
    public abstract void onFail(RetrofitError error);
}