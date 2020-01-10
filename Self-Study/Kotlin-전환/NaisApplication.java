package com.esumtech.naisvn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.esumtech.naisvn.config.Constants;
import com.esumtech.naisvn.content.NaisServiceReceiver;
import com.esumtech.naisvn.network.TokenManager;
import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import io.fabric.sdk.android.Fabric;

public class NaisApplication extends MultiDexApplication {

    private static final String TAG = NaisApplication.class.getSimpleName();

    private static Context mContext;

    private FirebaseAnalytics mFirebaseAnalytics;

    private TokenManager mTokenManager;

    private final String FIREBASE_AUTHORIZATION_TOKEN = "FIREBASE_AUTHORIZATION_TOKEN";
    private final String FIREBASE_AUTHORIZATION = "FIREBASE_AUTHORIZATION";


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        BroadcastReceiver mNaisReceiver = new NaisServiceReceiver();
        registerReceiver(mNaisReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));

        mTokenManager = TokenManager.getInstance();

        mContext = getApplicationContext();

        Stetho.initializeWithDefaults(this);

        Fabric.with(this, new Crashlytics());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("NaisApplication", "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    final SharedPreferences pref = NaisApplication.getAppContext().getSharedPreferences(FIREBASE_AUTHORIZATION, Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = pref.edit();
                    editor.putString(FIREBASE_AUTHORIZATION_TOKEN, token);
                    editor.commit();

                    // Log and toast
                    Log.d("NaisApplication", token);
                });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * Context 를 가져온다
     *
     * @return
     */
    public static Context getAppContext() {
        return mContext;
    }
}

