package com.esumtech.naisvn

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

import com.crashlytics.android.Crashlytics
import com.esumtech.naisvn.content.NaisServiceReceiver
import com.esumtech.naisvn.network.TokenManager
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId

import io.fabric.sdk.android.Fabric

class NaisApplication : MultiDexApplication() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private var mTokenManager: TokenManager? = null

    private val FIREBASE_AUTHORIZATION_TOKEN = "FIREBASE_AUTHORIZATION_TOKEN"
    private val FIREBASE_AUTHORIZATION = "FIREBASE_AUTHORIZATION"

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        val mNaisReceiver = NaisServiceReceiver()
        registerReceiver(mNaisReceiver, IntentFilter(Intent.ACTION_USER_PRESENT))

        mTokenManager = TokenManager.getInstance()

        appContext = applicationContext

        Stetho.initializeWithDefaults(this)

        Fabric.with(this, Crashlytics())
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("NaisApplication", "getInstanceId failed", task.exception)
                        return@addOnCompleteListener
                    }
                    // Get new Instance ID token
                    val token = task.result!!.token

                    val pref = appContext!!.getSharedPreferences(FIREBASE_AUTHORIZATION, Context.MODE_PRIVATE)
                    val editor = pref.edit()
                    editor.putString(FIREBASE_AUTHORIZATION_TOKEN, token)
                    editor.apply()

                    // Log and toast
                    Log.d("NaisApplication", token)
                }
    }

    companion object {

        private val TAG = NaisApplication::class.java.simpleName

        /**
         * Context 를 가져온다
         *
         * @return
         */
        var appContext: Context? = null
            private set
        // Setter는 NaisApplication.kt에서만 접근 가능
    }
}
