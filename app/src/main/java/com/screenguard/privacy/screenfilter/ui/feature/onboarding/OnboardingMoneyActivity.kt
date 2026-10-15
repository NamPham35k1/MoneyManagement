package com.screenguard.privacy.screenfilter.ui.feature.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.ActivityOnboardingMoneyBinding
import com.screenguard.privacy.screenfilter.ui.feature.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingMoneyActivity : BaseActivity<CommonVM, ActivityOnboardingMoneyBinding>() {

    private var selectedSalaryDay = "Ngày 10 hàng tháng"

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinding(): ActivityOnboardingMoneyBinding =
        ActivityOnboardingMoneyBinding.inflate(layoutInflater)

    override fun initView() {
        binding.btnBackOnboarding.setOnClickListener {
            finish()
        }

        binding.optionGoal1.setOnClickListener {
            binding.cbGoal1.isChecked = !binding.cbGoal1.isChecked
        }
        binding.optionGoal2.setOnClickListener {
            binding.cbGoal2.isChecked = !binding.cbGoal2.isChecked
        }
        binding.optionGoal3.setOnClickListener {
            binding.cbGoal3.isChecked = !binding.cbGoal3.isChecked
        }

        binding.btnSkipStep2.setOnClickListener {
            binding.edtExpectedIncome.setText("0 đ")
            Toast.makeText(this, "Đã bỏ qua thu nhập hàng tháng", Toast.LENGTH_SHORT).show()
        }

        val days = arrayOf(
            "Ngày 5 hàng tháng",
            "Ngày 10 hàng tháng",
            "Ngày 15 hàng tháng",
            "Ngày 20 hàng tháng",
            "Ngày 25 hàng tháng",
            "Ngày cuối tháng"
        )
        binding.btnSalaryDayPicker.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Chọn ngày nhận thu nhập")
                .setItems(days) { _, which ->
                    selectedSalaryDay = days[which]
                    binding.tvSalaryDayDisplay.text = selectedSalaryDay
                }
                .show()
        }

        binding.btnRestoreBackup.setOnClickListener {
            Toast.makeText(this, "Tính năng khôi phục sao lưu đang được xử lý", Toast.LENGTH_SHORT).show()
        }

        binding.btnStartJourney.setOnClickListener {
            // Save state and open MainActivity
            sharePrefApp.isFirstOpenApp = false
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }

    override fun bindViewModel() {
    }
}
