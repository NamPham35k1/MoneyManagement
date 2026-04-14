package com.screenguard.privacy.screenfilter.utils

import android.app.Activity
import android.content.Intent
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.screenguard.privacy.screenfilter.ui.feature.home.HomeActivity

fun Activity.shareApp() {
    AppOpenManager.getInstance().disableAppResumeWithActivity(HomeActivity::class.java)
    ShareCompat.IntentBuilder.from(this)
        .setType("text/plain")
        .setChooserTitle("Chooser title")
        .setText("http://play.google.com/store/apps/details?id=" + this.packageName)
        .startChooser()
}

fun Activity.policy() {
    AppOpenManager.getInstance().disableAppResumeWithActivity(HomeActivity::class.java)
    val url = "https://sites.google.com/vtdglobal.com/personal-loan-loan-tool-policy/home"
    val i = Intent(Intent.ACTION_VIEW)
    i.data = url.toUri()
    startActivity(i)
}