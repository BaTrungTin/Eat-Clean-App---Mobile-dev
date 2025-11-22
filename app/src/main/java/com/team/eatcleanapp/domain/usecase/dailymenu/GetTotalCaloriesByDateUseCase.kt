package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import java.util.Date

/**
 * Use case de lay tong calo cua 1 ngay
 * Su dung de hien thi tong calo can nap / da nap / con lai trong ngay
 */
class GetTotalCaloriesByDateUseCase(
    private val repository: DailyMenuRepository
) {
    suspend operator fun invoke(
        userId: String,
        date: Date
    ): Result<Double?> {
        // Kiem tra
        if (userId.isBlank()) {
            return Result.Error(IllegalArgumentException("User ID khong duoc de trong"))
        }

        return repository.getTotalCaloriesByDate(userId, date)
    }
}

