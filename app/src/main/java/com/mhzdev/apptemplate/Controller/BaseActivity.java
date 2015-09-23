package com.mhzdev.apptemplate.Controller;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.mhzdev.apptemplate.Controller.Login.Fragment.RegistrationFragment;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Utils.CMuffin;


public class BaseActivity extends AppCompatActivity {


    private boolean backClickedRecently;

    protected void plainToCloseApp() {
        if (backClickedRecently) {
            super.onBackPressed();
            return;
        }
        backClickedRecently = true;
        CMuffin.makeLong(this, R.string.back_button_warning);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backClickedRecently = false;
            }
        }, 2000);
    }


    /** ***************************
     *  xxxFragment - Callback
     * *****************************
     */

    /** ****************************
     *  xxx Api - Callback
     * *****************************
     */

    /** ----------------------------------------------------->
     * -------------- Callbacks ----------------------------->
     * ------------------------------------------------------>
     */

    /**
     * INTERFACE - Callback used by {@link RegistrationFragment}
     */
}
