package com.loan.calculator.loantools.ui.feature.home

import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.NavHostFragment
import com.amazic.library.ads.collapse_banner_ads.CollapseBannerManager
import com.loan.calculator.loantools.base.BaseActivity
import com.loan.calculator.loantools.custome_view.BottomViewNav
import com.loan.calculator.loantools.extention.clickVibrate
import com.loan.calculator.loantools.R
import com.loan.calculator.loantools.ads.AdsHelper
import com.loan.calculator.loantools.ads.AdsHelper.loadCollapseBanner
import com.loan.calculator.loantools.databinding.ActivityHomeBinding
import com.loan.calculator.loantools.extention.tap
import com.loan.calculator.loantools.ui.feature.history.HistoryLoanActivity
import com.loan.calculator.loantools.ui.feature.mortgages.MortgageActivity
import com.loan.calculator.loantools.ui.feature.persion_loan.PersonLoanActivity
import com.loan.calculator.loantools.ui.feature.persion_loan.PersonLoanVM
import com.loan.calculator.loantools.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.component1
import kotlin.collections.component2

@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeVM, ActivityHomeBinding>() {

    override fun createViewModel(): Class<HomeVM> = HomeVM::class.java
    override fun getViewDataBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
    private var collapseBanner: CollapseBannerManager? = null
    private val bottomNavItems: Map<Int, BottomViewNav> by lazy {
        mapOf(
            R.id.homeFragment to binding.home,
            R.id.compareFragment to binding.compare,
            R.id.toolFragment to binding.tool,
            R.id.settingsFragment to binding.settings
        )
    }

    override fun initView() {
        enableEdgeToEdge()
        onBottomClick()
        collapseBanner = loadCollapseBanner(
            this, this,
            Constants.AdsKey.collapse_home,
            Constants.RemoteKeys.collapse_home,
            binding.frAds
        )
    }

    override fun bindViewModel() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcvHome) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavState(destination.id)
            viewModel.setNavigation(destination.id)
            collapseBanner?.let {
                it.reloadAdNow()
            }
        }
        viewModel.selectedNav.observe(this) {
            navController.navigate(it)
        }
    }


    private fun updateBottomNavState(currentDestinationId: Int) {
        bottomNavItems.forEach { (destinationId, view) ->
            view.isActivated = (destinationId == currentDestinationId)
        }
    }

    private fun onBottomClick() {
        binding.home.clickVibrate {
            viewModel.setNavigation(R.id.homeFragment)
        }
        binding.compare.clickVibrate {
            viewModel.setNavigation(R.id.compareFragment)
        }
        binding.tool.clickVibrate {
            viewModel.setNavigation(R.id.toolFragment)
        }
        binding.settings.clickVibrate {
            viewModel.setNavigation(R.id.settingsFragment)
        }

    }

    override fun onBackPressedSystem() {
        finishAffinity()
    }
}