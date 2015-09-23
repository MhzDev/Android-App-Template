package com.mhzdev.apptemplate.Controller;


import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    /**
     * Close the fragment by the tag passed (only if attacched)
     * @param fragmentTag
     */
    protected void closeFragmentByTag(String fragmentTag) {
        if(getActivity()!=null)
            if(getActivity().getSupportFragmentManager().findFragmentByTag(fragmentTag)!=null)
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
