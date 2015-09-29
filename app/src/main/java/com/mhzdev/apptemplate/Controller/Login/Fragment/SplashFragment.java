package com.mhzdev.apptemplate.Controller.Login.Fragment;

import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Config.MainConfig;
import com.mhzdev.apptemplate.Controller.BaseFragment;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class SplashFragment extends BaseFragment {

    //Tags
    @SuppressWarnings("unused")
    public static final String TAG = "SplashFragment";
    @SuppressWarnings("unused")
    public final String LOG_TAG = "SplashFragment";
    private View view;

    public SplashFragment() {
    }

    long splashStartTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashStartTime = System.currentTimeMillis();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.loading_animation);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.md_white),
                PorterDuff.Mode.SRC_IN);

        String versionName = "";
        PackageInfo pInfo;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView versionCode = (TextView) view.findViewById(R.id.version_code);
        versionCode.setText(versionName);
    }

    /**
     * Request the close of the splash
     * if elapsed time finished close immediately else wait the time remaining
     */
    public void requestClose() {
        long currentTime = System.currentTimeMillis();
        long splashElapsedTime = currentTime - splashStartTime;

        //Close now
        if (splashElapsedTime > MainConfig.SPLASH_DURATION) {
            closeSplash();
        } else {
            //Wait the remaining time
            Handler mHandler = new Handler();
            Runnable mRunnable = new Runnable() {
                @Override
                public void run() {
                    if(getActivity() == null)
                        return;

                    closeSplash();
                }
            };
            long remainingTime = MainConfig.SPLASH_DURATION - splashElapsedTime;
            mHandler.postDelayed(mRunnable, remainingTime);
        }
    }

    public void closeSplash(){
        View revealLayout = view.findViewById(R.id.reveal_layout);
        revealLayout.setVisibility(View.VISIBLE);

        // get the center for the clipping circle
        int cx = (revealLayout.getLeft() + revealLayout.getRight()) / 2;
        int cy = (revealLayout.getTop() + revealLayout.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(revealLayout.getWidth(), revealLayout.getHeight());

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(revealLayout, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1500);
        animator.start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeFragmentByTag(SplashFragment.TAG);
            }
        }, 1500);
    }

    public void forceClose(){
        closeFragmentByTag(SplashFragment.TAG);
    }
}

