package com.team.eatcleanapp.ui.screens.dailymenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuDay
import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuWeek
import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.domain.model.nutrition.NutritionInfo
import com.team.eatcleanapp.domain.usecase.dailymenu.*
import com.team.eatcleanapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getDailyMenuUseCase: GetDailyMenuUseCase,
    private val getWeeklyMenuUseCase: GetWeeklyMenuUseCase,
    private val addMealToDailyMenuUseCase: AddMealToDailyMenuUseCase,
    private val updatePortionSizeUseCase: UpdatePortionSizeUseCase,
    private val deleteSpecificMealUseCase: DeleteSpecificMealUseCase,
    private val getDailyCaloriesUseCase: GetDailyCaloriesUseCase,
    private val getTotalCaloriesByMealTypeUseCase: GetTotalCaloriesByMealTypeUseCase,
    private val calculateDailyNutritionUseCase: CalculateDailyNutritionUseCase,
    private val checkMealInMenuUseCase: CheckMealInMenuUseCase,
    private val deleteDayMenuUseCase: DeleteDayMenuUseCase
) : ViewModel() {

    private val _dailyMenu = MutableStateFlow<Result<DailyMenuDay>>(Result.Idle)
    val dailyMenu: StateFlow<Result<DailyMenuDay>> = _dailyMenu

    private val _weeklyMenu = MutableStateFlow<Result<DailyMenuWeek>>(Result.Idle)
    val weeklyMenu: StateFlow<Result<DailyMenuWeek>> = _weeklyMenu

    private val _addMealState = MutableStateFlow<Result<Unit>>(Result.Idle)
    val addMealState: StateFlow<Result<Unit>> = _addMealState

    private val _updatePortionState = MutableStateFlow<Result<Unit>>(Result.Idle)
    val updatePortionState: StateFlow<Result<Unit>> = _updatePortionState

    private val _deleteMealState = MutableStateFlow<Result<Unit>>(Result.Idle)
    val deleteMealState: StateFlow<Result<Unit>> = _deleteMealState

    private val _dailyCalories = MutableStateFlow<Result<Double>>(Result.Idle)
    val dailyCalories: StateFlow<Result<Double>> = _dailyCalories

    private val _mealTypeCalories = MutableStateFlow<Result<Double>>(Result.Idle)
    val mealTypeCalories: StateFlow<Result<Double>> = _mealTypeCalories

    private val _nutritionInfo = MutableStateFlow<Result<NutritionInfo>>(Result.Idle)
    val nutritionInfo: StateFlow<Result<NutritionInfo>> = _nutritionInfo

    private val _mealCheckState = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val mealCheckState: StateFlow<Result<Boolean>> = _mealCheckState

    fun getDailyMenu(userId: String, date: Date) {
        viewModelScope.launch {
            _dailyMenu.value = Result.Loading
            getDailyMenuUseCase(userId, date).collect { result ->
                _dailyMenu.value = result
            }
        }
    }

    fun getWeeklyMenu(userId: String, weekStartDate: Date) {
        viewModelScope.launch {
            _weeklyMenu.value = Result.Loading
            _weeklyMenu.value = getWeeklyMenuUseCase(userId, weekStartDate)
        }
    }

    fun addMealToDailyMenu(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory,
        portionSize: Double = 1.0
    ) {
        viewModelScope.launch {
            _addMealState.value = Result.Loading
            val result = addMealToDailyMenuUseCase(userId, date, mealId, mealType, portionSize)
            _addMealState.value = result
            // Refresh daily menu after adding meal so HomeScreen shows updated data
            if (result.isSuccess) {
                getDailyMenu(userId, date)
            }
        }
    }

    fun updatePortionSize(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory,
        portionSize: Double
    ) {
        viewModelScope.launch {
            _updatePortionState.value = Result.Loading
            _updatePortionState.value = updatePortionSizeUseCase(userId, date, mealId, mealType, portionSize)
        }
    }

    fun deleteSpecificMeal(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory
    ) {
        viewModelScope.launch {
            _deleteMealState.value = Result.Loading
            _deleteMealState.value = deleteSpecificMealUseCase(userId, date, mealId, mealType)
        }
    }

    fun deleteDayMenu(
        userId: String,
        dateMealTypes: Map<Date, List<MealCategory>?>
    ) {
        viewModelScope.launch {
            _deleteMealState.value = Result.Loading
            _deleteMealState.value = deleteDayMenuUseCase(userId, dateMealTypes)
        }
    }

    fun getDailyCalories(userId: String, date: Date) {
        viewModelScope.launch {
            _dailyCalories.value = Result.Loading
            _dailyCalories.value = getDailyCaloriesUseCase(userId, date)
        }
    }

    fun getTotalCaloriesByMealType(
        userId: String,
        date: Date,
        mealType: MealCategory
    ) {
        viewModelScope.launch {
            _mealTypeCalories.value = Result.Loading
            _mealTypeCalories.value = getTotalCaloriesByMealTypeUseCase(userId, date, mealType)
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

    fun checkMealInMenu(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory
    ) {
        viewModelScope.launch {
            _mealCheckState.value = Result.Loading
            _mealCheckState.value = checkMealInMenuUseCase(userId, date, mealId, mealType)
        }
    }

    fun resetAddMealState() {
        _addMealState.value = Result.Idle
    }

    fun resetUpdatePortionState() {
        _updatePortionState.value = Result.Idle
    }

    fun resetDeleteMealState() {
        _deleteMealState.value = Result.Idle
    }

    fun resetNutritionInfo() {
        _nutritionInfo.value = Result.Idle
    }

    fun resetMealCheckState() {
        _mealCheckState.value = Result.Idle
    }

    fun deleteMultipleItems(
        userId: String,
        weekStartDate: Date,
        deleteOperations: List<suspend () -> Unit>
    ) {
        viewModelScope.launch {
            _deleteMealState.value = Result.Loading
            try {
                android.util.Log.d("MenuViewModel", "Starting to delete ${deleteOperations.size} items")
                // Execute all delete operations sequentially
                deleteOperations.forEachIndexed { index, operation ->
                    android.util.Log.d("MenuViewModel", "Deleting item ${index + 1}/${deleteOperations.size}")
                    operation()
                }
                android.util.Log.d("MenuViewModel", "All delete operations completed, waiting for DB update...")
                // Wait longer for database to commit transaction
                kotlinx.coroutines.delay(800)
                // Force UI to show loading state
                _weeklyMenu.value = Result.Loading
                // Refresh weekly menu
                android.util.Log.d("MenuViewModel", "Refreshing weekly menu...")
                val refreshResult = getWeeklyMenuUseCase(userId, weekStartDate)
                android.util.Log.d("MenuViewModel", "Refresh result: isSuccess=${refreshResult.isSuccess}, data size=${if (refreshResult.isSuccess) (refreshResult as Result.Success).data.days.sumOf { it.breakfast.size + it.lunch.size + it.dinner.size } else 0}")
                _weeklyMenu.value = refreshResult
                android.util.Log.d("MenuViewModel", "Weekly menu refreshed, result: ${refreshResult.isSuccess}")
                _deleteMealState.value = Result.Success(Unit)
            } catch (e: Exception) {
                android.util.Log.e("MenuViewModel", "Error in deleteMultipleItems: ${e.message}", e)
                _deleteMealState.value = Result.Error(e, "Lỗi khi xóa món ăn: ${e.message}")
            }
        }
    }

    suspend fun deleteSpecificMealDirectly(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory
    ) {
        // Normalize date to start of day to ensure consistency
        val calendar = java.util.Calendar.getInstance().apply {
            time = date
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        val normalizedDate = calendar.time
        
        android.util.Log.d("MenuViewModel", "Deleting meal: userId=$userId, originalDate=${date.time}, normalizedDate=${normalizedDate.time}, mealId=$mealId, mealType=$mealType")
        val result = deleteSpecificMealUseCase(userId, normalizedDate, mealId, mealType)
        if (result.isError) {
            val errorMessage = result.errorMessage() ?: "Failed to delete meal"
            android.util.Log.e("MenuViewModel", "Error deleting meal: $errorMessage")
            throw Exception(errorMessage)
        }
        android.util.Log.d("MenuViewModel", "Successfully deleted meal")
    }

    suspend fun deleteDayMenuDirectly(
        userId: String,
        dateMealTypes: Map<Date, List<MealCategory>?>
    ) {
        // Normalize all dates to start of day
        val normalizedDateMealTypes = dateMealTypes.mapKeys { (date, _) ->
            val calendar = java.util.Calendar.getInstance().apply {
                time = date
                set(java.util.Calendar.HOUR_OF_DAY, 0)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }
            calendar.time
        }
        
        android.util.Log.d("MenuViewModel", "Deleting day menu: userId=$userId, originalDates=${dateMealTypes.keys.map { it.time }}, normalizedDates=${normalizedDateMealTypes.keys.map { it.time }}")
        val result = deleteDayMenuUseCase(userId, normalizedDateMealTypes)
        if (result.isError) {
            val errorMessage = result.errorMessage() ?: "Failed to delete day menu"
            android.util.Log.e("MenuViewModel", "Error deleting day menu: $errorMessage")
            throw Exception(errorMessage)
        }
        android.util.Log.d("MenuViewModel", "Successfully deleted day menu")
    }
}