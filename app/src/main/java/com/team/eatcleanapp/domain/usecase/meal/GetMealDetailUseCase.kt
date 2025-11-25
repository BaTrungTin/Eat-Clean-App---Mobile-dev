package com.team.eatcleanapp.domain.usecase.meal

import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class GetMealDetailUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(mealId: String): Result<Meal?> {
        return mealRepository.getMealDetail(mealId)
    }

    suspend operator fun invoke(mealIds: List<String>): Result<List<Meal>> {
        return mealRepository.getMealsByIds(mealIds)
    }

    suspend fun checkExists(mealId: String): Result<Boolean> {
        return mealRepository.mealExists(mealId)
    }
}