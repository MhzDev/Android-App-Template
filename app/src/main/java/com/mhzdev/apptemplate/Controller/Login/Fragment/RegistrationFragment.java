package com.mhzdev.apptemplate.Controller.Login.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhzdev.apptemplate.Controller.BaseFragment;
import com.mhzdev.apptemplate.Controller.Login.LoginActivity;
import com.mhzdev.apptemplate.Controller.Login.View.RegistrationView;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Services.API.BaseResponse;
import com.mhzdev.apptemplate.Services.API.Call.RegistrationAPI;
import com.mhzdev.apptemplate.Services.API.Response.RegistrationResponse;
import com.mhzdev.apptemplate.Services.ApiAdapterBuilder;
import com.mhzdev.apptemplate.Services.ApiCallback;
import com.mhzdev.apptemplate.Services.ApiList;
import com.mhzdev.apptemplate.Session.UserSessionManager;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class RegistrationFragment extends BaseFragment {

    //Tags
    @SuppressWarnings("unused")
    public static final String TAG = "RegistrationFragment";
    @SuppressWarnings("unused")
    public final String LOG_TAG = "RegistrationFragment";

    //Callback
    private RegistrationFragmentListener mCallback;

    public RegistrationFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RegistrationView registrationView = (RegistrationView) view.findViewById(R.id.registration_view);
        registrationView.setListener(new RegistrationViewListener());
    }

    public void setListener(RegistrationFragmentListener listener){
        mCallback = listener;
    }


    private void doRegistration(String email, String password, String name) {

        //Do the Registration
        ApiList ApiList = ApiAdapterBuilder.getApiAdapter();

        RegistrationAPI registrationAPI = new RegistrationAPI(getActivity());
        registrationAPI.user_email = email;
        registrationAPI.user_password = password;
        registrationAPI.username = name;

        UserSessionManager.getInstance(getActivity()).saveUserData(name);

        ApiList.register(registrationAPI, new RegistrationCallback(getActivity(), true, true));
    }


    /**
     * INTERFACE - Callback used by {@link LoginActivity}
     */
    public interface RegistrationFragmentListener {
        void onUserRegistered();
    }

    /** ***************************
     *  Registration Api - Callback
     * *****************************
     */
    private class RegistrationCallback extends ApiCallback<RegistrationResponse> {
        public RegistrationCallback(Context context, boolean requestLoading, boolean notifyErrors) {
            super(context, requestLoading, notifyErrors);
        }
        @Override
        public void onSuccess(BaseResponse<RegistrationResponse> response, Response baseResponse) {
            UserSessionManager.getInstance(getActivity()).clearUserData(getActivity());

            //Save the token
            String newToken = response.response.token;
            UserSessionManager.getInstance(getActivity()).setToken(newToken);

            String userName = response.response.user_name;
            UserSessionManager.getInstance(getActivity()).saveUserData(userName);

            mCallback.onUserRegistered();
        }
        @Override public void onResponseKO(BaseResponse<RegistrationResponse> response, Response baseResponse) {}
        @Override public void onFail(RetrofitError error) {}
    }

    /** ***************************
     *  RegistrationView - Callback
     * *****************************
     */
    private class RegistrationViewListener implements RegistrationView.RegistrationViewListener{

        @Override
        public void onRegistrationRequest(String email, String password, String name) {
            doRegistration(email, password, name);
        }
    }
}
