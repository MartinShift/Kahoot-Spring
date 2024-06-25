package com.example.kahoot_front.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kahoot_front.model.User;
import com.google.gson.Gson;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_CURRENT_USER = "current_user";

    private static SharedPrefManager instance;
    private final SharedPreferences sharedPreferences;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveCurrentUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(KEY_CURRENT_USER, json);
        editor.apply();
    }

    public User getCurrentUser() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_CURRENT_USER, null);
        return gson.fromJson(json, User.class);
    }

    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_CURRENT_USER);
        editor.apply();
    }
}