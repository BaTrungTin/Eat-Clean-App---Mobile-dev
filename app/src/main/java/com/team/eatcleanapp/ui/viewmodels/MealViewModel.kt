package com.team.eatcleanapp.ui.screens.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.domain.usecase.meal.GetMealsUseCase
import com.team.eatcleanapp.domain.usecase.meal.GetMealDetailUseCase
import com.team.eatcleanapp.domain.usecase.meal.ManageMealsUseCase
import com.team.eatcleanapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val getMealsUseCase: GetMealsUseCase,
    private val getMealDetailUseCase: GetMealDetailUseCase,
    private val manageMealsUseCase: ManageMealsUseCase
) : ViewModel() {

    private val _meals = MutableStateFlow<Result<List<Meal>>>(Result.Idle)
    val meals: StateFlow<Result<List<Meal>>> = _meals

    private val _mealDetail = MutableStateFlow<Result<Meal?>>(Result.Idle)
    val mealDetail: StateFlow<Result<Meal?>> = _mealDetail

    private val _mealsByIds = MutableStateFlow<Result<List<Meal>>>(Result.Idle)
    val mealsByIds: StateFlow<Result<List<Meal>>> = _mealsByIds

    private val _syncState = MutableStateFlow<Result<Unit>>(Result.Idle)
    val syncState: StateFlow<Result<Unit>> = _syncState


    private val _deleteMealState = MutableStateFlow<Result<Unit>>(Result.Idle)
    val deleteMealState: StateFlow<Result<Unit>> = _deleteMealState

    private val _mealExistsState = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val mealExistsState: StateFlow<Result<Boolean>> = _mealExistsState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _caloriesQuery = MutableStateFlow("")
    val caloriesQuery: StateFlow<String> = _caloriesQuery

    fun getMeals(searchQuery: String? = null, caloriesQuery: String? = null) {
        viewModelScope.launch {
            _meals.value = Result.Loading
            getMealsUseCase(searchQuery, caloriesQuery).collect { result ->
                _meals.value = result
            }
        }
    }

    fun getMealDetail(mealId: String) {
        viewModelScope.launch {
            _mealDetail.value = Result.Loading
            _mealDetail.value = getMealDetailUseCase(mealId)
        }
    }

    fun getMealsByIds(mealIds: List<String>) {
        viewModelScope.launch {
            _mealsByIds.value = Result.Loading
            _mealsByIds.value = getMealDetailUseCase(mealIds)
        }
    }

    fun syncMealsFromRemote() {
        viewModelScope.launch {
            _syncState.value = Result.Loading
            _syncState.value = manageMealsUseCase()
        }
    }

    fun deleteMeal(mealId: String) {
        viewModelScope.launch {
            _deleteMealState.value = Result.Loading
            _deleteMealState.value = manageMealsUseCase(mealId)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        getMeals(searchQuery = query)
    }

    fun setCaloriesQuery(query: String) {
        _caloriesQuery.value = query
        getMeals(caloriesQuery = query)
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _caloriesQuery.value = ""
        getMeals()
    }

    fun resetSyncState() {
        _syncState.value = Result.Idle
    }

    fun resetDeleteState() {
        _deleteMealState.value = Result.Idle
    }

    fun resetMealDetail() {
        _mealDetail.value = Result.Idle
    }

    fun resetMealExistsState() {
        _mealExistsState.value = Result.Idle
    }

}