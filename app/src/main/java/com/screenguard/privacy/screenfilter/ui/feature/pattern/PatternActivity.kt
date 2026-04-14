package com.screenguard.privacy.screenfilter.ui.feature.pattern

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.databinding.ActivityHomeBinding
import com.screenguard.privacy.screenfilter.databinding.ActivityPartternsBinding
import com.screenguard.privacy.screenfilter.extention.logDebug
import com.screenguard.privacy.screenfilter.extention.tap
import com.screenguard.privacy.screenfilter.ui.feature.home.HomeVM
import com.screenguard.privacy.screenfilter.utils.PatternRepository
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PatternActivity : BaseActivity<HomeVM, ActivityPartternsBinding>() {

    override fun createViewModel(): Class<HomeVM> = HomeVM::class.java
    override fun getViewDataBinding(): ActivityPartternsBinding = ActivityPartternsBinding.inflate(layoutInflater)
    override fun initView() {
        val data = PatternRepository.getDefaultPatterns()
        binding.rvPattern.layoutManager = GridLayoutManager(this, 2)
        val adapter = PatternAdapter(data) { sharePrefApp.idPattern = it }
        binding.rvPattern.adapter = adapter

        adapter.selectedId = sharePrefApp.idPattern
        logDebug("CheckIDPattern", "${sharePrefApp.idPattern}")
    }

    override fun bindViewModel() {
        sharePrefApp.idPattern
        binding.icBack.tap {
            finish()
        }
    }
}