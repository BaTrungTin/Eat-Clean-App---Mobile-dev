package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuDay
import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class GetDailyMenuUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository
) {
    operator fun invoke(userId: String, date: Date): Flow<Result<DailyMenuDay>> {
        return dailyMenuRepository.getDailyMenuByDate(userId, date).map { repositoryResult ->
            when (repositoryResult) {
                is Result.Success -> {
                    val meals = repositoryResult.data
                    val dailyMenuDay = organizeMealsByType(date, meals)
                    Result.Success(dailyMenuDay)
                }
                is Result.Error -> Result.Error(
                    exception = repositoryResult.exception,
                    message = repositoryResult.message
                )
                is Result.Loading -> Result.Loading
                is Result.Idle -> Result.Idle
            }
        }
    }

    private fun organizeMealsByType(date: Date, meals: List<DailyMenuItem>): DailyMenuDay {
        val breakfast = meals.filter { it.mealType == MealCategory.BREAKFAST }
        val lunch = meals.filter { it.mealType == MealCategory.LUNCH }
        val dinner = meals.filter { it.mealType == MealCategory.DINNER }
        return DailyMenuDay(date = date, breakfast = breakfast, lunch = lunch, dinner = dinner)
    }
}