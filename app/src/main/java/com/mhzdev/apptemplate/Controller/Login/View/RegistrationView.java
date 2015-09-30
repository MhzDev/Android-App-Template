package com.mhzdev.apptemplate.Controller.Login.View;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mhzdev.apptemplate.Controller.Login.Fragment.RegistrationFragment;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Utils.CMuffin;
import com.mhzdev.apptemplate.Utils.VerificationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RegistrationView extends RelativeLayout {

    RegistrationViewListener mListener;

    @Bind(R.id.register_email_edit) EditText emailEdit;
    @Bind(R.id.register_password_edit) EditText passwordEdit;
    @Bind(R.id.register_name_edit) EditText nameEdit;

    @Bind(R.id.register_email_edit_inputlayout) TextInputLayout emailInputLayout;
    @Bind(R.id.register_name_edit_inputlayout) TextInputLayout nameInputLayout;
    @Bind(R.id.register_password_edit_inputlayout) TextInputLayout passwordInputLayout;
    @Bind(R.id.register_policy_checkbox) CheckBox registerPolicyCheckBox;

    @Bind(R.id.register_button) Button registerButtonPage1;


    public RegistrationView(Context context) {
        super(context);
        init(context);
    }
    public RegistrationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public RegistrationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.view_registration, this);
        ButterKnife.bind(this, this);

        if(isInEditMode())
            return;

        registerButtonPage1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRegistrationData();
            }
        });
    }

    public void setListener(RegistrationViewListener registrationViewListener){
        mListener = registrationViewListener;
    }

    /**
     * Check if the data are correct and callback the registration fragment to make the call
     */
    private void checkRegistrationData(){
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String name = nameEdit.getText().toString();

        Resources resources = getContext().getResources();

        // Controls
        // Email non vuota
        // Email valida

        // Nome non vuota
        // Nome valido (no caratteri speciali)
        // Nome più lungo di xxx
        // Nome più corto di xxx

        // Passowrd non vuota
        // Password valida
        // Passowrd più lunga di xxx
        // Passowrd più corta di xxx


        //Email empty
        if(email.equals("")){
            emailInputLayout.setError(resources.getString(R.string.registration_error_email_empty));
            return;
        }
        //Email invalid
        if (!VerificationUtils.isValidEmailAddress(email)) {
            emailInputLayout.setError(resources.getString(R.string.registration_error_email_not_valid));
            return;
        }

        emailInputLayout.setError("");

        //Name empty
        if(name.equals("")){
            nameInputLayout.setError(resources.getString(R.string.registration_error_name_empty));
            return;
        }
        //Name not valid (Only char & num)
        if (!VerificationUtils.hasOnlyCharAndNumbers(name)) {
            nameInputLayout.setError(resources.getString(R.string.registration_error_name_not_valid));
            return;
        }
        //Name too short or too long
        if(name.length() < resources.getInteger(R.integer.config_min_lenght_name) || name.length() > resources.getInteger(R.integer.config_max_lenght_name)){
            nameInputLayout.setError(
                    String.format(
                            resources.getString(R.string.registration_error_name_too_short_or_long),
                            resources.getInteger(R.integer.config_min_lenght_name),
                            resources.getInteger(R.integer.config_max_lenght_name)
                    )
            );
            return;
        }

        nameInputLayout.setError("");


        //Password empty
        if(password.equals("")){
            passwordInputLayout.setError(resources.getString(R.string.registration_password_empty));
            return;
        }
        //Password not valid (Only char & num)
        if (!VerificationUtils.hasOnlyCharAndNumbers(password)) {
            passwordInputLayout.setError(resources.getString(R.string.registration_error_password_not_valid));
            return;
        }
        //Password too short or too long
        if(password.length() < resources.getInteger(R.integer.config_min_lenght_password) || password.length() > resources.getInteger(R.integer.config_max_lenght_password)){
            passwordInputLayout.setError(
                   String.format(
                           resources.getString(R.string.registration_error_password_too_short_or_long),
                           resources.getInteger(R.integer.config_min_lenght_password),
                           resources.getInteger(R.integer.config_max_lenght_password)
                   )
            );
            return;
        }

        passwordInputLayout.setError("");

        if(!registerPolicyCheckBox.isChecked()){
            CMuffin.makeLong(getContext(), R.string.registration_must_accept_policy_message);
            return;
        }

        mListener.onRegistrationRequest(email, password, name);
    }


    /**
     * INTERFACE - Callback used by {@link RegistrationFragment}
     */
    public interface RegistrationViewListener{
        void onRegistrationRequest(String email, String password, String name);
    }
}