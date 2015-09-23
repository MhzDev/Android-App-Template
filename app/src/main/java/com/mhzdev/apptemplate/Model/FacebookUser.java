package com.mhzdev.apptemplate.Model;

/**
 * Created by Michele Stefanelli (MhzDev) on 14/09/2015.
 */
public class FacebookUser extends User {
    private String fbUserId;
    private String fbToken;

    public String getFbUserId() {
        return fbUserId;
    }

    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }
}
