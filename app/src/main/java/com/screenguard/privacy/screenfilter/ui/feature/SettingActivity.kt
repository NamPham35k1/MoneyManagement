package com.screenguard.privacy.screenfilter.ui.feature

import android.content.Context
import android.content.Intent
import android.view.View
import com.screenguard.privacy.screenfilter.BuildConfig
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.ads.AdsHelper
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.ActivitySettingBinding
import com.screenguard.privacy.screenfilter.extention.logDebug
import com.screenguard.privacy.screenfilter.extention.tap
import com.screenguard.privacy.screenfilter.rate.RatingDialog
import com.screenguard.privacy.screenfilter.rate.SharePrefUtils
import com.screenguard.privacy.screenfilter.ui.defaut_ui.language.LanguageSettingActivity
import com.screenguard.privacy.screenfilter.utils.DataProject
import com.screenguard.privacy.screenfilter.utils.SystemUtil
import com.screenguard.privacy.screenfilter.utils.policy

class SettingActivity : BaseActivity<CommonVM, ActivitySettingBinding>() {

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinding(): ActivitySettingBinding = ActivitySettingBinding.inflate(layoutInflater)
    override fun initView() {
        binding.ivBack.tap { onBackPressedSystem() }
        binding.tvShare.tap {
            shareApp()
        }
        binding.llLanguage.setOnClickListener {
            val intent = Intent(this, LanguageSettingActivity::class.java)
            intent.putExtra("screen", "SettingsFragment")
            startActivity(intent)
        }
        binding.tvPrivacyPolicy.tap {
           policy()
        }
        binding.tvRateUs.tap {
            rateApp()
        }
        val version = getString(R.string.version, getAppVersionName())
        binding.tvAppVersion.text = version
    }

    override fun bindViewModel() {

    }
    override fun onResume() {
        super.onResume()
        checkRate()
    }

    private fun checkRate() {
        if (SharePrefUtils.isRated(this)) {
            binding.tvRateUs.visibility = View.GONE
            logDebug("check ","oke")
        } else {
            binding.tvRateUs.visibility = View.VISIBLE
        }
    }

    private fun rateApp() {
        RatingDialog(this).apply {
            setOnDismissListener {
                checkRate()
            }
        }.show()
    }

    private fun shareApp() {
        AdsHelper.dismissAppOpen(this)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        var shareMessage = getString(R.string.app_name)
        shareMessage = "$shareMessage \nhttps://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "Share to"))
    }

    override fun onPostResume() {
        super.onPostResume()
        binding.tvCurrentLanguage.text = getCurrentLanguage()
    }
    private fun getCurrentLanguage(): String {
        val currentIsoLang = SystemUtil.getPreLanguage(this)
        val currentLang = DataProject.listLanguage().find {
            it.isoLanguage == currentIsoLang
        }?.languageName

        return currentLang ?: "UnKnown"
    }
    fun Context.getAppVersionName(): String {
        return packageManager.getPackageInfo(packageName, 0).versionName ?: "unknown"
    }
}