package com.example.kanitkar.biig.classes;

import android.content.SharedPreferences;

import com.example.kanitkar.biig.api.Model.User;
import com.google.gson.Gson;

/**
 * Created by Kanitkar on 22-03-2018.
 */
public class Session {
    private static Session instance = null;
    public static final String PREFS_NAME = "UserProfile";
    private User userInfo;
    SharedPreferences sp;
    protected Session() {
        // Exists only to defeat instantiation.
    }

    public static Session getInstance() {
        if(instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public boolean isUserInSession(SharedPreferences settings){
        String userJson = settings.getString("userdata", "");
        userInfo = new Gson().fromJson(userJson, User.class);
        return userInfo != null;
    }

    public void clearSession(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public User getUserInfo(SharedPreferences settings) {
        String userJosn = settings.getString("userdata", "");
        userInfo = new Gson().fromJson(userJosn, User.class);
        return userInfo;
    }

    public void setUserInfo(User user, SharedPreferences settings) {
        SharedPreferences.Editor prefsEditor = settings.edit();
        String json = new Gson().toJson(user);
        prefsEditor.putString("userdata", json);
        prefsEditor.commit();
    }
}
