package com.mhzdev.apptemplate.Session;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.mhzdev.apptemplate.Config.MainConfig;
import com.mhzdev.apptemplate.Config.SharedPref;

import java.util.Locale;

public class UserSessionManager {

    private static UserSessionManager mInstance;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private UserSessionManager(Context context) {
        sharedPreferences = getSharedPref(context);
        sharedPreferencesEditor = getSharedPrefEditor(context);
    }

    public static UserSessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = initUserSessionManager(context);
        }
        return mInstance;
    }

    private static UserSessionManager initUserSessionManager(Context context){
        return new UserSessionManager(context);
    }

    public SharedPreferences getSharedPref(Context context){
        return context.getSharedPreferences(MainConfig.SHARED_PREFERENCES, Activity.MODE_PRIVATE);
    }
    public SharedPreferences.Editor getSharedPrefEditor(Context context){
        return context.getSharedPreferences(MainConfig.SHARED_PREFERENCES, Activity.MODE_PRIVATE).edit();
    }




    /** ----------------------------------------------------->
     * -------------- Getter ----------------------------->
     * ------------------------------------------------------>
     */
    public String getLang() {
        //If not present create a new lang based from the current
        if (sharedPreferences.getString(SharedPref.USER_LANG,"").equals("")){
            String deviceLang = Locale.getDefault().toString();
            sharedPreferencesEditor.putString(SharedPref.USER_LANG, deviceLang).apply();
            return deviceLang;
        }
        //If present return the current lang
        return sharedPreferences.getString(SharedPref.USER_LANG, "");
    }
    public boolean isTutorialDone() {
        return sharedPreferences.getBoolean(SharedPref.TUTORIAL_DONE, false);
    }
    public boolean isLogged(){
        String token = sharedPreferences.getString(SharedPref.TOKEN, "");
        return !token.equals("");
    }
    public void setLang(String newLang) {
        sharedPreferencesEditor.putString(SharedPref.USER_LANG, newLang).apply();
    }
    public String getToken() {
        return sharedPreferences.getString(SharedPref.TOKEN, "");
    }
    public String getUserName() {
        return sharedPreferences.getString(SharedPref.USER_NAME, "User");
    }




    /** ----------------------------------------------------->
     * -------------- Setter ----------------------------->
     * ------------------------------------------------------>
     */
    public void setTutorialDone() {
        sharedPreferencesEditor.putBoolean(SharedPref.TUTORIAL_DONE, true).apply();
    }

    /**
     * Save the token
     * @param token token
     */
    public void setToken(String token){
        sharedPreferencesEditor.putString(SharedPref.TOKEN, token).apply();
    }

    /**
     * Save User name & sex
     * @param name name
     */
    public void saveUserData(String name){
        sharedPreferencesEditor.putString(SharedPref.USER_NAME, name).apply();
    }



    /** ----------------------------------------------------->
     * -------------- Actions ----------------------------->
     * ------------------------------------------------------>
     */
    /**
     * Clear Token And user data
     * @param context context
     */
    public void logOut(Context context){
        sharedPreferencesEditor.clear().commit();
        setTutorialDone();
        clearUserData(context);
    }
    /**
     * Clear the user data
     * @param context context
     */
    public void clearUserData(Context context){


        //TODO Clear the DataBase DB
    }
}
