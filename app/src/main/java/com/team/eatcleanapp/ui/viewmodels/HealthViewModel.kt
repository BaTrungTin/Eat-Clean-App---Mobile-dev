package com.team.eatcleanapp.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import com.team.eatcleanapp.domain.model.enums.ActivityLevel
import com.team.eatcleanapp.domain.model.enums.Gender
import com.team.eatcleanapp.domain.model.enums.Goal
import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.domain.usecase.health.HealthCalculatorUseCase
import com.team.eatcleanapp.domain.usecase.health.CheckHealthMetricsNeedUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val healthCalculatorUseCase: HealthCalculatorUseCase,
    private val checkHealthMetricsNeedUpdateUseCase: CheckHealthMetricsNeedUpdateUseCase
) : ViewModel() {

    private val _bmiResult = MutableStateFlow<Float?>(null)
    val bmiResult: StateFlow<Float?> = _bmiResult

    private val _bmrResult = MutableStateFlow<Float?>(null)
    val bmrResult: StateFlow<Float?> = _bmrResult

    private val _tdeeResult = MutableStateFlow<Float?>(null)
    val tdeeResult: StateFlow<Float?> = _tdeeResult

    private val _dailyCaloriesTarget = MutableStateFlow<Int?>(null)
    val dailyCaloriesTarget: StateFlow<Int?> = _dailyCaloriesTarget

    private val _mealCalories = MutableStateFlow<Map<MealCategory, Int>>(emptyMap())
    val mealCalories: StateFlow<Map<MealCategory, Int>> = _mealCalories

    private val _bmiCategory = MutableStateFlow<String>("")
    val bmiCategory: StateFlow<String> = _bmiCategory

    private val _activityLevelDescription = MutableStateFlow<String>("")
    val activityLevelDescription: StateFlow<String> = _activityLevelDescription

    private val _needsHealthUpdate = MutableStateFlow<Boolean>(false)
    val needsHealthUpdate: StateFlow<Boolean> = _needsHealthUpdate

    fun calculateBMI(weightKg: Float, heightCm: Float) {
        val bmi = healthCalculatorUseCase.calculateBMI(weightKg, heightCm)
        _bmiResult.value = bmi
        _bmiCategory.value = healthCalculatorUseCase.getBMICategory(bmi.toDouble())
    }

    fun calculateBMR(weightKg: Float, heightCm: Float, age: Int, gender: Gender) {
        val bmr = healthCalculatorUseCase.calculateBMR(weightKg, heightCm, age, gender)
        _bmrResult.value = bmr
    }

    fun calculateTDEE(bmr: Float, activityLevel: ActivityLevel) {
        val tdee = healthCalculatorUseCase.calculateTDEE(bmr, activityLevel)
        _tdeeResult.value = tdee
        _activityLevelDescription.value = healthCalculatorUseCase.getActivityLevelDescription(activityLevel)
    }

    fun calculateDailyCaloriesTarget(tdee: Float, goal: Goal) {
        val target = healthCalculatorUseCase.calculateDailyCaloriesTarget(tdee, goal)
        _dailyCaloriesTarget.value = target
    }

    fun calculateAllMealCalories(tdee: Float, goal: Goal) {
        val mealCaloriesMap = mutableMapOf<MealCategory, Int>()
        MealCategory.entries.forEach { mealCategory ->
            val calories = healthCalculatorUseCase.calculateMealCalories(tdee, goal, mealCategory)
            mealCaloriesMap[mealCategory] = calories
        }
        _mealCalories.value = mealCaloriesMap
    }

    fun calculateMealCalories(tdee: Float, goal: Goal, mealCategory: MealCategory): Int {
        return healthCalculatorUseCase.calculateMealCalories(tdee, goal, mealCategory)
    }

    fun inferActivityLevel(minutesPerDay: Int, age: Int): ActivityLevel {
        return healthCalculatorUseCase.inferActivityLevel(minutesPerDay, age)
    }

    fun checkHealthMetricsNeedUpdate(user: com.team.eatcleanapp.domain.model.user.User) {
        _needsHealthUpdate.value = checkHealthMetricsNeedUpdateUseCase(user)
    }

    fun resetAll() {
        _bmiResult.value = null
        _bmrResult.value = null
        _tdeeResult.value = null
        _dailyCaloriesTarget.value = null
        _mealCalories.value = emptyMap()
        _bmiCategory.value = ""
        _activityLevelDescription.value = ""
        _needsHealthUpdate.value = false
    }
}