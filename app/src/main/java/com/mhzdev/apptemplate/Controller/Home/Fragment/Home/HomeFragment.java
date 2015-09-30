package com.mhzdev.apptemplate.Controller.Home.Fragment.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhzdev.apptemplate.Controller.BaseFragment;
import com.mhzdev.apptemplate.Controller.Home.HomeActivity;
import com.mhzdev.apptemplate.R;


public class HomeFragment extends BaseFragment {
    //Tags
    @SuppressWarnings("unused")
    public static final String TAG = "HomeFragment";
    @SuppressWarnings("unused")
    public final String LOG_TAG = "HomeFragment";
    private HomeFragmentListener mListener;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setListener(HomeFragmentListener homeFragmentListener) {
        mListener = homeFragmentListener;
    }

    /**
     * INTERFACE - Callback used by {@link HomeActivity}
     */
    public interface HomeFragmentListener {
    }

}

