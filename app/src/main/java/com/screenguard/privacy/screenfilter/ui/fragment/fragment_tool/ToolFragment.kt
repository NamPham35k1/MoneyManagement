package com.screenguard.privacy.screenfilter.ui.fragment.fragment_tool

import android.widget.Toast
import com.screenguard.privacy.screenfilter.base.BaseFragment
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.FragmentToolBinding
import com.screenguard.privacy.screenfilter.ui.feature.main.MainActivityVM
import com.screenguard.privacy.screenfilter.utils.MamMoneyHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToolFragment : BaseFragment<CommonVM, FragmentToolBinding>() {

    private lateinit var mainVM: MainActivityVM
    private var currentMonth = 10
    private var currentYear = 2024

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinging(): FragmentToolBinding =
        FragmentToolBinding.inflate(layoutInflater)

    override fun initView() {
        mainVM = activityViewModel()

        binding.btnPrevMonth.setOnClickListener {
            if (currentMonth > 1) currentMonth-- else {
                currentMonth = 12
                currentYear--
            }
            updateMonthDisplay()
        }

        binding.btnNextMonth.setOnClickListener {
            if (currentMonth < 12) currentMonth++ else {
                currentMonth = 1
                currentYear++
            }
            updateMonthDisplay()
        }

        binding.btnNewBudget.setOnClickListener {
            Toast.makeText(requireContext(), "Tính năng thiết lập ngân sách mới", Toast.LENGTH_SHORT).show()
        }

        binding.ivAvatarTool.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.profile.ProfileSettingsActivity::class.java))
        }

        binding.ivBellTool.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.recurring.RecurringBillsActivity::class.java))
        }

        binding.btnAutoTransferTip.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.report.FinancialReportActivity::class.java))
        }
    }

    override fun bindViewModel() {
        mainVM = activityViewModel()

        mainVM.totalExpense.observe(viewLifecycleOwner) { expense ->
            val limit = 8000000L
            val exp = if (expense > 0) expense else 3550000L
            val remaining = (limit - exp).coerceAtLeast(0L)

            binding.tvTotalBudgetSpent.text = MamMoneyHelper.formatCurrency(exp)
            binding.tvRemainingSpend.text = MamMoneyHelper.formatCurrency(remaining)
        }
    }

    private fun updateMonthDisplay() {
        binding.tvBudgetMonth.text = "Tháng $currentMonth, $currentYear"
    }
}
