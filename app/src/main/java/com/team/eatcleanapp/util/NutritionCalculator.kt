package com.team.eatcleanapp.util

import com.team.eatcleanapp.domain.model.ActivityLevel
import com.team.eatcleanapp.domain.model.Gender
import com.team.eatcleanapp.domain.model.Goal
import com.team.eatcleanapp.domain.model.dailymenu.MealCategory
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt


object NutritionCalculator {
    private const val CENTIMETERS_IN_METER = 100f

    // BMI
    fun calculateBmi(weightKg: Float, heightCm: Float): Float {
        if (weightKg <= 0f || heightCm <= 0f) return 0f

        val heightM = heightCm / CENTIMETERS_IN_METER
        val bmi = weightKg / (heightM * heightM)
        return bmi.roundTo(decimals = 1)
    }

    // BMR
    fun calculateBmr(
        weightKg: Float,
        heightCm: Float,
        age: Int,
        gender: Gender
    ): Float {
        if (weightKg <= 0f || heightCm <= 0f || age <= 0) return 0f

        val base = 10f * weightKg + 6.25f * heightCm - 5f * age
        val bmr = when (gender) {
            Gender.MALE -> base + 5f
            Gender.FEMALE -> base - 161f
        }
        return bmr
    }

    //Activity level
    fun inferActivityLevel(
        minutesPerDay: Int,
        daysPerWeek: Int
    ): ActivityLevel {
        val safeMinutesPerDay = max(0, minutesPerDay)
        val safeDaysPerWeek = max(0, daysPerWeek)
        val totalMinutesPerWeek = safeMinutesPerDay * safeDaysPerWeek

        return when {
            totalMinutesPerWeek < 60 -> ActivityLevel.SEDENTARY
            totalMinutesPerWeek < 150 -> ActivityLevel.LIGHTLY_ACTIVE
            totalMinutesPerWeek < 300 -> ActivityLevel.MODERATELY_ACTIVE
            totalMinutesPerWeek < 450 -> ActivityLevel.VERY_ACTIVE
            else -> ActivityLevel.EXTRA_ACTIVE
        }
    }

    // TDEE = BMR * activityFactor
    fun calculateTdee(
        bmr: Float,
        activityLevel: ActivityLevel
    ): Float {
        if (bmr <= 0f) return 0f

        val factor = when (activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2f
            ActivityLevel.LIGHTLY_ACTIVE -> 1.375f
            ActivityLevel.MODERATELY_ACTIVE -> 1.55f
            ActivityLevel.VERY_ACTIVE -> 1.725f
            ActivityLevel.EXTRA_ACTIVE -> 1.9f
        }

        val tdee = bmr * factor
        return tdee.roundTo(decimals = 1)
    }

    // kcal từng buổi ăn
    fun calculateMealCalories(
        tdee: Float,
        goal: Goal,
        mealCategory: MealCategory
    ): Int {
        if (tdee <= 0f) return 0

        val breakfastPercent: Float
        val lunchPercent: Float
        val dinnerPercent: Float

        when (goal) {
            Goal.LOSE_WEIGHT -> {
                breakfastPercent = 0.275f
                lunchPercent = 0.375f
                dinnerPercent = 0.275f
            }
            Goal.GAIN_WEIGHT -> {
                breakfastPercent = 0.325f
                lunchPercent = 0.375f
                dinnerPercent = 0.275f
            }
            Goal.MAINTAIN_WEIGHT -> {
                breakfastPercent = 0.30f
                lunchPercent = 0.40f
                dinnerPercent = 0.30f
            }
        }

        val percent = when (mealCategory) {
            MealCategory.BREAKFAST -> breakfastPercent
            MealCategory.LUNCH -> lunchPercent
            MealCategory.DINNER -> dinnerPercent
        }

        return (tdee * percent).roundToInt()
    }

    // lam tron
    private fun Float.roundTo(decimals: Int): Float {
        val factor = 10f.pow(decimals.toFloat())
        return (this * factor).roundToInt() / factor
    }



 }