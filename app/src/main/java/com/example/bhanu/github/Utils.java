package com.example.bhanu.github;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

import com.example.bhanu.github.network.model.Token;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Utils {

    public static String getToken(Context context){
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.token_sharedpref), Context.MODE_PRIVATE);
        return settings.getString(context.getString(R.string.github_access_token), "");
    }

    public static void saveToken(Context context, Token token) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.token_sharedpref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.github_access_token), token.getAccess_token());
        editor.commit();
    }


    public static void saveState(Context context, String  state) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.token_sharedpref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.state), state);
        editor.commit();
    }


    public static String getState(Context context){
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.token_sharedpref), Context.MODE_PRIVATE);
        return settings.getString(context.getString(R.string.state), "");
    }

    public static String generateUniqueString(Context context) {
        SecureRandom random = new SecureRandom();
        String state =  new BigInteger(130, random).toString(32);
        saveState(context, state);
        return state;
    }
}
