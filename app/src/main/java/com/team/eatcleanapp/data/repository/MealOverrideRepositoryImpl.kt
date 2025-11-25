package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.MealOverrideDao
import com.team.eatcleanapp.data.mapper.MealOverrideMapper.applyOverride
import com.team.eatcleanapp.data.mapper.MealOverrideMapper.toOverrideEntity
import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.domain.repository.MealOverrideRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class MealOverrideRepositoryImpl @Inject constructor(
    private val mealOverrideDao: MealOverrideDao
) : MealOverrideRepository {

    override suspend fun getMealOverride(userId: String, mealId: String): Result<com.team.eatcleanapp.data.local.entities.MealOverrideEntity?> {
        return try {
            Result.Success(mealOverrideDao.getMealOverride(userId, mealId))
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lấy meal override")
        }
    }

    override suspend fun saveMealOverride(userId: String, originalMeal: Meal, modifiedMeal: Meal): Result<Unit> {
        return try {
            val entity = modifiedMeal.toOverrideEntity(originalMeal, userId)
            mealOverrideDao.upsertMealOverride(entity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lưu meal override")
        }
    }

    override suspend fun removeMealOverride(userId: String, mealId: String): Result<Unit> {
        return try {
            mealOverrideDao.deleteMealOverride(userId, mealId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa meal override")
        }
    }

    override suspend fun hasMealOverride(userId: String, mealId: String): Result<Boolean> {
        return try {
            Result.Success(mealOverrideDao.mealOverrideExists(userId, mealId))
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi kiểm tra meal override")
        }
    }

    override suspend fun getMealWithOverride(userId: String, meal: Meal): Result<Meal> {
        return try {
            val overrideEntity = mealOverrideDao.getMealOverride(userId, meal.id)
            Result.Success(meal.applyOverride(overrideEntity))
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi áp dụng meal override")
        }
    }
}
