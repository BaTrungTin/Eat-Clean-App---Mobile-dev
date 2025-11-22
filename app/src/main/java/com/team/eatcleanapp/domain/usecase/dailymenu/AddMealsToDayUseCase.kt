package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.dailymenu.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result
import java.util.Date

class AddMealsToDayUseCase(
    private val dailyMenuRepository: DailyMenuRepository,
    private val mealRepository: MealRepository
) {

    data class Params(
        val userId: String,
        val date: Date,
        val mealIds: List<String>,
        val mealCategory: MealCategory
    )

    suspend operator fun invoke(params: Params): Result<Unit> {
        if (params.mealIds.isEmpty()) {
            return Result.Error(IllegalArgumentException("Danh sach mon an khong duoc de trong"))
        }

        val dailyMenuItems = mutableListOf<DailyMenuItem>()

        for (mealId in params.mealIds) {
            val mealResult = mealRepository.getMealById(mealId)
            when (mealResult) {
                is Result.Success -> {
                    val meal = mealResult.data
                    if (meal != null) {
                        dailyMenuItems.add(
                            DailyMenuItem(
                                userId = params.userId,
                                date = params.date,
                                mealId = meal.id,
                                category = params.mealCategory,
                                mealName = meal.name,
                                calories = meal.calories,
                                quantity = 1.0,
                                unit = "phần"
                            )
                        )
                    } else {
                        return Result.Error(IllegalStateException("Không tìm thấy món ăn với ID: $mealId"))
                    }
                }
                is Result.Error -> {
                    return Result.Error(IllegalStateException("Lỗi khi lấy thông tin món ăn: ${mealResult.exception.message}"))
                }
                is Result.Loading -> {
                    return Result.Error(IllegalStateException("Đang tải thông tin món ăn"))
                }
            }
        }

        return dailyMenuRepository.insertMultipleDailyMenus(dailyMenuItems).let { result ->
            when (result) {
                is Result.Success -> Result.Success(Unit)
                is Result.Error -> Result.Error(result.exception)
                is Result.Loading -> Result.Error(IllegalStateException("Đang thêm món ăn vào thực đơn"))
            }
        }
    }
}