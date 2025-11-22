package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.DateUtils
import com.team.eatcleanapp.util.Result
import java.util.*

class GetWeeklyMenuUseCase(
    private val repository: DailyMenuRepository
) {
    suspend operator fun invoke(
        userId: String,
        date: Date
    ): Result<Map<Date, List<DailyMenuItem>>> {
        // Kiem tra
        if (userId.isBlank()) {
            return Result.Error(IllegalArgumentException("User ID khong duoc de trong"))
        }

        return try {
            // Lay tuan hop le tu DateUtils
            val (startDate, endDate) = DateUtils.getValidWeekForDate(date)

            // Kiem tra tinh hop le cua tuan
            if (!DateUtils.isValidWeek(startDate, endDate)) {
                return Result.Error(IllegalArgumentException("Tuan khong hop le"))
            }

            // Lay thuc don tu repository
            val result = repository.getDailyMenuByWeek(userId, startDate, endDate)

            // Xu ly ket qua
            when (result) {
                is Result.Success -> {
                    val weeklyMenus = processWeeklyMenus(result.data)
                    Result.Success(weeklyMenus)
                }
                is Result.Error -> result
                is Result.Loading -> Result.Error(IllegalStateException("Dang tai thuc don tuan"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun processWeeklyMenus(dailyMenus: List<DailyMenuItem>): Map<Date, List<DailyMenuItem>> {
        // Nhom thuc don theo ngay (su dung start of day de dam bao cung ngay)
        return dailyMenus.groupBy { menu ->
            DateUtils.getStartOfDay(menu.date)
        }
    }
}