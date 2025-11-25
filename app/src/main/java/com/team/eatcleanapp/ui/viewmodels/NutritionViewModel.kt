package com.team.eatcleanapp.ui.screens.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.eatcleanapp.domain.model.nutrition.MealIntake
import com.team.eatcleanapp.domain.model.nutrition.NutritionInfo
import com.team.eatcleanapp.domain.usecase.dailymenu.CalculateDailyNutritionUseCase
import com.team.eatcleanapp.domain.usecase.user.GetMealIntakeByDateUseCase
import com.team.eatcleanapp.domain.usecase.user.SaveMealIntakeUseCase
import com.team.eatcleanapp.domain.usecase.user.UpdateConsumedStatusUseCase
import com.team.eatcleanapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val getMealIntakeByDateUseCase: GetMealIntakeByDateUseCase,
    private val calculateDailyNutritionUseCase: CalculateDailyNutritionUseCase,
    private val saveMealIntakeUseCase: SaveMealIntakeUseCase,
    private val updateConsumedStatusUseCase: UpdateConsumedStatusUseCase
) : ViewModel() {

    private val _mealIntake = MutableStateFlow<Result<List<MealIntake>>>(Result.Idle)
    val mealIntake: StateFlow<Result<List<MealIntake>>> = _mealIntake

    private val _nutritionInfo = MutableStateFlow<Result<NutritionInfo>>(Result.Idle)
    val nutritionInfo: StateFlow<Result<NutritionInfo>> = _nutritionInfo

    private val _saveIntakeState = MutableStateFlow<Result<Unit>>(Result.Idle)
    val saveIntakeState: StateFlow<Result<Unit>> = _saveIntakeState

    private val _updateConsumedState = MutableStateFlow<Result<Unit>>(Result.Idle)
    val updateConsumedState: StateFlow<Result<Unit>> = _updateConsumedState

    fun getMealIntakeByDate(userId: String, date: Date) {
        viewModelScope.launch {
            _mealIntake.value = Result.Loading
            _mealIntake.value = getMealIntakeByDateUseCase(userId, date)
        }
    }

    fun calculateDailyNutrition(
        userId: String,
        date: Date,
        targetCalories: Double
    ) {
        viewModelScope.launch {
            _nutritionInfo.value = Result.Loading
            _nutritionInfo.value = calculateDailyNutritionUseCase(userId, date, targetCalories)
        }
    }

    fun saveMealIntake(mealIntake: MealIntake) {
        viewModelScope.launch {
            _saveIntakeState.value = Result.Loading
            _saveIntakeState.value = saveMealIntakeUseCase(mealIntake)
        }
    }

    fun updateConsumedStatus(id: String, isConsumed: Boolean) {
        viewModelScope.launch {
            _updateConsumedState.value = Result.Loading
            _updateConsumedState.value = updateConsumedStatusUseCase(id, isConsumed)
        }
    }

    fun resetSaveState() {
        _saveIntakeState.value = Result.Idle
    }

    fun resetUpdateState() {
        _updateConsumedState.value = Result.Idle
    }

    fun resetNutritionInfo() {
        _nutritionInfo.value = Result.Idle
    }
}