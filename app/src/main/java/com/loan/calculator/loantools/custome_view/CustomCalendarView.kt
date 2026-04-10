package com.loan.calculator.loantools.custome_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.loan.calculator.loantools.R
import com.loan.calculator.loantools.databinding.ViewCustomCalendarBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class CustomCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding =
        ViewCustomCalendarBinding.inflate(LayoutInflater.from(context), this, true)

    private var selectedDate: LocalDate = LocalDate.now()

    private val monthFormatter =
        DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)

    private val fullDateFormatter =
        DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)

    var onDateSelected: ((LocalDate) -> Unit)? = null

    init {
        setupCalendar()
        setupButtons()
    }

    private fun setupCalendar() {
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)

        binding.calendarView.setup(startMonth, endMonth, DayOfWeek.SUNDAY)
        binding.calendarView.scrollToMonth(currentMonth)

        // Init UI
        binding.tvMonthYear.text = currentMonth.format(monthFormatter)
        binding.tvSelectedDateDisplay.text = selectedDate.format(fullDateFormatter)

        // Month scroll
        binding.calendarView.monthScrollListener = { month ->
            binding.tvMonthYear.text = month.yearMonth.format(monthFormatter)
        }

        // Prev
        binding.btnPrev.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.scrollToMonth(it.yearMonth.minusMonths(1))
            }
        }

        // Next
        binding.btnNext.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.scrollToMonth(it.yearMonth.plusMonths(1))
            }
        }

        // Year picker
        binding.tvMonthYear.setOnClickListener {
            showYearPicker()
        }

        // Day binder
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                val ctx = context

                container.textView.text = data.date.dayOfMonth.toString()

                when {
                    data.date == selectedDate -> {
                        container.textView.setBackgroundResource(R.drawable.bg_btn_calculate)
                        container.textView.setTextColor(
                            ContextCompat.getColor(ctx, R.color.white)
                        )
                    }

                    data.date == LocalDate.now() -> {
                        container.textView.background = null
                        container.textView.setTextColor(
                            ContextCompat.getColor(ctx, R.color.teal_200)
                        )
                    }

                    data.position == DayPosition.MonthDate -> {
                        container.textView.background = null
                        container.textView.setTextColor(
                            ContextCompat.getColor(ctx, R.color.white)
                        )
                    }

                    else -> {
                        container.textView.background = null
                        container.textView.setTextColor(
                            ContextCompat.getColor(ctx, R.color.gray1_in_app)
                        )
                    }
                }

                // FIX CLICK (always set listener)
                container.view.setOnClickListener {
                    if (data.position != DayPosition.MonthDate) return@setOnClickListener
                    if (data.date == selectedDate) return@setOnClickListener

                    val oldDate = selectedDate
                    selectedDate = data.date

                    binding.calendarView.notifyDateChanged(oldDate)
                    binding.calendarView.notifyDateChanged(selectedDate)

                    binding.tvSelectedDateDisplay.text =
                        selectedDate.format(fullDateFormatter)
                }
            }
        }

        // Highlight initial
        binding.calendarView.post {
            binding.calendarView.notifyDateChanged(selectedDate)
        }
    }

    private fun setupButtons() {
        binding.btnDone.setOnClickListener {
            onDateSelected?.invoke(selectedDate)
        }

        binding.btnCancel.setOnClickListener {
            setDate(LocalDate.now())
        }
    }

    // API
    fun setDate(date: LocalDate) {
        val oldDate = selectedDate
        selectedDate = date

        binding.calendarView.scrollToMonth(YearMonth.from(date))

        binding.calendarView.notifyDateChanged(oldDate)
        binding.calendarView.notifyDateChanged(date)

        binding.tvSelectedDateDisplay.text = date.format(fullDateFormatter)
    }

    fun getSelectedDate(): LocalDate = selectedDate

    // YEAR PICKER FIX FULL
    private fun showYearPicker() {
        val currentYear = binding.calendarView.findFirstVisibleMonth()?.yearMonth?.year
            ?: YearMonth.now().year

        val yearPicker = NumberPicker(context).apply {
            minValue = 1900
            maxValue = 2100
            value = currentYear
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(context)
            .setTitle("Select Year")
            .setView(yearPicker)
            .setPositiveButton("OK") { _, _ ->
                val selectedYear = yearPicker.value

                val currentMonth = binding.calendarView.findFirstVisibleMonth()?.yearMonth
                    ?: YearMonth.now()

                val newYearMonth = YearMonth.of(selectedYear, currentMonth.month)

                val safeDay = minOf(selectedDate.dayOfMonth, newYearMonth.lengthOfMonth())
                val newDate = newYearMonth.atDay(safeDay)

                val oldDate = selectedDate
                selectedDate = newDate

                binding.tvSelectedDateDisplay.text =
                    newDate.format(fullDateFormatter)

                binding.calendarView.scrollToMonth(newYearMonth)

                binding.calendarView.post {
                    binding.calendarView.notifyDateChanged(oldDate)
                    binding.calendarView.notifyDateChanged(newDate)

                    // update header luôn
                    binding.tvMonthYear.text =
                        newYearMonth.format(monthFormatter)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = view.findViewById<android.widget.TextView>(R.id.tvDay)
}