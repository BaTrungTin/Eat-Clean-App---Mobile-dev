package com.team.eatcleanapp.ui.screens.meal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.domain.usecase.favorite.AddToFavoriteUseCase
import com.team.eatcleanapp.domain.usecase.favorite.RemoveFromFavoriteUseCase
import com.team.eatcleanapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val favoriteRepository: FavoriteRepository,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val removeFromFavoriteUseCase: RemoveFromFavoriteUseCase,
    private val auth: FirebaseAuth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal: StateFlow<Meal?> = _meal

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val mealId: String? = savedStateHandle.get<String>("mealId")

    init {
        if (mealId != null) {
            getMealDetail(mealId)
            checkFavoriteStatus(mealId)
        } else {
            _isLoading.value = false
        }
    }

    private fun getMealDetail(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = mealRepository.getMealDetail(id)
            _meal.value = result
            _isLoading.value = false
        }
    }

    private fun checkFavoriteStatus(mealId: String) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            when (val result = favoriteRepository.isFavorite(userId, mealId)) {
                is Result.Success -> _isFavorite.value = result.data
                is Result.Error -> _isFavorite.value = false
                else -> {}
            }
        }
    }

    fun toggleFavorite() {
        val currentMeal = _meal.value ?: return
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            if (_isFavorite.value) {
                // Remove from favorites
                val result = removeFromFavoriteUseCase(userId, currentMeal.id)
                if (result is Result.Success) {
                    _isFavorite.value = false
                }
            } else {
                // Add to favorites
                val result = addToFavoriteUseCase(userId, currentMeal.id)
                if (result is Result.Success) {
                    _isFavorite.value = true
                }
            }
        }
    }
}
