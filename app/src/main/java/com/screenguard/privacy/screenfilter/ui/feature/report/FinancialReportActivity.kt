package com.screenguard.privacy.screenfilter.ui.feature.report

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.ActivityFinancialReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinancialReportActivity : BaseActivity<CommonVM, ActivityFinancialReportBinding>() {

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinding(): ActivityFinancialReportBinding =
        ActivityFinancialReportBinding.inflate(layoutInflater)

    override fun initView() {
        binding.ivBackReport.setOnClickListener {
            finish()
        }

        binding.ivHelpReport.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Báo cáo chuyên sâu")
                .setMessage("Tỷ lệ giữ lại dòng tiền phản ánh khả năng chuyển đổi thu nhập thành tài sản tích lũy an toàn. Cơ cấu chi tiêu giúp bạn nhận diện các khoản có thể cắt giảm linh hoạt.")
                .setPositiveButton("Đã hiểu", null)
                .show()
        }

        binding.ivMoreReport.setOnClickListener {
            val items = arrayOf("Xuất báo cáo PDF", "Chia sẻ bản tóm tắt", "Cài đặt chu kỳ báo cáo")
            AlertDialog.Builder(this)
                .setTitle("Tùy chọn")
                .setItems(items) { _, which ->
                    Toast.makeText(this, items[which], Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        setupChips()

        binding.btnTransferEmergency.setOnClickListener {
            Toast.makeText(this, "Đã chuyển 1.000.000 đ vào Quỹ dự phòng khẩn cấp!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupChips() {
        val chips = listOf(
            binding.chipWeek,
            binding.chipOctober,
            binding.chip3Months,
            binding.chipYear
        )

        chips.forEach { chip ->
            chip.setOnClickListener {
                chips.forEach { c ->
                    c.background = ContextCompat.getDrawable(this, R.drawable.bg_mam_pill_white)
                    c.setTextColor(ContextCompat.getColor(this, R.color.mam_text_secondary))
                }
                chip.background = ContextCompat.getDrawable(this, R.drawable.bg_chip_selected_dark)
                chip.setTextColor(ContextCompat.getColor(this, R.color.white))
                Toast.makeText(this, "Xem báo cáo: ${(chip as TextView).text}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun bindViewModel() {
    }
}
