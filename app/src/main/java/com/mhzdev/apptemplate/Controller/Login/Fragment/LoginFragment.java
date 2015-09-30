package com.mhzdev.apptemplate.Controller.Login.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mhzdev.apptemplate.Controller.BaseFragment;
import com.mhzdev.apptemplate.Controller.Login.LoginActivity;
import com.mhzdev.apptemplate.Controller.Login.View.LoginView;
import com.mhzdev.apptemplate.Model.FacebookUser;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Services.API.BaseResponse;
import com.mhzdev.apptemplate.Services.API.Call.FbAccessAPI;
import com.mhzdev.apptemplate.Services.API.Call.LoginAPI;
import com.mhzdev.apptemplate.Services.API.Response.FbAccessResponse;
import com.mhzdev.apptemplate.Services.API.Response.LoginResponse;
import com.mhzdev.apptemplate.Services.ApiAdapterBuilder;
import com.mhzdev.apptemplate.Services.ApiCallback;
import com.mhzdev.apptemplate.Services.ApiList;
import com.mhzdev.apptemplate.Session.UserSessionManager;
import com.mhzdev.apptemplate.Utils.CMuffin;
import com.mhzdev.apptemplate.Utils.ConnectionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginFragment extends BaseFragment {

    //Tags
    public static final String TAG = "LoginFragment";
    @SuppressWarnings("unused")
    private final String LOG_TAG = this.getClass().getSimpleName();

    //Facebook Login
    private LoginManager loginManager;
    private CallbackManager callbackManager;



    //Callback
    private LoginFragmentListener mCallback;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set the view
        LoginView loginView = (LoginView) view.findViewById(R.id.login_view);
        loginView.setListener(new LoginViewListener());

        inizializeFbLogin();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //For Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void setListener(LoginFragmentListener listener){
        mCallback = listener;
    }

    /**
     * Initialing the Facebook Login
     */
    private void inizializeFbLogin(){
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();

        //Add callback
        LoginManager.getInstance().registerCallback(callbackManager, new FbLoginNativeCallback());
        loginManager = LoginManager.getInstance();
    }


    /**
     * Call the login api
     *
     * @param email The user email
     * @param password The user password
     */
    private void callLoginApi(String email, String password){
        if(!ConnectionUtil.isConnectionAvailable(getActivity())){
            CMuffin.makeLong(LoginFragment.this.getActivity(), R.string.error_connection_off);
            return;
        }

        //Do the login call
        ApiList ApiList = ApiAdapterBuilder.getApiAdapter();

        LoginAPI loginAPI = new LoginAPI(getActivity());
        loginAPI.user_email = email;
        loginAPI.user_password = password;

        ApiList.login(loginAPI, new LoginCallback(getActivity(), true, true));
    }

    /**
     * Skip the login - communicate to login activity
     */
    private void skipLogin(){
        mCallback.onSkipLogin();
    }

    /**
     * Start the Login via FB
     */
    private void doFacebookLogin(){
        // --- Facebook Login flow ---
        // 1. Try to login to Facebook
        // 2. Call facebook for additional info
        // 3. Pass to our server the data
        // 4. Communicate to activity login done

        //If connection not available make a muffin
        if(!ConnectionUtil.isConnectionAvailable(getActivity())){
            CMuffin.makeLong(LoginFragment.this.getActivity(), R.string.error_connection_off);
            return;
        }

        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    /**
     * call the FacebookRegistration Api
     */
    private void callFacebookRegistrationApi(FacebookUser facebookUser) {
        ApiList ApiList = ApiAdapterBuilder.getApiAdapter();

        FbAccessAPI fbAccessAPI = new FbAccessAPI(getActivity());
        fbAccessAPI.user_email = facebookUser.getEmail();
        fbAccessAPI.username = facebookUser.getEmail();
        fbAccessAPI.token = facebookUser.getToken();
        fbAccessAPI.user_id = facebookUser.getFbUserId();

        ApiList.fbAccess(fbAccessAPI, new FbAccessCallback(getActivity(), true, true));
    }


    /**
     * INTERFACE - Callback used by {@link LoginActivity}
     */
    public interface LoginFragmentListener {
        void onUserLogged();

        void onRequestRegistration();

        void onForgetPassword();

        void onSkipLogin();
    }

    /** ****************************
     *  Login Api - Callback
     * *****************************
     */
    private class LoginCallback extends ApiCallback<LoginResponse> {

        public LoginCallback(Context context, boolean requestLoading, boolean notifyErrors) {
            super(context, requestLoading, notifyErrors);
        }

        @Override
        public void onSuccess(BaseResponse<LoginResponse> response, Response baseResponse) {
            //Save the token
            String newToken = response.response.token;
            UserSessionManager.getInstance(getActivity()).setToken(newToken);

            String userName = response.response.user_name;
            UserSessionManager.getInstance(getActivity()).saveUserData(userName);

            //Successful token verification
            mCallback.onUserLogged();
        }
        @Override
        public void onResponseKO(BaseResponse<LoginResponse> response, Response baseResponse) {}
        @Override
        public void onFail(RetrofitError error) {}
    }

    /** ****************************
     *  Facebook register Api - Callback
     * *****************************
     */
    private class FbAccessCallback extends ApiCallback<FbAccessResponse> {
        public FbAccessCallback(Context context, boolean requestLoading, boolean notifyErrors) {
            super(context, requestLoading, notifyErrors);
        }
        @Override
        public void onSuccess(BaseResponse<FbAccessResponse> response, Response baseResponse) {
            String name = response.response.user_name;
            String token = response.response.token;

            //Save data & Token
            UserSessionManager.getInstance(getActivity()).setToken(token);
            UserSessionManager.getInstance(getActivity()).saveUserData(name);

            mCallback.onUserLogged();
        }
        @Override
        public void onResponseKO(BaseResponse<FbAccessResponse> response, Response baseResponse) {}
        @Override
        public void onFail(RetrofitError error) {}
    }

    /** ****************************
     *  Facebook Native SDK Api - Callback
     * *****************************
     */
    private class FbLoginNativeCallback implements FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(final LoginResult loginResult) {
            //Request Additional Data
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            //Get the additional data parameter
                            try {
                                String email = object.getString("email");
                                String name = object.getString("name");
                                String userId = object.getString("id");
                                String token = loginResult.getAccessToken().getToken();

                                FacebookUser facebookUser = new FacebookUser();
                                facebookUser.setEmail(email);
                                facebookUser.setUsername(name);
                                facebookUser.setFbUserId(userId);
                                facebookUser.setFbToken(token);

                                callFacebookRegistrationApi(facebookUser);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                CMuffin.makeLong(getActivity(), R.string.error_connection);
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }
        @Override
        public void onCancel() {
            CMuffin.makeLong(getActivity(), R.string.error_connection);
        }
        @Override
        public void onError(FacebookException exception) {
            CMuffin.makeLong(getActivity(), exception.getMessage());
        }
    }

    /** ****************************
     *  LoginView - Callback
     * *****************************
     */
    private class LoginViewListener implements LoginView.LoginViewListener {
        @Override
        public void onLogin(String email, String password) {
            callLoginApi(email, password);
        }
        @Override
        public void onLoginFacebookClick() {
            doFacebookLogin();
        }
        @Override
        public void onRegisterClick() {
            mCallback.onRequestRegistration();
        }
        @Override
        public void onSkipLoginClick() {
            skipLogin();
        }
        @Override
        public void onForgetPasswordClick() {
            mCallback.onForgetPassword();
        }
    }
}
