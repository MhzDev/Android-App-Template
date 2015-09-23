package com.mhzdev.apptemplate.Controller.Tutorial;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Session.UserSessionManager;


public class TutorialActivity extends AppIntro2 {

    @Override
    public void init(Bundle bundle) {
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.app_name),
                getString(R.string.generic_subtitle),
                R.mipmap.ic_launcher,
                getResources().getColor(R.color.accent)));

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.app_name),
                getString(R.string.generic_subtitle),
                R.mipmap.ic_launcher,
                getResources().getColor(R.color.accent)));

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.app_name),
                getString(R.string.generic_subtitle),
                R.mipmap.ic_launcher,
                getResources().getColor(R.color.accent)));

    }

    @Override
    public void onDonePressed() {
        UserSessionManager.getInstance(this).setTutorialDone();
        finish();
    }

}
