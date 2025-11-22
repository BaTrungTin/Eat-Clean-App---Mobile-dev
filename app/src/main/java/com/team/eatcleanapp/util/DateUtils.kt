package com.team.eatcleanapp.util

import com.team.eatcleanapp.domain.model.dailymenu.DateCategory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    const val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val DATE_FORMAT_DD_MM_YYYY = "dd-MM-yyyy"

    private val dateFormatISO = SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD, Locale.getDefault())
    private val dateFormatDisplay = SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY, Locale.getDefault())

    fun formatDate(date: Date): String = dateFormatISO.format(date)

    fun formatDateDisplay(date: Date): String = dateFormatDisplay.format(date)

    fun parseDate(dateString: String): Date? = try {
        dateFormatISO.parse(dateString)
    } catch (e: Exception) {
        null
    }

    fun parseDateDisplay(dateString: String): Date? = try {
        dateFormatDisplay.parse(dateString)
    } catch (e: Exception) {
        null
    }

    fun getCurrentDate(): Date = Date()

    fun addDays(date: Date, days: Int): Date {
        return Calendar.getInstance().apply {
            time = date
            add(Calendar.DAY_OF_YEAR, days)
        }.time
    }

    fun getStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun getEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun getDaysDifference(startDate: Date, endDate: Date): Int {
        val diffInMillis = endDate.time - startDate.time
        return (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
    }

    fun getWeekForDate(selectedDate: Date): List<Date> {
        val startOfWeek = getStartOfWeek(selectedDate)
        return (0..6).map { day -> addDays(startOfWeek, day) }
    }

    fun getStartOfWeek(date: Date): Date {
        return Calendar.getInstance().apply {
            time = date
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }.time
    }

    fun getEndOfWeek(date: Date): Date {
        return Calendar.getInstance().apply {
            time = date
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }.time
    }

    fun getValidWeekForDate(date: Date): Pair<Date, Date> {
        return Pair(getStartOfWeek(date), getEndOfWeek(date))
    }

    fun isValidWeek(startDate: Date, endDate: Date): Boolean {
        val calendar = Calendar.getInstance().apply { firstDayOfWeek = Calendar.MONDAY }

        calendar.time = startDate
        val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        calendar.time = endDate
        val endDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        return startDayOfWeek == Calendar.MONDAY &&
                endDayOfWeek == Calendar.SUNDAY &&
                getDaysDifference(startDate, endDate) == 6
    }

    fun isFullValidWeek(dateCategories: List<DateCategory>): Boolean {
        if (dateCategories.size != 7) return false
        val dates = dateCategories.map { it.date }
        val minDate = dates.minOrNull() ?: return false
        val maxDate = dates.maxOrNull() ?: return false
        return isValidWeek(minDate, maxDate)
    }

    fun getWeekStringsForDate(selectedDate: Date): List<String> =
        getWeekForDate(selectedDate).map { formatDate(it) }

    fun getWeekDisplayStringsForDate(selectedDate: Date): List<String> =
        getWeekForDate(selectedDate).map { formatDateDisplay(it) }

    fun getWeekRangeForDate(selectedDate: Date): Pair<String, String> =
        Pair(formatDate(getStartOfWeek(selectedDate)), formatDate(getEndOfWeek(selectedDate)))

    fun getWeekDisplayRangeForDate(selectedDate: Date): Pair<String, String> =
        Pair(formatDateDisplay(getStartOfWeek(selectedDate)), formatDateDisplay(getEndOfWeek(selectedDate)))

    fun getCurrentWeekRange(): Pair<String, String> = getWeekRangeForDate(getCurrentDate())

    fun getTodayString(): String = formatDate(getCurrentDate())

    fun getTodayDisplayString(): String = formatDateDisplay(getCurrentDate())

    fun isToday(date: Date): Boolean = isSameDay(date, getCurrentDate())

    fun isInCurrentWeek(date: Date): Boolean =
        getWeekForDate(getCurrentDate()).any { isSameDay(it, date) }

    fun isInSameWeek(date: Date, selectedDate: Date): Boolean =
        getWeekForDate(selectedDate).any { isSameDay(it, date) }

    fun isValidDate(dateString: String, format: String = DATE_FORMAT_YYYY_MM_DD): Boolean =
        try {
            SimpleDateFormat(format, Locale.getDefault()).apply {
                isLenient = false
            }.parse(dateString)
            true
        } catch (e: Exception) {
            false
        }

    // === DATECATEGORY UTILS (CÓ THỂ XEM XÉT CHUYỂN SANG REPOSITORY) ===
    fun getSortedWeekCategories(dateCategories: List<DateCategory>): List<DateCategory> {
        require(dateCategories.size == 7) { "Phải có đúng 7 danh mục ngày" }
        return dateCategories.sortedBy { it.date }
    }
}