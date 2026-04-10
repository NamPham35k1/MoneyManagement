package com.loan.calculator.loantools

import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.amazic.library.application.AdsApplication
import com.loan.calculator.loantools.share_pref.SharePrefApp
import com.loan.calculator.loantools.ui.defaut_ui.splash.SplashActivity
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
}