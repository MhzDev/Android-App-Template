package com.mhzdev.apptemplate.Controller.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.facebook.FacebookSdk;
import com.mhzdev.apptemplate.Controller.BaseActivity;
import com.mhzdev.apptemplate.Controller.Home.HomeActivity;
import com.mhzdev.apptemplate.Controller.Login.Fragment.DownloadFragment;
import com.mhzdev.apptemplate.Controller.Login.Fragment.LoginFragment;
import com.mhzdev.apptemplate.Controller.Login.Fragment.PasswordRecoverFragment;
import com.mhzdev.apptemplate.Controller.Login.Fragment.RegistrationFragment;
import com.mhzdev.apptemplate.Controller.Login.Fragment.SplashFragment;
import com.mhzdev.apptemplate.Controller.Tutorial.TutorialActivity;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Services.API.BaseResponse;
import com.mhzdev.apptemplate.Services.API.Call.AutoLoginAPI;
import com.mhzdev.apptemplate.Services.API.Response.TokenResponse;
import com.mhzdev.apptemplate.Services.ApiAdapterBuilder;
import com.mhzdev.apptemplate.Services.ApiCallback;
import com.mhzdev.apptemplate.Services.ApiList;
import com.mhzdev.apptemplate.Session.UserSessionManager;
import com.mhzdev.apptemplate.Utils.ConnectionUtil;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends BaseActivity {

    //Tags
    @SuppressWarnings("unused")
    public final String TAG = "LoginActivity";
    @SuppressWarnings("unused")
    public final String LOG_TAG = "LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        /* Facebook SKD */
        FacebookSdk.sdkInitialize(getApplicationContext());

        //If first time open the tutorial fragment
        if(!UserSessionManager.getInstance(this).isTutorialDone()){
            openLoginPageFragment();
            openTutorialActivity();
            return;
        }

        openSplashPageFragment();

        //If user has a token try the autoLogin Else open the Login page
        if (UserSessionManager.getInstance(this).isLogged()) {
            Log.d(LOG_TAG, "Try autoLogin");
            tryAutoLogin();
            return;
        }

        Log.d(LOG_TAG, "Not logged, open login page");
        openLoginPageFragment();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            super.onBackPressed();
            return;
        }

        plainToCloseApp();
    }

    /**
     * Try the auto login with the saved token
     */
    private void tryAutoLogin(){
        Log.d(LOG_TAG, "[tryAutoLogin] Trying auto login");

        if(!ConnectionUtil.isConnectionAvailable(this)){
            Log.d(LOG_TAG, "[tryAutoLogin] Connection not available");
            closeLoginActivity();
            //Connection not available go to home
            return;
        }

        //Get token
        String token = UserSessionManager.getInstance(this).getToken();

        //Try to auto login
        ApiList ApiList = ApiAdapterBuilder.getApiAdapter();
        AutoLoginAPI autoLoginAPI = new AutoLoginAPI(this);
        autoLoginAPI.token = token;

        ApiList.autoLogin(autoLoginAPI, new AutoLoginCallback(this, false, false));
    }

    /** ----------------------------------------------------->
     * -------------- Fragment Opener Methods -------------->
     * ------------------------------------------------------>
     */

    /**
     * Open SplashFragment (at the launch of the app)
     */
    private void openSplashPageFragment(){
        SplashFragment splashFragment = new SplashFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.splash_fragment_container, splashFragment, SplashFragment.TAG).commit();

        //Force Splash Fragment commit
        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * Open TutorialFragment (If is the first time)
     */
    private void openTutorialActivity() {
        Intent myIntent = new Intent(this, TutorialActivity.class);
        startActivity(myIntent);
    }

    /**
     * Open LoginFragment (When is needed a manual login)
     */
    private void openLoginPageFragment(){
        requestSplashPageFragmentClosing();

        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loginFragment, LoginFragment.TAG)
                .commit();
        loginFragment.setListener(new LoginFragmentListener());
    }

    /**
     * Open RegistrationFragment (Triggered by user in login page)
     */
    private void openRegistrationFragment(){
        RegistrationFragment registrationFragment = new RegistrationFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit,
                        R.anim.activity_close_enter, R.anim.activity_close_exit)
                .add(R.id.fragment_container, registrationFragment, RegistrationFragment.TAG)
                .addToBackStack(RegistrationFragment.TAG)
                .commit();
        registrationFragment.setListener(new RegistrationFragmentListener());
    }

    /**
     * Open the download fragment (When finished, the callback will close the login activity)
     */
    private void openDownloadFragment() {
        requestSplashPageFragmentClosing();

        DownloadFragment downloadFragment = new DownloadFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, downloadFragment, DownloadFragment.TAG)
                .commit();
        downloadFragment.setListener(new DownloadFragmentListener());
    }

    /**
     * Open the download fragment (The callback will close the login activity)
     */
    private void openPasswordRecoverFragment() {
        PasswordRecoverFragment passwordRecoverFragment = new PasswordRecoverFragment();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit,
                        R.anim.activity_close_enter, R.anim.activity_close_exit)
                .replace(R.id.fragment_container, passwordRecoverFragment, PasswordRecoverFragment.TAG)
                .addToBackStack(PasswordRecoverFragment.TAG)
                .commit();
    }



    /** ----------------------------------------------------->
     * -------------- Other Methods -------------->
     * ------------------------------------------------------>
     */

    /**
     * Close the Splash Fragment after minimum xx seconds (Depending by Config.SPLASH_DURATION)
     */
    private void requestSplashPageFragmentClosing(){
        SplashFragment splashFragment = (SplashFragment) getSupportFragmentManager().findFragmentById(R.id.splash_fragment_container);
        if(splashFragment!=null)
            splashFragment.requestClose();
    }

    /**
     * If connection is available open download fragment
     */
    private void downloadContent(){
        if(ConnectionUtil.isConnectionAvailable(this) && UserSessionManager.getInstance(this).isLogged())
            openDownloadFragment();
        else
            closeLoginActivity();
    }

    /**
     * Close the login - and go to the home
     */
    private void closeLoginActivity(){
        Intent homeActivity = new Intent(this, HomeActivity.class);
        startActivity(homeActivity);
        finish();
    }


    /** ----------------------------------------------------->
     * -------------- Callbacks ----------------------------->
     * ------------------------------------------------------>
     */

    /** ****************************
     *  AutoLoginFragment - Callback
     * *****************************
     */
    private class AutoLoginCallback extends ApiCallback<TokenResponse> {
        public AutoLoginCallback(Context context, boolean requestLoading, boolean notifyErrors) {
            super(context, requestLoading, notifyErrors);
        }
        @Override
        public void onSuccess(BaseResponse<TokenResponse> response, Response baseResponse) {
            //Successful token verification
            //Download the content (After will close the login activity)
            UserSessionManager.getInstance(LoginActivity.this).setToken(response.response.token);
            downloadContent();
        }
        @Override
        public void onResponseKO(BaseResponse<TokenResponse> response, Response baseResponse) {
            //Logout
            UserSessionManager.getInstance(LoginActivity.this).logOut(LoginActivity.this);
            openLoginPageFragment();
        }
        @Override
        public void onFail(RetrofitError error) {
            //No response, connection problem, go to home
            closeLoginActivity();
        }
    }

    /** ****************************
     *  LoginFragment - Callback
     * *****************************
     */
    private class LoginFragmentListener implements LoginFragment.LoginFragmentListener {
        @Override
        public void onUserLogged() {
            downloadContent();
        }

        @Override
        public void onRequestRegistration() {
            openRegistrationFragment();
        }

        @Override
        public void onForgetPassword() {
            openPasswordRecoverFragment();
        }

        @Override
        public void onSkipLogin() {
            closeLoginActivity();
        }

    }


    /** ****************************
     *  RegistrationFragment - Callback
     * *****************************
     */
    private class RegistrationFragmentListener implements RegistrationFragment.RegistrationFragmentListener{
        @Override
        public void onUserRegistered() {
            getSupportFragmentManager().popBackStack();
            downloadContent();
        }
    }

    /** ****************************
     *  DownloadFragment - Callback
     * *****************************
     */
    private class DownloadFragmentListener implements DownloadFragment.DownloadFragmentListener{
        @Override
        public void onDownloadFinished() {
            //Go to the home
            closeLoginActivity();
        }
    }
}