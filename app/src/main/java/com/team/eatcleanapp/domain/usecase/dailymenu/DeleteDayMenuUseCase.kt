package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import java.util.Date

class DeleteDayMenuUseCase(
    private val dailyMenuRepository: DailyMenuRepository
) {

    data class Params(
        val userId: String,
        val date: Date,
        val isConfirmed: Boolean
    )

    suspend operator fun invoke(params: Params): Result<Unit> {
        // Kiem tra
        if (params.userId.isBlank()) {
            return Result.Error(IllegalArgumentException("User ID khong duoc de trong"))
        }
        if (!params.isConfirmed) {
            return Result.Error(ActionCancelledException("Hanh dong xoa da bi nguoi dung huy bo."))
        }

        return dailyMenuRepository.deleteAllByDate(params.userId, params.date)
    }
}

class ActionCancelledException(message: String) : Exception(message)