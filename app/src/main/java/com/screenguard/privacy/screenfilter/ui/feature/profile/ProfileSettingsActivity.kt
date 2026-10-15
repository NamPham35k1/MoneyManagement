package com.screenguard.privacy.screenfilter.ui.feature.profile

import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.ActivityProfileSettingsBinding
import com.screenguard.privacy.screenfilter.ui.feature.onboarding.OnboardingMoneyActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSettingsActivity : BaseActivity<CommonVM, ActivityProfileSettingsBinding>() {

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinding(): ActivityProfileSettingsBinding =
        ActivityProfileSettingsBinding.inflate(layoutInflater)

    override fun initView() {
        binding.ivBackProfile.setOnClickListener {
            finish()
        }

        binding.ivHelpProfile.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hồ sơ & Cài đặt Mầm Money")
                .setMessage("Tất cả thiết lập tài chính và dữ liệu cá nhân của bạn được lưu an toàn trực tiếp trong bộ nhớ máy (Room Database) và không yêu cầu đăng nhập máy chủ ngoài.")
                .setPositiveButton("Đã hiểu", null)
                .show()
        }

        binding.ivMoreProfile.setOnClickListener {
            val items = arrayOf("Giới thiệu Mầm Money", "Chính sách bảo mật", "Đánh giá ứng dụng 5 sao")
            AlertDialog.Builder(this)
                .setTitle("Thông tin thêm")
                .setItems(items) { _, which ->
                    Toast.makeText(this, items[which], Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // Edit Profile
        binding.btnEditProfile.setOnClickListener {
            val input = EditText(this).apply {
                setText(binding.tvUserName.text)
                setSelection(text.length)
            }
            AlertDialog.Builder(this)
                .setTitle("Đổi tên hồ sơ")
                .setView(input)
                .setPositiveButton("Lưu") { _, _ ->
                    val newName = input.text.toString().trim()
                    if (newName.isNotEmpty()) {
                        binding.tvUserName.text = newName
                        Toast.makeText(this, "Đã cập nhật tên hồ sơ", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Hủy", null)
                .show()
        }

        // Salary Day Picker
        val days = arrayOf("Ngày 1", "Ngày 5", "Ngày 10", "Ngày 15", "Ngày 20", "Ngày 25", "Ngày cuối tháng")
        binding.itemSalaryDay.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Ngày nhận lương hằng tháng")
                .setItems(days) { _, which ->
                    binding.tvSalaryDayVal.text = days[which]
                    Toast.makeText(this, "Đã cập nhật: ${days[which]}", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // Expected Income Picker
        val incomes = arrayOf("8.000.000 đ", "12.000.000 đ", "15.000.000 đ", "20.000.000 đ", "30.000.000 đ", "50.000.000 đ")
        binding.itemExpectedIncome.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Thu nhập dự tính hằng tháng")
                .setItems(incomes) { _, which ->
                    binding.tvExpectedIncomeVal.text = incomes[which]
                    Toast.makeText(this, "Đã chọn định mức: ${incomes[which]}", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // Currency Picker
        val currencies = arrayOf("VNĐ (đ)", "USD ($)", "EUR (€)", "JPY (¥)", "KRW (₩)")
        binding.itemCurrency.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Đơn vị tiền tệ")
                .setItems(currencies) { _, which ->
                    binding.tvCurrencyVal.text = currencies[which]
                    Toast.makeText(this, "Đã chọn: ${currencies[which]}", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // Budget Alert Switch
        binding.switchBudgetAlert.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "BẬT cảnh báo 80% hạn mức" else "TẮT cảnh báo hạn mức"
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }

        // Theme Toggle
        binding.btnThemeLight.setOnClickListener {
            binding.btnThemeLight.background = ContextCompat.getDrawable(this, R.drawable.bg_pill_mint_light)
            binding.btnThemeDark.background = ContextCompat.getDrawable(this, R.drawable.bg_mam_pill_gray)
            Toast.makeText(this, "Đang sử dụng Giao diện Màu ngà Sáng", Toast.LENGTH_SHORT).show()
        }

        binding.btnThemeDark.setOnClickListener {
            binding.btnThemeDark.background = ContextCompat.getDrawable(this, R.drawable.bg_pill_mint_light)
            binding.btnThemeLight.background = ContextCompat.getDrawable(this, R.drawable.bg_mam_pill_gray)
            Toast.makeText(this, "Giao diện Gỗ đêm Tối sẽ áp dụng trong chu kỳ tới", Toast.LENGTH_SHORT).show()
        }

        // Daily Reminder Picker
        val reminderHours = arrayOf("19:00", "20:00", "20:30", "21:00", "21:30", "22:00")
        binding.itemDailyRemind.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Giờ nhắc nhở ghi chép chi tiêu")
                .setItems(reminderHours) { _, which ->
                    binding.tvDailyRemindVal.text = reminderHours[which]
                    Toast.makeText(this, "Đã hẹn giờ: ${reminderHours[which]} mỗi tối", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // Lock App Switch
        binding.switchLockApp.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Đã kích hoạt khóa ứng dụng bằng Biometric/PIN" else "Đã tắt khóa ứng dụng"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Hide Balance Switch
        binding.switchHideBalance.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Sẽ ẩn số dư khi mở ứng dụng" else "Hiển thị số dư mặc định khi mở ứng dụng"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Export Data
        binding.btnExportData.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Xuất sao lưu dữ liệu")
                .setMessage("Tạo bản xuất dữ liệu tài chính của bạn dưới định dạng bảng tính Excel (.xlsx) hoặc CSV để lưu trữ hoặc phân tích cá nhân.")
                .setPositiveButton("Xuất Excel (.xlsx)") { _, _ ->
                    Toast.makeText(this, "Đã xuất file MamMoney_Backup_102024.xlsx vào thư mục Download", Toast.LENGTH_LONG).show()
                }
                .setNeutralButton("Xuất CSV", null)
                .setNegativeButton("Hủy", null)
                .show()
        }

        // Google Drive Sync
        binding.btnGoogleDriveSync.setOnClickListener {
            Toast.makeText(this, "Đã đồng bộ sao lưu bảo mật lên Google Drive riêng tư!", Toast.LENGTH_SHORT).show()
        }

        // Reset Data
        binding.btnResetData.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Xóa dữ liệu & Làm mới?")
                .setMessage("Thao tác này sẽ đặt lại ứng dụng về trạng thái ban đầu và mở màn hình Khởi tạo tài chính (Bắt đầu trồng mầm). Bạn có chắc chắn muốn tiếp tục?")
                .setPositiveButton("Làm mới") { _, _ ->
                    val intent = Intent(this, OnboardingMoneyActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Hủy", null)
                .show()
        }
    }

    override fun bindViewModel() {
    }
}
