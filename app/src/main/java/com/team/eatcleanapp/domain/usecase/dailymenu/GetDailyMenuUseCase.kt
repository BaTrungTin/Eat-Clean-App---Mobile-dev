package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import java.util.Date

class GetDailyMenuUseCase(
    private val repository: DailyMenuRepository
) {
    suspend operator fun invoke(
        userId: String,
        date: Date
    ): Result<List<DailyMenuItem>> {
        if (userId.isBlank()) {
            return Result.Error(IllegalArgumentException("User ID khong duoc de trong"))
        }

        return try {
            val result = repository.getDailyMenuByDate(userId, date)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}