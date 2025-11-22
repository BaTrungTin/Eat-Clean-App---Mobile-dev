package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result

/**
 * Use case de xoa nhieu mon an theo danh sach ID
 * Su dung khi xoa nhieu mon da chon tu man hinh dailymenu
 */
class DeleteMultipleMealsUseCase(
    private val dailyMenuRepository: DailyMenuRepository
) {
    data class Params(
        val mealIds: List<String>
    )

    suspend operator fun invoke(params: Params): Result<Unit> {
        // Kiem tra
        if (params.mealIds.isEmpty()) {
            return Result.Error(IllegalArgumentException("Danh sach mon an khong duoc de trong"))
        }

        // Xoa tung mon
        var lastError: Result.Error? = null
        for (mealId in params.mealIds) {
            val result = dailyMenuRepository.deleteDailyMenuItem(mealId)
            if (result is Result.Error) {
                lastError = result
            }
        }

        return lastError ?: Result.Success(Unit)
    }
}

