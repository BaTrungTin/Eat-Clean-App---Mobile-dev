package com.team.eatcleanapp.domain.usecase.meal

import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class ManageMealsUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(): Result<Unit> = mealRepository.syncMealsFromRemote()
    suspend operator fun invoke(mealId: String): Result<Unit> = mealRepository.deleteMeal(mealId)
}