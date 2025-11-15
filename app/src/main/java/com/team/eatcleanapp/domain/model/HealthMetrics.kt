package com.team.eatcleanapp.domain.model


// HealthMetrics
data class HealthMetrics(
    val weightKg : Float,
    val heightCm : Float,
    val age : Int,
    val gender : Gender,
    val activityMinutesPerDay : Int,
    val activityDaysPerWeek : Int,
    val activityLevel : ActivityLevel,
    val goal: Goal,
    val bmi: Float = 0f,
    val bmr: Float = 0f,
    val tdee: Float = 0f


 )