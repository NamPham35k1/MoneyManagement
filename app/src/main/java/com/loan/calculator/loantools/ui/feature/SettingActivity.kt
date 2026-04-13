package com.loan.calculator.loantools.ui.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.amazic.library.Utils.EventTrackingHelper
import com.loan.calculator.loantools.BuildConfig
import com.loan.calculator.loantools.R
import com.loan.calculator.loantools.ads.AdsHelper
import com.loan.calculator.loantools.base.BaseActivity
import com.loan.calculator.loantools.databinding.ActivityPartternsBinding
import com.loan.calculator.loantools.databinding.ActivitySettingBinding
import com.loan.calculator.loantools.extention.logDebug
import com.loan.calculator.loantools.extention.tap
import com.loan.calculator.loantools.rate.RatingDialog
import com.loan.calculator.loantools.rate.SharePrefUtils
import com.loan.calculator.loantools.ui.defaut_ui.language.LanguageSettingActivity
import com.loan.calculator.loantools.ui.feature.home.HomeVM
import com.loan.calculator.loantools.ui.feature.pattern.PatternAdapter
import com.loan.calculator.loantools.utils.DataProject
import com.loan.calculator.loantools.utils.PatternRepository
import com.loan.calculator.loantools.utils.SystemUtil
import com.loan.calculator.loantools.utils.policy
import com.tiktok.appevents.base.EventName

class SettingActivity : BaseActivity<HomeVM, ActivitySettingBinding>() {

    override fun createViewModel(): Class<HomeVM> = HomeVM::class.java
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