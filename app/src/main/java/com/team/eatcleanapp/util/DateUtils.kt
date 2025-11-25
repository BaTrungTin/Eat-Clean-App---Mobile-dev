package com.team.eatcleanapp.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dateFormatDDMMYYYY = SimpleDateFormat(Constants.DATE_FORMAT_DD_MM_YYYY, Constants.DEFAULT_LOCALE)
    private val dateFormatDDTHGMM = SimpleDateFormat(Constants.DATE_FORMAT_DD_THG_MM, Constants.DEFAULT_LOCALE)
    private val dateFormatEEEDDMM = SimpleDateFormat(Constants.DATE_FORMAT_EEE_DD_MM, Constants.DEFAULT_LOCALE)
    private val dateFormatEEEE = SimpleDateFormat(Constants.DATE_FORMAT_DAY_FULL, Constants.DEFAULT_LOCALE)

    // Format date dinh dang: 10/11/2025
    fun formatDateDDMMYYYY(date: Date): String {
        return dateFormatDDMMYYYY.format(date)
    }

    // Format date dinh dang: 10 thg 11
    fun formatDateDDTHGMM(date: Date): String {
        return dateFormatDDTHGMM.format(date)
    }

    // Format date dinh dang: T2, 10/11
    fun formatDateEEEDDMM(date: Date): String {
        return dateFormatEEEDDMM.format(date)
    }

    // Format date dinh dang: Thu hai
    fun formatDateEEEE(date: Date): String {
        return dateFormatEEEE.format(date)
    }

    // Format string sang date tu dinh dang: 10/11/2025
    fun parseDateDDMMYYYY(dateString: String): Date? {
        return try {
            dateFormatDDMMYYYY.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    // Lấy ngày hiện tại
    fun getCurrentDate(): Date {
        return Date()
    }

    // Lay thoi gian bat dau cua ngay (00:00:00)
    fun getStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }

    // Lay thoi gian ket thuc cua ngay (23:59:59)
    fun getEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)

        return calendar.time
    }

    // them so ngay vao mot ngay
    fun addDays(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date

        calendar.add(Calendar.DAY_OF_YEAR, days)
        
        // Normalize to start of day to ensure consistency
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }

    // kiem tra 2 ngay co cung mot ngay khong
    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    // Lay mot tuan tieu chuan tu mot ngay bat ky (tu t2 - cn)
    fun getStandardWeekFromDate(date: Date): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = date

        // tim thu 2 cua tuan
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysToMonday = when (dayOfWeek) {
            Calendar.SUNDAY -> -6
            else -> Calendar.MONDAY - dayOfWeek
        }

        calendar.add(Calendar.DAY_OF_YEAR, daysToMonday)

        val monday = getStartOfDay(calendar.time)

        // tao danh sach 7 ngay tu t2 - cn co chua ngay date
        val week = mutableListOf<Date>()
        for (i in 0 until Constants.DAYS_IN_WEEK)
            week.add(addDays(monday, i))

        return week
    }

    // lay tuan hien tai (chua ngay hien tai)
    fun getCurrentWeek(): List<Date> {
        return getStandardWeekFromDate(getCurrentDate())
    }

    // lay so ngay chenh lech giua hai ngay
    fun getDaysDifference(startDate: Date, endDate: Date): Int {
        val diffInMillis = getStartOfDay(endDate).time - getStartOfDay(startDate).time

        return (diffInMillis / Constants.MILLIS_IN_DAY).toInt()
    }

    // kiem tra xem mot ngay co phai la ngay hien tai khong
    fun isToday(date: Date): Boolean {
        return isSameDay(date, getCurrentDate())
    }
}

