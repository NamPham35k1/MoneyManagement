package com.screenguard.privacy.screenfilter

import com.vifarmer.ads.lib.ads.app_open_ads.AppOpenManager
import com.screenguard.privacy.screenfilter.ui.defaut_ui.splash.SplashActivity
import com.vifarmer.ads.lib.application.AdsApplication
import dagger.hilt.android.HiltAndroidApp
import kotlin.jvm.java
@HiltAndroidApp
class App : AdsApplication(){
    companion object {
        lateinit var instance: App
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity::class.java)

    }
    override fun getAppTokenAdjust(): String {
        return getString(R.string.adjust_key)
    }

    override fun getFacebookID(): String {
        return getString(R.string.facebook_app_id)
    }

    override fun buildDebug(): Boolean {
        return false
    }
}