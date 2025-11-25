package com.team.eatcleanapp.ui.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.eatcleanapp.domain.model.meal.Favorite
import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.domain.usecase.favorite.*
import com.team.eatcleanapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteMealsUseCase: GetFavoriteMealsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val checkIsFavoriteUseCase: CheckIsFavoriteUseCase,
    private val getMealWithOverrideUseCase: GetMealWithOverrideUseCase,
    private val saveMealOverrideUseCase: SaveMealOverrideUseCase,
    private val canEditMealUseCase: CanEditMealUseCase
) : ViewModel() {

    private val _favorites = MutableStateFlow<Result<List<Favorite>>>(Result.Idle)
    val favorites: StateFlow<Result<List<Favorite>>> = _favorites

    private val _toggleFavoriteState = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val toggleFavoriteState: StateFlow<Result<Boolean>> = _toggleFavoriteState

    private val _isFavoriteState = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val isFavoriteState: StateFlow<Result<Boolean>> = _isFavoriteState

    private val _mealWithOverride = MutableStateFlow<Result<Meal>>(Result.Idle)
    val mealWithOverride: StateFlow<Result<Meal>> = _mealWithOverride

    private val _saveOverrideState = MutableStateFlow<Result<Unit>>(Result.Idle)
    val saveOverrideState: StateFlow<Result<Unit>> = _saveOverrideState

    private val _canEditMealState = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val canEditMealState: StateFlow<Result<Boolean>> = _canEditMealState

    fun getFavorites(userId: String) {
        viewModelScope.launch {
            _favorites.value = Result.Loading
            getFavoriteMealsUseCase(userId).collect { result ->
                _favorites.value = result
            }
        }
    }

    fun toggleFavorite(userId: String, mealId: String) {
        viewModelScope.launch {
            _toggleFavoriteState.value = Result.Loading
            _toggleFavoriteState.value = toggleFavoriteUseCase(userId, mealId)
        }
    }

    fun checkIsFavorite(userId: String, mealId: String) {
        viewModelScope.launch {
            _isFavoriteState.value = Result.Loading
            _isFavoriteState.value = checkIsFavoriteUseCase(userId, mealId)
        }
    }

    fun getMealWithOverride(userId: String, mealId: String) {
        viewModelScope.launch {
            _mealWithOverride.value = Result.Loading
            _mealWithOverride.value = getMealWithOverrideUseCase(userId, mealId)
        }
    }

    fun saveMealOverride(userId: String, mealId: String, modifiedMeal: Meal) {
        viewModelScope.launch {
            _saveOverrideState.value = Result.Loading
            _saveOverrideState.value = saveMealOverrideUseCase(userId, mealId, modifiedMeal)
        }
    }

    fun canEditMeal(userId: String, mealId: String) {
        viewModelScope.launch {
            _canEditMealState.value = Result.Loading
            _canEditMealState.value = canEditMealUseCase(userId, mealId)
        }
    }

    fun resetToggleState() {
        _toggleFavoriteState.value = Result.Idle
    }

    fun resetOverrideState() {
        _saveOverrideState.value = Result.Idle
    }

    fun resetCanEditState() {
        _canEditMealState.value = Result.Idle
    }
}