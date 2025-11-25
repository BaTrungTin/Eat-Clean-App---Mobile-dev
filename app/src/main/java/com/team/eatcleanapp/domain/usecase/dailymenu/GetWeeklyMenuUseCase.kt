package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuDay
import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuWeek
import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.DateUtils
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject

class GetWeeklyMenuUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository
) {
    suspend operator fun invoke(userId: String, weekStartDate: Date): Result<DailyMenuWeek> {
        val weekDates = DateUtils.getStandardWeekFromDate(weekStartDate)
        val startDate = weekDates.first()
        val endDate = weekDates.last()

        val result = dailyMenuRepository.getWeeklyMenu(userId, startDate, endDate)

        return when (result) {
            is Result.Success -> {
                val meals = result.data
                val dailyMenuDays = organizeWeeklyMeals(weekDates, meals)
                Result.Success(DailyMenuWeek(days = dailyMenuDays))
            }
            is Result.Error -> Result.Error(
                exception = result.exception,
                message = result.message
            )
            is Result.Loading -> Result.Loading
            is Result.Idle -> Result.Idle
        }
    }

    private fun organizeWeeklyMeals(
        weekDates: List<Date>,
        meals: List<DailyMenuItem>
    ): List<DailyMenuDay> {
        return weekDates.map { date ->
            val dayMeals = meals.filter { DateUtils.isSameDay(it.date, date) }
            organizeMealsByType(date, dayMeals)
        }
    }

    private fun organizeMealsByType(date: Date, meals: List<DailyMenuItem>): DailyMenuDay {
        val breakfast = meals.filter { it.mealType == MealCategory.BREAKFAST }
        val lunch = meals.filter { it.mealType == MealCategory.LUNCH }
        val dinner = meals.filter { it.mealType == MealCategory.DINNER }
        return DailyMenuDay(date = date, breakfast = breakfast, lunch = lunch, dinner = dinner)
    }
}