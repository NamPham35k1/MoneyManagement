package com.loan.calculator.loantools.ui.defaut_ui.welcome_back

import androidx.activity.enableEdgeToEdge
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.amazic.library.ads.callback.AppOpenCallback
import com.loan.calculator.loantools.databinding.ActivityWelcomeBackBinding
import com.google.android.gms.ads.appopen.AppOpenAd
import com.loan.calculator.loantools.ads.AdsHelper.loadNative
import com.loan.calculator.loantools.ads.NativeType
import com.loan.calculator.loantools.base.BaseActivity
import com.loan.calculator.loantools.extention.tap
import com.loan.calculator.loantools.utils.Constants

class WelcomeBackActivity : BaseActivity<WelcomeBackViewModel, ActivityWelcomeBackBinding>() {
    override fun createViewModel(): Class<WelcomeBackViewModel> = WelcomeBackViewModel::class.java

    override fun getViewDataBinding(): ActivityWelcomeBackBinding = ActivityWelcomeBackBinding.inflate(layoutInflater)

    override fun initView() {
        enableEdgeToEdge()
        //Preload
        AppOpenManager.getInstance().loadAd(this, AdmobApi.getInstance().getListIDByName(Constants.RemoteKeys.resume_wb), object : AppOpenCallback() {
            override fun onAdLoaded(ad: AppOpenAd?) {
                super.onAdLoaded(ad)
                binding.tvStart.alpha = 1f
                binding.tvStart.isEnabled = true
            }

            override fun onAdFailedToLoad() {
                super.onAdFailedToLoad()
                binding.tvStart.alpha = 1f
                binding.tvStart.isEnabled = true
            }
        }, Constants.RemoteKeys.resume_wb)

        binding.tvStart.tap {
            //Show resume preload
            AppOpenManager.getInstance().showAdIfAvailableWelcomeBack(
                this,
                AdmobApi.getInstance().getListIDByName(Constants.RemoteKeys.resume_wb),
                object : AppOpenCallback() {
                    override fun onAdFailedToShowFullScreenContent() {
                        super.onAdFailedToShowFullScreenContent()
                        finish()
                    }

                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        finish()
                    }
                }, Constants.RemoteKeys.resume_wb
            )
        }
            //Load and show resume
            /*AppOpenManager.getInstance().loadAndShowResumeAds(this, AdmobApi.getInstance().getListIDByName(Constants.RemoteKeys.resume_wb), object : AppOpenCallback() {
                override fun onAdFailedToLoad() {
                    super.onAdFailedToLoad()
                    finish()
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    finish()
                }

                override fun onAdFailedToShowFullScreenContent() {
                    super.onAdFailedToShowFullScreenContent()
                    finish()
                }
            }, Constants.RemoteKeys.resume_wb)*//*
        }

        */loadNative(
            activity = this,
            lifecycleOwner = this,
            frAds = binding.frAds,
            remoteKeyMain = Constants.RemoteKeys.native_wb,
            remoteKeySecondary = Constants.RemoteKeys.native_wb,
            type = NativeType.NATIVE_LARGE_ABOVE
        )
    }

    override fun bindViewModel() {

    }

    override fun onBackPressCustom() {

    }
}
