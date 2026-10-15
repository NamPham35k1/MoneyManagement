package com.screenguard.privacy.screenfilter.ui.defaut_ui.wellcome

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.ads.AdsHelper.loadNative
import com.screenguard.privacy.screenfilter.ads.NativeType
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.databinding.ActivityWelcomeBackBinding
import com.screenguard.privacy.screenfilter.databinding.ActivityWellcomeBinding
import com.screenguard.privacy.screenfilter.extention.tap
import com.screenguard.privacy.screenfilter.ui.defaut_ui.welcome_back.WelcomeBackViewModel
import com.screenguard.privacy.screenfilter.ui.feature.main.MainActivity
import com.screenguard.privacy.screenfilter.utils.Constants

class WelcomeActivity : BaseActivity<WelcomeBackViewModel, ActivityWellcomeBinding>() {
    override fun createViewModel(): Class<WelcomeBackViewModel> = WelcomeBackViewModel::class.java

    override fun getViewDataBinding(): ActivityWellcomeBinding = ActivityWellcomeBinding.inflate(layoutInflater)
    override fun initView() {
        binding.tvStart.tap {
            if (sharePrefApp.isFirstOpenApp) {
                showActivity(com.screenguard.privacy.screenfilter.ui.feature.onboarding.OnboardingMoneyActivity::class.java, null)
            } else {
                showActivity(MainActivity::class.java, null)
            }
            finish()
        }
        loadNative(
            activity = this,
            lifecycleOwner = this,
            frAds = binding.frAds,
            remoteKeyMain = Constants.RemoteKeys.native_wel,
            remoteKeySecondary = Constants.RemoteKeys.native_wel,
            type = NativeType.NATIVE_LARGE_ABOVE
        )
    }

    override fun bindViewModel() {

    }

    override fun onBackPressedSystem() {
       finish()
    }
}