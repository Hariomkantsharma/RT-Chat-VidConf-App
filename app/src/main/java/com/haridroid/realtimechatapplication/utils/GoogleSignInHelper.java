package com.haridroid.realtimechatapplication.utils;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.haridroid.realtimechatapplication.R;

public class GoogleSignInHelper {
    private static GoogleSignInHelper instance;
    private GoogleSignInClient googleSignInClient;

    private GoogleSignInHelper(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public static synchronized GoogleSignInHelper getInstance(Context context) {
        if (instance == null) {
            instance = new GoogleSignInHelper(context);
        }
        return instance;
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }
}