package com.team.eatcleanapp.ui.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.usecase.favorite.GetFavoriteMealsUseCase
import com.team.eatcleanapp.domain.usecase.favorite.RemoveFromFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteMealsUseCase: GetFavoriteMealsUseCase,
    private val removeFromFavoriteUseCase: RemoveFromFavoriteUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _favoriteMeals = MutableStateFlow<List<Meal>>(emptyList())
    val favoriteMeals: StateFlow<List<Meal>> = _favoriteMeals

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadFavoriteMeals()
    }

    fun loadFavoriteMeals() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val meals = getFavoriteMealsUseCase(userId)
                    _favoriteMeals.value = meals
                } catch (e: Exception) {
                    _favoriteMeals.value = emptyList()
                } finally {
                    _isLoading.value = false
                }
            }
        } else {
             _isLoading.value = false
        }
    }

    fun removeFavorite(mealId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            viewModelScope.launch {
                try {
                    // Optimistically update UI
                    val currentList = _favoriteMeals.value.toMutableList()
                    currentList.removeAll { it.id == mealId }
                    _favoriteMeals.value = currentList
                    
                    // Call use case
                    removeFromFavoriteUseCase(userId, mealId)
                    
                    // Reload to ensure sync (optional, can rely on optimistic update)
                    // loadFavoriteMeals() 
                } catch (e: Exception) {
                    // Revert if error (optional)
                    loadFavoriteMeals()
                }
            }
        }
    }
}
