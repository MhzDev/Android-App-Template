package com.mhzdev.apptemplate.Controller.Login.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mhzdev.apptemplate.Controller.BaseFragment;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Services.API.BaseResponse;
import com.mhzdev.apptemplate.Services.API.Call.RecoverPasswordAPI;
import com.mhzdev.apptemplate.Services.API.Response.RecoverPasswordResponse;
import com.mhzdev.apptemplate.Services.ApiAdapterBuilder;
import com.mhzdev.apptemplate.Services.ApiCallback;
import com.mhzdev.apptemplate.Services.ApiList;
import com.mhzdev.apptemplate.Utils.CMuffin;
import com.mhzdev.apptemplate.Utils.GenericUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PasswordRecoverFragment extends BaseFragment {

    //Tags
    @SuppressWarnings("unused")
    public static final String TAG = "PasswordRecoverFragment";
    @SuppressWarnings("unused")
    public final String LOG_TAG = "PasswordRecoverFragment";


    @Bind(R.id.recovery_email_edit) EditText emailTextView;
    @Bind(R.id.recovery_email_edit_container)
    TextInputLayout emailTextViewContainer;
    @Bind(R.id.recovery_continue_button) Button continueButton;

    public PasswordRecoverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recover_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRecoverPasswordApi();
            }
        });
    }

    /**
     * Call the RecoveryPassword Api
     */
    private void callRecoverPasswordApi(){
        String email = emailTextView.getText().toString();

        //Check email
        if(!GenericUtils.isValidEmailAddress(email)){
            emailTextViewContainer.setError(getActivity().getString(R.string.registration_error_email_not_valid));
            return;
        }

        //Do the call
        ApiList ApiList = ApiAdapterBuilder.getApiAdapter();

        RecoverPasswordAPI recoverPasswordAPI = new RecoverPasswordAPI(getActivity());
        recoverPasswordAPI.user_email = email;

        ApiList.recoverPassword(recoverPasswordAPI, new RecoverPasswordCallback(getActivity(), true, true));
    }


    /** ****************************
     *  RecoverPassword Api - Callback
     * *****************************
     */
    private class RecoverPasswordCallback extends ApiCallback<RecoverPasswordResponse> {
        public RecoverPasswordCallback(Context context, boolean requestLoading, boolean notifyErrors) {
            super(context, requestLoading, notifyErrors);
        }
        @Override
        public void onSuccess(BaseResponse<RecoverPasswordResponse> response, Response baseResponse) {
            //Notify success
            CMuffin.makeLong(getActivity(), R.string.recovery_success_message);

            //Close the fragment
            getActivity().onBackPressed();
            //closeFragmentByTag(PasswordRecoverFragment.TAG);
        }
        @Override public void onResponseKO(BaseResponse<RecoverPasswordResponse> response, Response baseResponse) {}
        @Override public void onFail(RetrofitError error) {}
    }
}
