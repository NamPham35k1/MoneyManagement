package com.screenguard.privacy.screenfilter.ui.fragment.fragment_compare

import androidx.core.content.ContextCompat
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.base.BaseFragment
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.FragmentCompareBinding
import com.screenguard.privacy.screenfilter.ui.feature.main.MainActivityVM
import com.screenguard.privacy.screenfilter.utils.MamMoneyHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompareFragment : BaseFragment<CommonVM, FragmentCompareBinding>() {

    private lateinit var mainVM: MainActivityVM
    private var currentFilter = "ALL" // "ALL", "EXPENSE", "INCOME"

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinging(): FragmentCompareBinding =
        FragmentCompareBinding.inflate(layoutInflater)

    override fun initView() {
        mainVM = activityViewModel()
        setupFilters()

        binding.ivAvatarCompare.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.profile.ProfileSettingsActivity::class.java))
        }

        binding.ivBellCompare.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.recurring.RecurringBillsActivity::class.java))
        }

        binding.cardCashflowReport.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.report.FinancialReportActivity::class.java))
        }
    }

    override fun bindViewModel() {
        mainVM = activityViewModel()

        mainVM.totalIncome.observe(viewLifecycleOwner) { income ->
            val inc = if (income > 0) income else 12000000L
            binding.tvTxTotalIncome.text = MamMoneyHelper.formatIncome(inc)
        }

        mainVM.totalExpense.observe(viewLifecycleOwner) { expense ->
            val exp = if (expense > 0) expense else 3550000L
            binding.tvTxTotalExpense.text = MamMoneyHelper.formatExpense(exp)
        }

        mainVM.availableBalance.observe(viewLifecycleOwner) { balance ->
            val bal = if (balance > 0) balance else 8450000L
            binding.tvTxRemaining.text = MamMoneyHelper.formatIncome(bal)
        }
    }

    private fun setupFilters() {
        binding.chipFilterAll.setOnClickListener {
            setFilter("ALL")
        }
        binding.chipFilterExpense.setOnClickListener {
            setFilter("EXPENSE")
        }
        binding.chipFilterIncome.setOnClickListener {
            setFilter("INCOME")
        }
    }

    private fun setFilter(filter: String) {
        currentFilter = filter
        val selectedBg = R.drawable.bg_mam_pill_green
        val normalBg = R.drawable.bg_mam_pill_white
        val selectedTextColor = ContextCompat.getColor(requireContext(), R.color.mam_primary_dark)
        val normalTextColor = ContextCompat.getColor(requireContext(), R.color.mam_text_secondary)

        when (filter) {
            "ALL" -> {
                binding.chipFilterAll.setBackgroundResource(selectedBg)
                binding.chipFilterAll.setTextColor(selectedTextColor)

                binding.chipFilterExpense.setBackgroundResource(normalBg)
                binding.chipFilterExpense.setTextColor(normalTextColor)

                binding.chipFilterIncome.setBackgroundResource(normalBg)
                binding.chipFilterIncome.setTextColor(normalTextColor)
            }
            "EXPENSE" -> {
                binding.chipFilterExpense.setBackgroundResource(selectedBg)
                binding.chipFilterExpense.setTextColor(selectedTextColor)

                binding.chipFilterAll.setBackgroundResource(normalBg)
                binding.chipFilterAll.setTextColor(normalTextColor)

                binding.chipFilterIncome.setBackgroundResource(normalBg)
                binding.chipFilterIncome.setTextColor(normalTextColor)
            }
            "INCOME" -> {
                binding.chipFilterIncome.setBackgroundResource(selectedBg)
                binding.chipFilterIncome.setTextColor(selectedTextColor)

                binding.chipFilterAll.setBackgroundResource(normalBg)
                binding.chipFilterAll.setTextColor(normalTextColor)

                binding.chipFilterExpense.setBackgroundResource(normalBg)
                binding.chipFilterExpense.setTextColor(normalTextColor)
            }
        }
    }
}
