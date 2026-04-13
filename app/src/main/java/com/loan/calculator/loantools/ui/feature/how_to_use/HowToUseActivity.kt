package com.loan.calculator.loantools.ui.feature.how_to_use

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.util.findColumnIndexBySuffix
import com.loan.calculator.loantools.R
import com.loan.calculator.loantools.base.BaseActivity
import com.loan.calculator.loantools.databinding.ActivityHomeBinding
import com.loan.calculator.loantools.databinding.ActivityHowToUseBinding
import com.loan.calculator.loantools.extention.tap
import com.loan.calculator.loantools.ui.feature.home.HomeVM

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
