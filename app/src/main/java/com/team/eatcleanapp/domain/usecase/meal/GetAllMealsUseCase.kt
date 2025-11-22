package com.team.eatcleanapp.domain.usecase.meal

import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result

class GetAllMealsUseCase(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(): Result<List<Meal>> {
        return mealRepository.getAllMeals().let { result ->
            when (result) {
                is Result.Success -> {
                    val sortedMeals = result.data.sortedBy { it.name.lowercase() }
                    Result.Success(sortedMeals)
                }
                is Result.Error -> result
                is Result.Loading -> result
            }
        }
    }
}