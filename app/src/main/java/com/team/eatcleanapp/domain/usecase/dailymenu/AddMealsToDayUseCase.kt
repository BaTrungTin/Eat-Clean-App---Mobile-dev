package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject

class AddMealToDailyMenuUseCase @Inject constructor(
    private val dailyMenuRepository: DailyMenuRepository,
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory,
        portionSize: Double = 1.0
    ): Result<Unit> {
        if (portionSize <= 0) return Result.Error(message = "Khẩu phần phải lớn hơn 0")
        if (portionSize > 10) return Result.Error(message = "Khẩu phần quá lớn")

        // Normalize date to start of day to ensure consistency with delete operations
        val calendar = java.util.Calendar.getInstance().apply {
            time = date
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        val normalizedDate = calendar.time
        
        android.util.Log.d("AddMealToDailyMenuUseCase", "Adding meal: userId=$userId, originalDate=${date.time}, normalizedDate=${normalizedDate.time}, mealId=$mealId, mealType=$mealType, portionSize=$portionSize")

        val existsResult = dailyMenuRepository.mealExistsInMenu(userId, normalizedDate, mealId, mealType)
        if (existsResult.isSuccess && existsResult.getOrThrow() == true) {
            android.util.Log.w("AddMealToDailyMenuUseCase", "Meal already exists in menu")
            return Result.Error(message = "Món ăn đã tồn tại trong thực đơn")
        }

        val mealResult = mealRepository.getMealDetail(mealId)
        if (mealResult.isError || mealResult.getOrNull() == null) {
            android.util.Log.e("AddMealToDailyMenuUseCase", "Meal not found: $mealId")
            return Result.Error(message = "Không tìm thấy món ăn")
        }

        val meal = mealResult.getOrThrow()!!
        val dailyMenuItem = DailyMenuItem(
            userId = userId,
            date = normalizedDate, // Use normalized date to ensure consistency
            mealId = mealId,
            mealName = meal.name,
            calories = meal.calories,
            mealType = mealType,
            portionSize = portionSize
        )

        android.util.Log.d("AddMealToDailyMenuUseCase", "Inserting meal: date=${dailyMenuItem.date.time}, mealName=${dailyMenuItem.mealName}")
        val result = dailyMenuRepository.insertDailyMenus(listOf(dailyMenuItem))
        android.util.Log.d("AddMealToDailyMenuUseCase", "Insert result: isSuccess=${result.isSuccess}")
        return result
    }
}