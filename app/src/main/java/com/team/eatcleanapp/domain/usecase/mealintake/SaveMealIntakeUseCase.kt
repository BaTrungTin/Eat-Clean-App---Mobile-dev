package com.team.eatcleanapp.domain.usecase.user

import com.team.eatcleanapp.domain.model.nutrition.MealIntake
import com.team.eatcleanapp.domain.repository.MealIntakeRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class SaveMealIntakeUseCase @Inject constructor(
    private val mealIntakeRepository: MealIntakeRepository
) {
    suspend operator fun invoke(mealIntake: MealIntake): Result<Unit> {
        return mealIntakeRepository.saveMealIntake(mealIntake)
    }
}