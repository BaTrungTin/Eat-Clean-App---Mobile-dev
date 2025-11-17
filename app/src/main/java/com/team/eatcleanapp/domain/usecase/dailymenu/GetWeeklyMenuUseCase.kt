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
            // Tính toán startDate (thứ 2) và endDate (chủ nhật) của tuần chứa date
            val calendar = Calendar.getInstance()
            calendar.time = date
            
            // Lùi về thứ 2 (Calendar.MONDAY = 2, nhưng Calendar.DAY_OF_WEEK bắt đầu từ Calendar.SUNDAY = 1)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val daysToMonday = when (dayOfWeek) {
                Calendar.SUNDAY -> -6 // Nếu là chủ nhật, lùi 6 ngày về thứ 2
                else -> Calendar.MONDAY - dayOfWeek // Tính số ngày cần lùi về thứ 2
            }
            calendar.add(Calendar.DAY_OF_YEAR, daysToMonday)
            val startDate = DateUtils.getStartOfDay(calendar.time)
            
            // Tính endDate (chủ nhật) - thêm 6 ngày vào thứ 2
            calendar.add(Calendar.DAY_OF_YEAR, 6)
            val endDate = DateUtils.getEndOfDay(calendar.time)
            
            // Lấy weekly menu từ repository
            val result = repository.getWeeklyMenu(userId, startDate).first()
            val dailyMenus = when (result) {
                is com.team.eatcleanapp.util.Result.Success -> result.data
                else -> emptyList()
            }
            
            // Nhóm theo ngày
            dailyMenus.groupBy { menu ->
                DateUtils.getStartOfDay(menu.date)
            }
        } catch (e: Exception) {
            emptyMap()
        }
    }
}

