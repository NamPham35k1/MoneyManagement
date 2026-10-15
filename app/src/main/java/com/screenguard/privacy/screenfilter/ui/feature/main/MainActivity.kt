package com.screenguard.privacy.screenfilter.ui.feature.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActivityVM, ActivityMainBinding>() {

    override fun createViewModel(): Class<MainActivityVM> = MainActivityVM::class.java
    override fun getViewDataBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        binding.btnFloatingAdd.setOnClickListener {
            showActivity(com.screenguard.privacy.screenfilter.ui.feature.add_transaction.AddTransactionActivity::class.java, null)
        }
    }

    override fun bindViewModel() {
    }
}
