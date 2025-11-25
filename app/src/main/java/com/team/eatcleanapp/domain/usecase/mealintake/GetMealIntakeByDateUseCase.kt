package com.team.eatcleanapp.domain.usecase.user

import com.team.eatcleanapp.domain.model.nutrition.MealIntake
import com.team.eatcleanapp.domain.repository.MealIntakeRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject

class GetMealIntakeByDateUseCase @Inject constructor(
    private val mealIntakeRepository: MealIntakeRepository
) {
    suspend operator fun invoke(userId: String, date: Date): Result<List<MealIntake>> {
        return mealIntakeRepository.getMealIntakeByDate(userId, date)
    }
}