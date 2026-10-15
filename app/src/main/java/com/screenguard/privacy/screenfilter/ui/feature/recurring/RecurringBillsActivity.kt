package com.screenguard.privacy.screenfilter.ui.feature.recurring

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.ActivityRecurringBillsBinding
import com.screenguard.privacy.screenfilter.ui.feature.add_transaction.AddTransactionActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecurringBillsActivity : BaseActivity<CommonVM, ActivityRecurringBillsBinding>() {

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinding(): ActivityRecurringBillsBinding =
        ActivityRecurringBillsBinding.inflate(layoutInflater)

    override fun initView() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivHelp.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Khoản chi định kỳ & Hóa đơn")
                .setMessage("Mầm Money giúp bạn theo dõi các kỳ hạn thanh toán điện thoại, internet, tiền nhà và đăng ký số. Ứng dụng tự động nhắc nhở nhẹ nhàng để bảo vệ điểm tín nhiệm tài chính cá nhân mà không lưu giữ thông tin mật khẩu ngân hàng.")
                .setPositiveButton("Đã hiểu", null)
                .show()
        }

        binding.ivMore.setOnClickListener {
            val options = arrayOf("Lọc theo nhóm hóa đơn", "Tùy chỉnh chu kỳ nhắc nhở", "Xuất danh sách hóa đơn")
            AlertDialog.Builder(this)
                .setTitle("Tùy chọn")
                .setItems(options) { _, which ->
                    Toast.makeText(this, options[which], Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        binding.btnMarkEvnPaid.setOnClickListener {
            Toast.makeText(this, "Đã đánh dấu Tiền Điện EVN đã thanh toán thành công!", Toast.LENGTH_SHORT).show()
            binding.btnMarkEvnPaid.isEnabled = false
            binding.btnMarkEvnPaid.text = "✓ Đã thanh toán"
            binding.btnMarkEvnPaid.alpha = 0.6f
        }

        binding.switchSpotify.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "BẬT" else "TẮT"
            Toast.makeText(this, "Đã $status nhắc nhở gia hạn Spotify Premium", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddRecurringBill.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java).apply {
                putExtra("IS_RECURRING", true)
            }
            startActivity(intent)
        }
    }

    override fun bindViewModel() {
    }
}
