package com.screenguard.privacy.screenfilter.ui.fragment.fragment_settings

import android.content.Intent
import android.widget.Toast
import com.screenguard.privacy.screenfilter.base.BaseFragment
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.FragmentSettingsBinding
import com.screenguard.privacy.screenfilter.ui.feature.onboarding.OnboardingMoneyActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<CommonVM, FragmentSettingsBinding>() {

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinging(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)

    override fun initView() {
        binding.ivAvatarGoal.setOnClickListener {
            startActivity(Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.profile.ProfileSettingsActivity::class.java))
        }

        binding.ivBellGoal.setOnClickListener {
            startActivity(Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.recurring.RecurringBillsActivity::class.java))
        }

        binding.btnNewGoal.setOnClickListener {
            Toast.makeText(requireContext(), "Tính năng tạo mục tiêu tích lũy mới", Toast.LENGTH_SHORT).show()
        }

        binding.settingOpenProfile.setOnClickListener {
            startActivity(Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.profile.ProfileSettingsActivity::class.java))
        }

        binding.settingOpenRecurring.setOnClickListener {
            startActivity(Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.recurring.RecurringBillsActivity::class.java))
        }

        binding.settingOpenReport.setOnClickListener {
            startActivity(Intent(requireContext(), com.screenguard.privacy.screenfilter.ui.feature.report.FinancialReportActivity::class.java))
        }

        binding.settingResetOnboarding.setOnClickListener {
            startActivity(Intent(requireContext(), OnboardingMoneyActivity::class.java))
        }

        binding.settingBackup.setOnClickListener {
            Toast.makeText(requireContext(), "Dữ liệu được lưu an toàn trực tiếp trên máy", Toast.LENGTH_SHORT).show()
        }

        binding.settingPrivacy.setOnClickListener {
            Toast.makeText(requireContext(), "Mầm Money cam kết không thu thập dữ liệu nhạy cảm", Toast.LENGTH_SHORT).show()
        }
    }

    override fun bindViewModel() {
    }
}
