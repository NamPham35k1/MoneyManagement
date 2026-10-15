package com.screenguard.privacy.screenfilter.utils

import com.screenguard.privacy.screenfilter.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object MamMoneyHelper {

    fun formatCurrency(amount: Long, withSymbol: Boolean = true): String {
        val symbols = DecimalFormatSymbols(Locale.GERMANY).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        val formatter = DecimalFormat("#,###", symbols)
        val formatted = formatter.format(amount)
        return if (withSymbol) "$formatted đ" else formatted
    }

    fun formatIncome(amount: Long): String {
        return "+${formatCurrency(amount)}"
    }

    fun formatExpense(amount: Long): String {
        return "-${formatCurrency(amount)}"
    }

    fun getCategoryIcon(category: String): Int {
        return when {
            category.contains("Ăn uống", true) -> R.drawable.ic_cat_food
            category.contains("Di chuyển", true) -> R.drawable.ic_cat_transport
            category.contains("Mua sắm", true) -> R.drawable.ic_cat_shopping
            category.contains("Nhà ở", true) -> R.drawable.ic_cat_housing
            category.contains("Hóa đơn", true) || category.contains("Điện", true) -> R.drawable.ic_cat_bill
            category.contains("Giải trí", true) -> R.drawable.ic_cat_entertainment
            category.contains("Sức khỏe", true) -> R.drawable.ic_cat_health
            category.contains("Giáo dục", true) || category.contains("Sách", true) -> R.drawable.ic_cat_education
            category.contains("Lương", true) || category.contains("Thu nhập", true) -> R.drawable.ic_cat_salary
            category.contains("Cà phê", true) || category.contains("Cafe", true) -> R.drawable.ic_coffee_item
            else -> R.drawable.ic_cat_other
        }
    }
}
