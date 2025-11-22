package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.repository.MealIntakeRepository
import com.team.eatcleanapp.util.Result

class UpdateMealIntakeUseCase(
    private val mealIntakeRepository: MealIntakeRepository
) {

    data class Params(
        val mealIntakeId: String,
        val isChecked: Boolean
    )

    suspend operator fun invoke(params: Params): Result<Unit> {
        return mealIntakeRepository.updateCheckedStatus(params.mealIntakeId, params.isChecked)
    }
}