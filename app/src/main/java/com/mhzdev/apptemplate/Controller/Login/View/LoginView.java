package com.mhzdev.apptemplate.Controller.Login.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mhzdev.apptemplate.Controller.Login.Fragment.LoginFragment;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Utils.CMuffin;
import com.mhzdev.apptemplate.Utils.VerificationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginView extends RelativeLayout {

    LoginViewListener mListener;

    @Bind(R.id.email_edit) EditText emailEdit;
    @Bind(R.id.password_edit) EditText passwordEdit;
    @Bind(R.id.login_button) Button loginButton;
    @Bind(R.id.login_facebook_button) Button loginFacebookButton;
    @Bind(R.id.register_button) Button registerButton;
    @Bind(R.id.skip_login_button) Button skipButton;
    @Bind(R.id.help_button) Button helpButton;

    public LoginView(Context context) {
        super(context);
        init(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.view_login, this);
        ButterKnife.bind(this, this);

        if(isInEditMode())
            return;


        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLoginData();
            }
        });

        loginFacebookButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLoginFacebookClick();
            }
        });

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRegisterClick();
            }
        });

        skipButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSkipLoginClick();
            }
        });

        helpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onForgetPasswordClick();
            }
        });

    }

    public void setListener(LoginViewListener loginViewListener){
        mListener = loginViewListener;
    }

    /**
     * Check if the data are correct and callback to the login fragment to make the call
     */
    private void checkLoginData(){
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        //Email empty
        if(email.equals("")){
            CMuffin.makeLong(getContext(), R.string.login_invalid_data_message);
            return;
        }
        //Email invalid
        if (!VerificationUtils.isValidEmailAddress(email)) {
            CMuffin.makeLong(getContext(), R.string.login_invalid_data_message);
            return;
        }
        //Password empty
        if(password.equals("")){
            CMuffin.makeLong(getContext(), R.string.login_invalid_data_message);
            return;
        }

        mListener.onLogin(email, password);
    }

    /**
     * INTERFACE - Callback used by {@link LoginFragment}
     */
    public interface LoginViewListener{
        void onLogin(String email, String password);
        void onLoginFacebookClick();
        void onRegisterClick();
        void onSkipLoginClick();
        void onForgetPasswordClick();
    }
}