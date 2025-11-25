package com.team.eatcleanapp.util
import java.util.Locale

object Constants {

    // firestore collection
    const val MEALS = "monan"

    val DEFAULT_LOCALE: Locale = Locale("vi", "VN")

    // Database
    const val DATABASE_NAME = "eatclean_database"
    const val DATABASE_VERSION = 4

    // Định dạng ngày VIỆT NAM
    const val DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy" // 10/11/2005
    const val DATE_FORMAT_DD_THG_MM = "dd 'thg' MM" // 10 thg 11
    const val DATE_FORMAT_EEE_DD_MM = "EEE, dd/MM" // T2, 10/11
    const val DATE_FORMAT_DAY_FULL = "EEEE" // Thứ hai

    // Giá trị mặc định
    const val DEFAULT_PORTION_SIZE = 1.0
    const val CENTIMETERS_IN_METER = 100f
    const val KILOGRAMS_IN_POUND = 0.453592f
    const val CENTIMETERS_IN_FOOT = 30.48f

    // Validation
    const val MIN_EMAIL_LENGTH = 5
    const val MIN_PASSWORD_LENGTH = 6
    const val MAX_NAME_LENGTH = 50

    // Số ngày trong tuần
    const val DAYS_IN_WEEK = 7

    // Milliseconds trong 1 ngày
    const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000L
}