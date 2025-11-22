package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.dailymenu.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result
import java.util.Date

/**
 * Use case de them 1 mon an vao daily menu voi quantity tuy chinh
 * Su dung khi them mon tu man hinh mealdetail (popup chon ngay, buoi, so phan an)
 */
class AddMealToDailyMenuUseCase(
    private val dailyMenuRepository: DailyMenuRepository,
    private val mealRepository: MealRepository
) {
    data class Params(
        val userId: String,
        val date: Date,
        val mealId: String,
        val category: MealCategory,
        val quantity: Double
    )

    suspend operator fun invoke(params: Params): Result<Unit> {
        // Kiem tra
        if (params.userId.isBlank()) {
            return Result.Error(IllegalArgumentException("User ID khong duoc de trong"))
        }
        if (params.mealId.isBlank()) {
            return Result.Error(IllegalArgumentException("Meal ID khong duoc de trong"))
        }
        if (params.quantity <= 0) {
            return Result.Error(IllegalArgumentException("So phan an phai lon hon 0"))
        }

        // Lay thong tin mon an
        val mealResult = mealRepository.getMealById(params.mealId)
        return when (mealResult) {
            is Result.Success -> {
                val meal = mealResult.data
                if (meal != null) {
                    val dailyMenuItem = DailyMenuItem(
                        userId = params.userId,
                        date = params.date,
                        mealId = meal.id,
                        category = params.category,
                        mealName = meal.name,
                        calories = meal.calories,
                        quantity = params.quantity,
                        unit = "phần"
                    )
                    dailyMenuRepository.insertDailyMenu(dailyMenuItem)
                } else {
                    Result.Error(IllegalStateException("Khong tim thay mon an voi ID: ${params.mealId}"))
                }
            }
            is Result.Error -> Result.Error(mealResult.exception)
            is Result.Loading -> Result.Error(IllegalStateException("Dang tai thong tin mon an"))
        }
    }
}

