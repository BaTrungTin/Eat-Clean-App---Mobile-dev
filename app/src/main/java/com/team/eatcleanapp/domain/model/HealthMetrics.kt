package com.team.eatcleanapp.domain.model

// Gender
enum class Gender {
    MALE,
    FEMALE
 }


// ActivityLevel
enum class ActivityLevel {
    SEDENTARY,
    LIGHT,
    MODERATE,
    ACTIVE,
    VERY_ACTIVE
 }


 // Goal
enum class Goal {
    LOSE_WEIGHT,
    MAINTAIN_WEIGHT,
    GAIN_WEIGHT
 }

// MealTime
enum class MealTime {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK

 }

// HealthMetrics
data class HealthMetrics(
    val weightKg : Float,
    val heightCm : Float,
    val age : Int,
    val gender : Gender,
    val activityMinutesPerDay : Int,
    val activityDaysPerDay : Int,
    // LEVEL ĐÃ SUY RA TỪ HAI FIELD TRÊN
    val activityLevel : ActivityLevel,
    val goal: Goal,
    val bmi: Float = 0f,
    val bmr: Float = 0f,
    val tdee: Float = 0f


 )