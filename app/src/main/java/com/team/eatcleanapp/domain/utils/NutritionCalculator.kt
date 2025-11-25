package com.team.eatcleanapp.domain.utils

import com.team.eatcleanapp.domain.model.enums.*
import com.team.eatcleanapp.util.Constants.CENTIMETERS_IN_METER
import kotlin.math.*
import kotlin.math.roundToInt

object NutritionCalculator {

    // BMI
    fun calculateBmi(weightKg: Float, heightCm: Float): Float {
        if (weightKg <= 0f || heightCm <= 0f)
            return 0f

        val heightM = heightCm / CENTIMETERS_IN_METER
        val bmi = weightKg / (heightM * heightM)

        return bmi.roundTo(1)
    }

    // BMR
    fun calculateBmr(
        weightKg: Float,
        heightCm: Float,
        age: Int,
        gender: Gender
    ): Float {
        if (weightKg <= 0f || heightCm <= 0f || age <= 0)
            return 0f

        val base = 10f * weightKg + 6.25f * heightCm - 5f * age
        val bmr = when (gender) {
            Gender.MALE -> base + 5f
            Gender.FEMALE -> base - 161f
        }
        return bmr
    }

    /**
     * Activity level DỰA TRÊN KHUYẾN NGHỊ WHO 2020
     *
     * Khuyến nghị WHO 2020:
     * - Người lớn (18-64 tuổi): 150-300 phút vận động vừa hoặc 75-150 phút vận động mạnh/tuần
     * - Người cao tuổi (65+): Tương tự người lớn + tập thăng bằng
     * - Trẻ em & thanh thiếu niên (5-17): 60 phút vận động vừa đến mạnh/ngày
     */
    fun inferActivityLevel(
        minutesPerDay: Int,
        age: Int
    ): ActivityLevel {
        val safeMinutesPerDay = max(0, minutesPerDay)

        return when {
            age < 18 -> getChildrenActivityLevel(safeMinutesPerDay)
            else -> getAdultsActivityLevel(safeMinutesPerDay)
        }
    }

    // Phan loai cho Tre em & Thanh thieu nien (5 - 17 tuoi)
    private fun getChildrenActivityLevel(minutesPerWeek: Int): ActivityLevel {
        return when {
            // It van dong: Duoi 60 phut/tuan hoac khong dat khuyen nghi
            minutesPerWeek < 60 -> ActivityLevel.SEDENTARY

            // Van dong nhe: 60-179 phut/tuan (dưới 30% khuyen nghi)
            minutesPerWeek < 180 -> ActivityLevel.LIGHTLY_ACTIVE

            // Van dong vua: 180-419 phut/tuan (30-99% khuyen nghi)
            minutesPerWeek < 420 -> ActivityLevel.MODERATELY_ACTIVE

            // Van dong nhieu: 420-839 phut/tuan (100-199% khuyen nghi)
            minutesPerWeek < 840 -> ActivityLevel.VERY_ACTIVE

            // Van dong rat nhieu: Tren 840 phut/tuan (≥200% khuyen nghi)
            else -> ActivityLevel.EXTRA_ACTIVE
        }
    }

    // Phan loai cho Nguoi lon (18 - 64) & Nguoi coa tuoi (65+)
    private fun getAdultsActivityLevel(minutesPerWeek: Int): ActivityLevel {
        return when {
            // It van dong: duoi 30 phut/tuan (khong dang ke)
            minutesPerWeek < 30
                -> ActivityLevel.SEDENTARY

            // Van dong nhe: 30-149 phut/tuan (duoi khuyen nghi toi thieu)
            minutesPerWeek < 150
                -> ActivityLevel.LIGHTLY_ACTIVE

            // Van dong vua: 150-299 phut/tuan (dat khuyen nghi)
            minutesPerWeek < 300
                -> ActivityLevel.MODERATELY_ACTIVE

            // Van dong nhieu: 300-599 phut/tuan (vuot khuyen nghi)
            minutesPerWeek < 600
                -> ActivityLevel.VERY_ACTIVE

            // Van dong rat nhieu: Tren 600 phut/tuan (gap doi khuyen nghi)
            else -> ActivityLevel.EXTRA_ACTIVE
        }
    }

    // TDEE = BMR * activityFactor
    fun calculateTdee(
        bmr: Float,
        activityLevel: ActivityLevel
    ): Float {
        if (bmr <= 0f)
            return 0f

        val factor = when (activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2f
            ActivityLevel.LIGHTLY_ACTIVE -> 1.375f
            ActivityLevel.MODERATELY_ACTIVE -> 1.55f
            ActivityLevel.VERY_ACTIVE -> 1.725f
            ActivityLevel.EXTRA_ACTIVE -> 1.9f
        }

        val tdee = bmr * factor

        return tdee.roundTo(1)
    }

    // Mô tả chi tiết về mức độ vận động theo nghiên cứu
    fun getActivityLevelDescription(activityLevel: ActivityLevel): String {
        return when (activityLevel) {
            ActivityLevel.SEDENTARY
                -> "Ít vận động - Dưới 30 phút/tuần, chủ yếu ngồi"
            ActivityLevel.LIGHTLY_ACTIVE
                -> "Vận động nhẹ - 30-149 phút/tuần, dưới khuyến nghị WHO"
            ActivityLevel.MODERATELY_ACTIVE
                -> "Vận động vừa - 150-299 phút/tuần, đạt khuyến nghị WHO"
            ActivityLevel.VERY_ACTIVE
                -> "Vận động nhiều - 300-599 phút/tuần, vượt khuyến nghị WHO"
            ActivityLevel.EXTRA_ACTIVE
                -> "Vận động rất nhiều - Trên 600 phút/tuần, gấp đôi khuyến nghị"
        }
    }

    // tinh trang co the dua vao BMI
    fun getBMICategory(bmi: Double): String {
        return when {
            bmi < 16.0 -> "Thiếu cân độ 3"
            bmi in 16.0..16.9 -> "Thiếu cân độ 2"
            bmi in 17.0..18.4 -> "Thiếu cân độ 1"
            bmi in 18.5..22.9 -> "Bình thường"
            bmi in 23.0..24.9 -> "Tiền béo phì (Thừa cân)"
            bmi in 25.0..29.9 -> "Béo phì độ I"
            bmi >= 30.0 -> "Béo phì độ II"
            else -> "Không xác định"
        }
    }

    // kcal moi ngay dua tren muc tieu
    fun calculateDailyCaloriesTarget(tdee: Float, goal: Goal): Int {
        return when(goal) {
            Goal.LOSE_WEIGHT -> (tdee - 500).toInt()
            Goal.GAIN_WEIGHT -> (tdee + 500).toInt()
            else -> tdee.toInt()
        }
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
                // An sang nhieu, toi it
                breakfastPercent = 0.35f
                lunchPercent = 0.40f
                dinnerPercent = 0.25f
            }
            Goal.GAIN_WEIGHT -> {
                // Phan bo deu, tang bua phu
                breakfastPercent = 0.30f
                lunchPercent = 0.35f
                dinnerPercent = 0.35f
            }
            Goal.MAINTAIN_WEIGHT -> {
                // Phan bo can bang
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

    fun needsHealthUpdate(lastUpdate: Long): Boolean {
        return (System.currentTimeMillis() - lastUpdate) > 30L * 24 * 60 * 60 * 1000
    }

    // lam tron
    private fun Float.roundTo(decimals: Int): Float {
        val factor = 10f.pow(decimals.toFloat())
        return (this * factor).roundToInt() / factor
    }
 }