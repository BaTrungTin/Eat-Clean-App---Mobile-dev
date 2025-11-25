package com.team.eatcleanapp.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadMeals()
    }

    fun loadMeals() {
        viewModelScope.launch {
            _isLoading.value = true
            _meals.value = mealRepository.getAllMeals()
            _isLoading.value = false
        }
    }

    fun searchMeals(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            if (query.isBlank()) {
                _meals.value = mealRepository.getAllMeals()
            } else {
                _meals.value = mealRepository.searchMeals(query)
            }
            _isLoading.value = false
        }
    }
}
