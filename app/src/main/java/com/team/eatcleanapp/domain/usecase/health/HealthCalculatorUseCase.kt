package com.team.eatcleanapp.domain.usecase.health

import com.team.eatcleanapp.domain.model.enums.*
import com.team.eatcleanapp.domain.utils.NutritionCalculator
import javax.inject.Inject

class HealthCalculatorUseCase @Inject constructor() {
    fun calculateBMI(weightKg: Float, heightCm: Float): Float {
        return NutritionCalculator.calculateBmi(weightKg, heightCm)
    }

    fun calculateBMR(weightKg: Float, heightCm: Float, age: Int, gender: Gender): Float {
        return NutritionCalculator.calculateBmr(weightKg, heightCm, age, gender)
    }

    fun calculateTDEE(bmr: Float, activityLevel: ActivityLevel): Float {
        return NutritionCalculator.calculateTdee(bmr, activityLevel)
    }

    fun calculateDailyCaloriesTarget(tdee: Float, goal: Goal): Int {
        return NutritionCalculator.calculateDailyCaloriesTarget(tdee, goal)
    }

    fun calculateMealCalories(tdee: Float, goal: Goal, mealCategory: MealCategory): Int {
        return NutritionCalculator.calculateMealCalories(tdee, goal, mealCategory)
    }

    fun inferActivityLevel(minutesPerDay: Int, age: Int): ActivityLevel {
        return NutritionCalculator.inferActivityLevel(minutesPerDay, age)
    }

    fun getBMICategory(bmi: Double): String {
        return NutritionCalculator.getBMICategory(bmi)
    }

    fun getActivityLevelDescription(activityLevel: ActivityLevel): String {
        return NutritionCalculator.getActivityLevelDescription(activityLevel)
    }
}