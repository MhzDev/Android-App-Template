package com.mhzdev.apptemplate.Controller.Login.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhzdev.apptemplate.Controller.Login.Activity.LoginActivity;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Controller.BaseFragment;


public class DownloadFragment extends BaseFragment {

    //Tags
    @SuppressWarnings("unused")
    public static final String TAG = "DownloadFragment";
    @SuppressWarnings("unused")
    public final String LOG_TAG = "DownloadFragment";

    DownloadFragmentListener mCallback;

    public DownloadFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void setListener(DownloadFragmentListener listener){
        mCallback = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //TODO download someting

        // ... When finished ...
        mCallback.onDownloadFinished();
    }

    /**
     * INTERFACE - Callback used by {@link LoginActivity}
     */
    public interface DownloadFragmentListener {
        void onDownloadFinished();
    }
}
