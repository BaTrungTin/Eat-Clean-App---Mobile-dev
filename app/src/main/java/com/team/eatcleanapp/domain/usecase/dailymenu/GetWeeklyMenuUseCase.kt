package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.DailyMenu
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.DateUtils
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.Date

class GetWeeklyMenuUseCase(
    private val repository: DailyMenuRepository
) {
    suspend operator fun invoke(
        userId: String,
        date: Date
    ): Map<Date, List<DailyMenu>> {
        if (userId.isBlank()) {
            return emptyMap()
        }

        return try {
            val calendar = Calendar.getInstance()
            calendar.time = date
            
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val daysToMonday = when (dayOfWeek) {
                Calendar.SUNDAY -> -6 
                else -> Calendar.MONDAY - dayOfWeek 
            }
            calendar.add(Calendar.DAY_OF_YEAR, daysToMonday)
            val startDate = DateUtils.getStartOfDay(calendar.time)
            
          
            calendar.add(Calendar.DAY_OF_YEAR, 6)
            val endDate = DateUtils.getEndOfDay(calendar.time)
            
         
            val result = repository.getWeeklyMenu(userId, startDate).first()
            val dailyMenus = when (result) {
                is com.team.eatcleanapp.util.Result.Success -> result.data
                else -> emptyList()
            }
            
          
            dailyMenus.groupBy { menu ->
                DateUtils.getStartOfDay(menu.date)
            }
        } catch (e: Exception) {
            emptyMap()
        }
    }
}

