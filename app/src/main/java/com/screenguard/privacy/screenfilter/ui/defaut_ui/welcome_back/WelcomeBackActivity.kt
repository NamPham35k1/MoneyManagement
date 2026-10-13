package com.screenguard.privacy.screenfilter.ui.defaut_ui.welcome_back

import androidx.activity.enableEdgeToEdge
import com.vifarmer.ads.lib.admob.AdmobApi
import com.vifarmer.ads.lib.ads.app_open_ads.AppOpenManager
import com.vifarmer.ads.lib.callback.AppOpenCallback
import com.screenguard.privacy.screenfilter.databinding.ActivityWelcomeBackBinding
import com.google.android.gms.ads.appopen.AppOpenAd
import com.screenguard.privacy.screenfilter.ads.AdsHelper.loadNative
import com.screenguard.privacy.screenfilter.ads.NativeType
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.extention.tap
import com.screenguard.privacy.screenfilter.utils.Constants

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
