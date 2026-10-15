package com.screenguard.privacy.screenfilter.ui.fragment.fragment_home

import androidx.navigation.fragment.findNavController
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.base.BaseFragment
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.FragmentHomeBinding
import com.screenguard.privacy.screenfilter.ui.feature.main.MainActivityVM
import com.screenguard.privacy.screenfilter.utils.MamMoneyHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<CommonVM, FragmentHomeBinding>() {

    private lateinit var mainVM: MainActivityVM

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinging(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun initView() {
        mainVM = activityViewModel()

        binding.layoutToggleBalance.setOnClickListener {
            mainVM.toggleBalanceVisibility()
        }

        binding.btnSeeAllTransactions.setOnClickListener {
            try {
                findNavController().navigate(R.id.compareFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.ivAvatar.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.profile.ProfileSettingsActivity::class.java))
        }

        binding.ivBell.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.recurring.RecurringBillsActivity::class.java))
        }

        binding.btnBillPill.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.recurring.RecurringBillsActivity::class.java))
        }

        binding.cardInsight.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.report.FinancialReportActivity::class.java))
        }

        binding.cardBudgetMonthly.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.report.FinancialReportActivity::class.java))
        }
    }

    override fun bindViewModel() {
        mainVM = activityViewModel()

        mainVM.isBalanceHidden.observe(viewLifecycleOwner) { isHidden ->
            if (isHidden) {
                binding.ivEyeToggle.setImageResource(R.drawable.ic_eye_hidden)
                binding.tvAvailableBalance.text = "•••••••• đ"
            } else {
                binding.ivEyeToggle.setImageResource(R.drawable.ic_eye_visible)
                val balance = mainVM.availableBalance.value ?: 8450000L
                binding.tvAvailableBalance.text = MamMoneyHelper.formatCurrency(balance)
            }
        }

        mainVM.availableBalance.observe(viewLifecycleOwner) { balance ->
            if (mainVM.isBalanceHidden.value != true) {
                binding.tvAvailableBalance.text = MamMoneyHelper.formatCurrency(balance)
            }
        }

        mainVM.totalIncome.observe(viewLifecycleOwner) { income ->
            val inc = if (income > 0) income else 12000000L
            binding.tvMonthlyIncome.text = MamMoneyHelper.formatIncome(inc)
        }

        mainVM.totalExpense.observe(viewLifecycleOwner) { expense ->
            val exp = if (expense > 0) expense else 3550000L
            binding.tvMonthlyExpense.text = MamMoneyHelper.formatExpense(exp)
        }

        mainVM.safeToSpendToday.observe(viewLifecycleOwner) { safeSpend ->
            val value = if (safeSpend > 0) safeSpend else 280000L
            binding.tvDailySafeSpend.text = "${MamMoneyHelper.formatCurrency(value, false)} đ/ngày"
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
