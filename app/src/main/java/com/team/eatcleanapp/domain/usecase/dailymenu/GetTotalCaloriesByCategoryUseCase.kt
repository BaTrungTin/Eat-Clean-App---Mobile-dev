package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.dailymenu.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import java.util.Date

/**
 * Use case de lay tong calo cua 1 buoi trong 1 ngay
 * Su dung de hien thi tong calo cua tung buoi (Sang, Trua, Toi)
 */
class GetTotalCaloriesByCategoryUseCase(
    private val repository: DailyMenuRepository
) {
    suspend operator fun invoke(
        userId: String,
        date: Date,
        category: MealCategory
    ): Result<Double?> {
        // Kiem tra
        if (userId.isBlank()) {
            return Result.Error(IllegalArgumentException("User ID khong duoc de trong"))
        }

        return repository.getTotalCaloriesByCategory(userId, date, category.toString())
    }
}

