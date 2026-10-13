package com.screenguard.privacy.screenfilter.ui.feature

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.util.findColumnIndexBySuffix
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.ActivityHomeBinding
import com.screenguard.privacy.screenfilter.databinding.ActivitySuccessBinding
import com.screenguard.privacy.screenfilter.extention.tap

class SuccessActivity : BaseActivity<CommonVM, ActivitySuccessBinding>() {

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinding(): ActivitySuccessBinding = ActivitySuccessBinding.inflate(layoutInflater)
    override fun initView() {

        val keyTurn = intent.getStringExtra("KEY_TURN")
        if (keyTurn == "TURN_ON") {
            binding.tvContent.visibility = View.VISIBLE
            binding.tvContent2.visibility = View.GONE
        } else {
            binding.tvContent.visibility = View.GONE
            binding.tvContent2.visibility = View.VISIBLE
        }
        binding.lotteAnimation.apply {
            setAnimation(R.raw.sucess)
            repeatCount = com.airbnb.lottie.LottieDrawable.INFINITE
            playAnimation()
        }

    }

    override fun bindViewModel() {
        binding.icBack.tap {
            finish()
        }
    }
}
