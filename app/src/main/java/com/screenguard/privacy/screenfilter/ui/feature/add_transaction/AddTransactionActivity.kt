package com.screenguard.privacy.screenfilter.ui.feature.add_transaction

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.ActivityAddTransactionBinding
import com.screenguard.privacy.screenfilter.model.TransactionEntity
import com.screenguard.privacy.screenfilter.repository.MamRepository
import com.screenguard.privacy.screenfilter.utils.MamMoneyHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddTransactionActivity : BaseActivity<CommonVM, ActivityAddTransactionBinding>() {

    private var currentType: String = "EXPENSE" // or "INCOME"
    private var amountString: String = "85000"
    private var selectedCategory: String = "Ăn uống"
    private var selectedWallet: String = "Ví MoMo"
    private var selectedDateCalendar: Calendar = Calendar.getInstance()

    private lateinit var categoryViews: List<CategoryViewHolder>

    data class CategoryViewHolder(
        val layout: LinearLayout,
        val icon: ImageView,
        val text: TextView,
        val name: String,
        val emoji: String,
        val iconRes: Int
    )

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinding(): ActivityAddTransactionBinding =
        ActivityAddTransactionBinding.inflate(layoutInflater)

    override fun initView() {
        setupCategories()
        setupTypeSwitcher()
        setupKeypad()
        setupDatePicker()
        setupWalletPicker()
        updateAmountDisplay()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.ivHeaderAvatar.setOnClickListener {
            startActivity(android.content.Intent(this, com.screenguard.privacy.screenfilter.ui.feature.profile.ProfileSettingsActivity::class.java))
        }

        if (intent.getBooleanExtra("IS_RECURRING", false)) {
            binding.switchRecurring.isChecked = true
        }

        binding.btnSaveTransaction.setOnClickListener {
            saveTransaction()
        }
    }

    override fun bindViewModel() {
    }

    private fun setupTypeSwitcher() {
        binding.btnTypeExpense.setOnClickListener {
            if (currentType != "EXPENSE") {
                currentType = "EXPENSE"
                binding.btnTypeExpense.setBackgroundResource(R.drawable.bg_segmented_selected)
                binding.btnTypeExpense.text = "• Chi tiền"
                binding.btnTypeExpense.setTextColor(ContextCompat.getColor(this, R.color.mam_expense_red))

                binding.btnTypeIncome.background = null
                binding.btnTypeIncome.text = "Thu tiền"
                binding.btnTypeIncome.setTextColor(ContextCompat.getColor(this, R.color.mam_text_secondary))
                updateAmountDisplay()
            }
        }

        binding.btnTypeIncome.setOnClickListener {
            if (currentType != "INCOME") {
                currentType = "INCOME"
                binding.btnTypeIncome.setBackgroundResource(R.drawable.bg_segmented_selected)
                binding.btnTypeIncome.text = "• Thu tiền"
                binding.btnTypeIncome.setTextColor(ContextCompat.getColor(this, R.color.mam_income_green))

                binding.btnTypeExpense.background = null
                binding.btnTypeExpense.text = "Chi tiền"
                binding.btnTypeExpense.setTextColor(ContextCompat.getColor(this, R.color.mam_text_secondary))
                updateAmountDisplay()
            }
        }
    }

    private fun setupCategories() {
        categoryViews = listOf(
            CategoryViewHolder(binding.catFood, binding.ivCatFood, binding.tvCatFood, "Ăn uống", "🍽️", R.drawable.ic_cat_food),
            CategoryViewHolder(binding.catTransport, binding.ivCatTransport, binding.tvCatTransport, "Di chuyển", "🚗", R.drawable.ic_cat_transport),
            CategoryViewHolder(binding.catShopping, binding.ivCatShopping, binding.tvCatShopping, "Mua sắm", "🛍️", R.drawable.ic_cat_shopping),
            CategoryViewHolder(binding.catHousing, binding.ivCatHousing, binding.tvCatHousing, "Nhà ở", "🏠", R.drawable.ic_cat_housing),
            CategoryViewHolder(binding.catBills, binding.ivCatBills, binding.tvCatBills, "Hóa đơn", "🧾", R.drawable.ic_cat_bill),
            CategoryViewHolder(binding.catEntertainment, binding.ivCatEntertainment, binding.tvCatEntertainment, "Giải trí", "🎮", R.drawable.ic_cat_entertainment),
            CategoryViewHolder(binding.catHealth, binding.ivCatHealth, binding.tvCatHealth, "Sức khỏe", "❤️", R.drawable.ic_cat_health),
            CategoryViewHolder(binding.catOther, binding.ivCatOther, binding.tvCatOther, "Khác", "✨", R.drawable.ic_cat_other)
        )

        for (holder in categoryViews) {
            holder.layout.setOnClickListener {
                selectCategory(holder.name)
            }
        }
        selectCategory(selectedCategory)
    }

    private fun selectCategory(name: String) {
        selectedCategory = name
        for (holder in categoryViews) {
            val isSelected = holder.name == name
            if (isSelected) {
                holder.layout.setBackgroundResource(R.drawable.bg_category_item_selected)
                holder.icon.setColorFilter(ContextCompat.getColor(this, R.color.mam_primary_dark))
                holder.text.setTextColor(ContextCompat.getColor(this, R.color.mam_primary_dark))
                binding.tvSelectedCategoryHeader.text = "${holder.emoji} ${holder.name}"
            } else {
                holder.layout.setBackgroundResource(R.drawable.bg_category_item_normal)
                holder.icon.setColorFilter(ContextCompat.getColor(this, R.color.mam_text_primary))
                holder.text.setTextColor(ContextCompat.getColor(this, R.color.mam_text_secondary))
            }
        }
    }

    private fun setupKeypad() {
        val numberButtons = mapOf(
            binding.btnKey0 to "0",
            binding.btnKey1 to "1",
            binding.btnKey2 to "2",
            binding.btnKey3 to "3",
            binding.btnKey4 to "4",
            binding.btnKey5 to "5",
            binding.btnKey6 to "6",
            binding.btnKey7 to "7",
            binding.btnKey8 to "8",
            binding.btnKey9 to "9"
        )

        for ((view, digit) in numberButtons) {
            view.setOnClickListener {
                appendDigit(digit)
            }
        }

        binding.btnKeyTripleZero.setOnClickListener {
            if (amountString.isNotEmpty() && amountString != "0") {
                if (amountString.length + 3 <= 12) {
                    amountString += "000"
                    updateAmountDisplay()
                }
            }
        }

        binding.btnKeyBackspace.setOnClickListener {
            if (amountString.isNotEmpty()) {
                amountString = amountString.dropLast(1)
                if (amountString.isEmpty()) amountString = "0"
                updateAmountDisplay()
            }
        }

        binding.btnQuickAdd50k.setOnClickListener { addAmount(50000L) }
        binding.btnQuickAdd100k.setOnClickListener { addAmount(100000L) }
        binding.btnQuickAdd200k.setOnClickListener { addAmount(200000L) }
        binding.btnQuickClear.setOnClickListener {
            amountString = "0"
            updateAmountDisplay()
        }
    }

    private fun appendDigit(d: String) {
        if (amountString == "0") {
            amountString = d
        } else if (amountString.length < 12) {
            amountString += d
        }
        updateAmountDisplay()
    }

    private fun addAmount(value: Long) {
        val cur = amountString.toLongOrNull() ?: 0L
        amountString = (cur + value).toString()
        updateAmountDisplay()
    }

    private fun updateAmountDisplay() {
        val amount = amountString.toLongOrNull() ?: 0L
        val formatted = MamMoneyHelper.formatCurrency(amount, withSymbol = false)
        val prefix = if (currentType == "EXPENSE") "- " else "+ "
        val full = "$prefix$formatted đ"
        binding.tvAmountDisplay.text = full
        if (currentType == "EXPENSE") {
            binding.tvAmountDisplay.setTextColor(ContextCompat.getColor(this, R.color.mam_text_primary))
        } else {
            binding.tvAmountDisplay.setTextColor(ContextCompat.getColor(this, R.color.mam_income_green))
        }
    }

    private fun setupDatePicker() {
        updateDateText()
        binding.btnPickDate.setOnClickListener {
            val dp = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    selectedDateCalendar.set(Calendar.YEAR, year)
                    selectedDateCalendar.set(Calendar.MONTH, month)
                    selectedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateText()
                },
                selectedDateCalendar.get(Calendar.YEAR),
                selectedDateCalendar.get(Calendar.MONTH),
                selectedDateCalendar.get(Calendar.DAY_OF_MONTH)
            )
            dp.show()
        }
    }

    private fun updateDateText() {
        val now = Calendar.getInstance()
        val isToday = now.get(Calendar.YEAR) == selectedDateCalendar.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == selectedDateCalendar.get(Calendar.DAY_OF_YEAR)

        val dayFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val dateStr = dayFormat.format(selectedDateCalendar.time)
        binding.tvDateDisplay.text = if (isToday) "Hôm nay, $dateStr" else dateStr
    }

    private fun setupWalletPicker() {
        binding.tvWalletDisplay.text = selectedWallet
        val wallets = arrayOf("Ví MoMo", "Tiền mặt", "Techcombank", "MB Bank")
        binding.btnPickWallet.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Chọn nguồn tiền")
                .setItems(wallets) { _, which ->
                    selectedWallet = wallets[which]
                    binding.tvWalletDisplay.text = selectedWallet
                    if (selectedWallet == "Ví MoMo") {
                        binding.ivWalletIcon.setImageResource(R.drawable.ic_momo_circle)
                    } else if (selectedWallet == "Tiền mặt") {
                        binding.ivWalletIcon.setImageResource(R.drawable.ic_cat_salary)
                    } else {
                        binding.ivWalletIcon.setImageResource(R.drawable.ic_cat_bill)
                    }
                }
                .show()
        }
    }

    private fun saveTransaction() {
        val amount = amountString.toLongOrNull() ?: 0L
        if (amount <= 0) {
            Toast.makeText(this, "Vui lòng nhập số tiền lớn hơn 0", Toast.LENGTH_SHORT).show()
            return
        }

        val note = binding.edtNote.text.toString().trim()
        val isRecurring = binding.switchRecurring.isChecked
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeStr = timeFormat.format(System.currentTimeMillis())

        val dayGroupFormat = SimpleDateFormat("dd 'Tháng' MM", Locale("vi", "VN"))
        val dateGroup = "Hôm nay ${dayGroupFormat.format(selectedDateCalendar.time)}"

        val title = if (note.isNotEmpty()) note else selectedCategory

        val tx = TransactionEntity(
            title = title,
            type = currentType,
            amount = amount,
            category = selectedCategory,
            wallet = selectedWallet,
            timestamp = selectedDateCalendar.timeInMillis,
            dateGroup = dateGroup,
            timeStr = timeStr,
            note = note,
            isRecurring = isRecurring
        )

        lifecycleScope.launch {
            MamRepository(this@AddTransactionActivity).insertTransaction(tx)
            Toast.makeText(this@AddTransactionActivity, "Đã lưu giao dịch thành công!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
