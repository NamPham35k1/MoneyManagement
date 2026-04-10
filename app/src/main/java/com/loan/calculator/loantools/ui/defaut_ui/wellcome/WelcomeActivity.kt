package com.loan.calculator.loantools.ui.defaut_ui.wellcome

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.loan.calculator.loantools.R
import com.loan.calculator.loantools.ads.AdsHelper.loadNative
import com.loan.calculator.loantools.ads.NativeType
import com.loan.calculator.loantools.base.BaseActivity
import com.loan.calculator.loantools.databinding.ActivityWelcomeBackBinding
import com.loan.calculator.loantools.databinding.ActivityWellcomeBinding
import com.loan.calculator.loantools.extention.tap
import com.loan.calculator.loantools.ui.defaut_ui.welcome_back.WelcomeBackViewModel
import com.loan.calculator.loantools.ui.feature.home.HomeActivity
import com.loan.calculator.loantools.utils.Constants

class WelcomeActivity : BaseActivity<WelcomeBackViewModel, ActivityWellcomeBinding>() {
    override fun createViewModel(): Class<WelcomeBackViewModel> = WelcomeBackViewModel::class.java

    override fun getViewDataBinding(): ActivityWellcomeBinding = ActivityWellcomeBinding.inflate(layoutInflater)
    override fun initView() {
        binding.tvStart.tap {
            showActivity(HomeActivity::class.java, null)
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