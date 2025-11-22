package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.util.Result

/**
 * Use case de dong bo daily menu khi meal thay doi tu Firestore
 * Su dung khi meal duoc cap nhat hoac xoa tu Firestore
 * - Cap nhat ten va calo trong daily menu
 * - Xoa tat ca daily menu items co mealId do
 */
class SyncDailyMenuUseCase(
    private val dailyMenuRepository: DailyMenuRepository,
    private val mealRepository: MealRepository
) {
    
    /**
     * Cap nhat daily menu khi meal thay doi
     */
    suspend fun syncMealUpdate(meal: Meal): Result<Unit> {
        return try {
            // Cap nhat ten va calo trong tat ca daily menu items co mealId nay
            val result = dailyMenuRepository.updateDailyMenuMealInfo(
                mealId = meal.id,
                newMealName = meal.name,
                newCalories = meal.calories
            )
            result
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Xoa daily menu items khi meal bi xoa
     */
    suspend fun syncMealDelete(mealId: String): Result<Unit> {
        return try {
            // Xoa tat ca daily menu items co mealId nay
            dailyMenuRepository.deleteDailyMenuByMealId(mealId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Dong bo tat ca daily menu voi meal changes
     */
    suspend fun syncAllDailyMenus(): Result<Unit> {
        return try {
            // Lay tat ca mealId dang duoc dung trong daily menu
            val usedMealIdsResult = dailyMenuRepository.getAllUsedMealIds()
            
            if (usedMealIdsResult is Result.Success) {
                val usedMealIds = usedMealIdsResult.data
                
                // Kiem tra tung mealId xem con ton tai khong
                for (mealId in usedMealIds) {
                    val mealExistsResult = mealRepository.mealExists(mealId)
                    
                    if (mealExistsResult is Result.Success && !mealExistsResult.data) {
                        // Meal khong con ton tai, xoa khoi daily menu
                        dailyMenuRepository.deleteDailyMenuByMealId(mealId)
                    } else if (mealExistsResult is Result.Success && mealExistsResult.data) {
                        // Meal con ton tai, cap nhat thong tin
                        val mealResult = mealRepository.getMealById(mealId)
                        if (mealResult is Result.Success && mealResult.data != null) {
                            val meal = mealResult.data!!
                            dailyMenuRepository.updateDailyMenuMealInfo(
                                mealId = meal.id,
                                newMealName = meal.name,
                                newCalories = meal.calories
                            )
                        }
                    }
                }
                
                Result.Success(Unit)
            } else {
                Result.Error(Exception("Khong the lay danh sach mealId trong daily menu"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

