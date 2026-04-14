package com.screenguard.privacy.screenfilter.ui.feature.how_to_use

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.util.findColumnIndexBySuffix
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.databinding.ActivityHomeBinding
import com.screenguard.privacy.screenfilter.databinding.ActivityHowToUseBinding
import com.screenguard.privacy.screenfilter.extention.tap
import com.screenguard.privacy.screenfilter.ui.feature.home.HomeVM

class HowToUseActivity : BaseActivity<HomeVM, ActivityHowToUseBinding>() {

    override fun createViewModel(): Class<HomeVM> = HomeVM::class.java
    override fun getViewDataBinding(): ActivityHowToUseBinding = ActivityHowToUseBinding.inflate(layoutInflater)
    override fun initView() {
        binding.icBack.tap {
            finish()
        }
    }

    override fun bindViewModel() {
    }
}
