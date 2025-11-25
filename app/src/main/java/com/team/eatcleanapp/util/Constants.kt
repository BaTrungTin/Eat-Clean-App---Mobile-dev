package com.team.eatcleanapp.util

object Constants {
    // API
    const val BASE_URL = "https://api.eatcleanapp.com"
    const val API_TIMEOUT = 30L
    
    // Database
    const val DATABASE_NAME = "eatclean_database"
    const val DATABASE_VERSION = 3
    
    // Date Format
    const val DATE_FORMAT = "yyyy-MM-dd"
    const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val TIME_FORMAT = "HH:mm"
    
    // Default Values
    const val DEFAULT_PORTION_SIZE = 1.0
    const val DEFAULT_CALORIES_PER_DAY = 2000.0
    
    // Validation
    const val MIN_EMAIL_LENGTH = 5
    const val MIN_PASSWORD_LENGTH = 6
    const val MAX_NAME_LENGTH = 50
    
    // Meal Categories
    const val MEAL_BREAKFAST = "BREAKFAST"
    const val MEAL_LUNCH = "LUNCH"
    const val MEAL_DINNER = "DINNER"
    const val MEAL_SNACK = "SNACK"
}

