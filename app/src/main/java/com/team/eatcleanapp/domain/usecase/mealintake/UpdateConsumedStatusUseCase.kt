package com.team.eatcleanapp.domain.usecase.user

import com.team.eatcleanapp.domain.repository.MealIntakeRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class UpdateConsumedStatusUseCase @Inject constructor(
    private val mealIntakeRepository: MealIntakeRepository
) {
    suspend operator fun invoke(id: String, isConsumed: Boolean): Result<Unit> {
        return mealIntakeRepository.updateConsumedStatus(id, isConsumed)
    }
}