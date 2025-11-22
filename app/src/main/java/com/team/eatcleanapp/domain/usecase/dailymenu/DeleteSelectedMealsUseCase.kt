package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.dailymenu.DateCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import java.util.Date

/**
 * Use case de xoa cac mon/ngay da chon tu man hinh dailymenu
 */
class DeleteSelectedMealsUseCase(
    private val dailyMenuRepository: DailyMenuRepository
) {
    data class Params(
        val userId: String,
        val mealIds: List<String> = emptyList(),
        val dateCategories: List<DateCategory> = emptyList(),
        val dates: List<Date> = emptyList()
    )

    suspend operator fun invoke(params: Params): Result<Unit> {
        // Kiem tra
        if (params.userId.isBlank()) {
            return Result.Error(IllegalArgumentException("User ID khong duoc de trong"))
        }
        if (params.mealIds.isEmpty() && params.dateCategories.isEmpty() && params.dates.isEmpty()) {
            return Result.Error(IllegalArgumentException("Phai chon it nhat mot muc de xoa"))
        }

        // Xoa cac mon theo ID
        if (params.mealIds.isNotEmpty()) {
            for (mealId in params.mealIds) {
                val result = dailyMenuRepository.deleteDailyMenuItem(mealId)
                if (result is Result.Error) {
                    return result
                }
            }
        }

        // Xoa theo DateCategory
        if (params.dateCategories.isNotEmpty()) {
            val result = dailyMenuRepository.deleteSelectedCategories(params.userId, params.dateCategories)
            if (result is Result.Error) {
                return result
            }
        }

        // Xoa theo danh sach ngay
        if (params.dates.isNotEmpty()) {
            val result = dailyMenuRepository.deleteSelectedDates(params.userId, params.dates)
            if (result is Result.Error) {
                return result
            }
        }

        return Result.Success(Unit)
    }
}

