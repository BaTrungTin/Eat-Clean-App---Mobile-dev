package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.MealIntakeDao
import com.team.eatcleanapp.data.mapper.MealIntakeMapper.toDomain
import com.team.eatcleanapp.data.mapper.MealIntakeMapper.toEntity
import com.team.eatcleanapp.domain.model.nutrition.MealIntake
import com.team.eatcleanapp.domain.model.nutrition.NutritionInfo
import com.team.eatcleanapp.domain.repository.MealIntakeRepository
import com.team.eatcleanapp.util.Result
import java.util.Date
import javax.inject.Inject

class MealIntakeRepositoryImpl @Inject constructor(
    private val mealIntakeDao: MealIntakeDao
) : MealIntakeRepository {

    override suspend fun getMealIntakeByDate(
        userId: String,
        date: Date
    ): Result<List<MealIntake>> {
        return try {
            val entities = mealIntakeDao.getMealIntakeByDate(userId, date.time)
            Result.Success(entities.toDomain())
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lấy meal intake theo ngày")
        }
    }

    override suspend fun getMealIntakeById(id: String): Result<MealIntake?> {
        return try {
            val entity = mealIntakeDao.getMealIntakeById(id)
            Result.Success(entity?.toDomain())
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lấy meal intake theo ID")
        }
    }

    override suspend fun saveMealIntake(mealIntake: MealIntake): Result<Unit> {
        return try {
            val entity = mealIntake.toEntity()
            mealIntakeDao.upsertMealIntake(entity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lưu meal intake")
        }
    }

    override suspend fun updateConsumedStatus(
        id: String,
        isConsumed: Boolean
    ): Result<Unit> {
        return try {
            mealIntakeDao.updateConsumedStatus(id, isConsumed)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi cập nhật trạng thái consumed")
        }
    }

    override suspend fun updatePortionSize(
        id: String,
        portionSize: Double,
        totalCalories: Double
    ): Result<Unit> {
        return try {
            mealIntakeDao.updatePortion(id, portionSize, totalCalories)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi cập nhật portion size")
        }
    }

    override suspend fun getTotalConsumedCalories(
        userId: String,
        date: Date
    ): Result<Double?> {
        return try {
            val total = mealIntakeDao.getTotalConsumedCalories(userId, date.time)
            Result.Success(total)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi tính tổng calories consumed")
        }
    }

    override suspend fun getTotalPlannedCalories(
        userId: String,
        date: Date
    ): Result<Double> {
        return try {
            val total = mealIntakeDao.getTotalPlannedCalories(userId, date.time) ?: 0.0
            Result.Success(total)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi tính tổng calories planned")
        }
    }

    override suspend fun getCaloriesProgressPercentage(
        userId: String,
        date: Date
    ): Result<Double?> {
        return try {
            val percentage = mealIntakeDao.getCaloriesProgressPercentage(userId, date.time)
            Result.Success(percentage)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi tính % calories progress")
        }
    }

    override suspend fun deleteMealIntake(id: String): Result<Unit> {
        return try {
            mealIntakeDao.deleteMealIntakeById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa meal intake")
        }
    }

    override suspend fun deleteAllMealIntakeByDate(
        userId: String,
        date: Date
    ): Result<Unit> {
        return try {
            mealIntakeDao.deleteAllByDate(userId, date.time)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa tất cả meal intake theo ngày")
        }
    }

    override suspend fun deleteAllByUserId(userId: String): Result<Unit> {
        return try {
            mealIntakeDao.deleteAllByUserId(userId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa tất cả meal intake của user")
        }
    }
}