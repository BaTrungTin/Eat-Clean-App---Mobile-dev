package com.team.eatcleanapp.domain.usecase.meal

import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMealsUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    operator fun invoke(
        searchQuery: String? = null,
        caloriesQuery: String? = null
    ): Flow<Result<List<Meal>>> {
        return when {
            !searchQuery.isNullOrBlank() -> mealRepository.searchMeals(searchQuery)
            !caloriesQuery.isNullOrBlank() -> mealRepository.searchMealsByCalories(caloriesQuery)
            else -> mealRepository.getAllMeals()
        }
    }
}