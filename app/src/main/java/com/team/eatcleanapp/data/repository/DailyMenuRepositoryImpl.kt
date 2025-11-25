package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.DailyMenuDao
import com.team.eatcleanapp.data.mapper.DailyMenuMapper.toDomain
import com.team.eatcleanapp.data.mapper.DailyMenuMapper.toEntity
import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class DailyMenuRepositoryImpl @Inject constructor(
    private val dailyMenuDao: DailyMenuDao
) : DailyMenuRepository {

    override fun getDailyMenuByDate(
        userId: String,
        date: Date
    ): Flow<Result<List<DailyMenuItem>>> {
        return dailyMenuDao.getDailyMenuByDate(userId, date.time)
            .map { entities ->
                Result.Success(entities.map { it.toDomain() })
            }
    }

    override suspend fun getWeeklyMenu(
        userId: String,
        startDate: Date,
        endDate: Date
    ): Result<List<DailyMenuItem>> {
        return try {
            android.util.Log.d("DailyMenuRepository", "Getting weekly menu: userId=$userId, startDate=${startDate.time}, endDate=${endDate.time}")
            val entities = dailyMenuDao.getWeeklyMenuSuspend(
                userId,
                startDate.time,
                endDate.time
            )
            android.util.Log.d("DailyMenuRepository", "Found ${entities.size} meals in weekly range")
            entities.forEach { entity ->
                android.util.Log.d("DailyMenuRepository", "Meal: date=${entity.date}, mealId=${entity.mealId}, mealType=${entity.mealType}, mealName=${entity.mealName}")
            }
            Result.Success(entities.map { it.toDomain() })
        } catch (e: Exception) {
            android.util.Log.e("DailyMenuRepository", "Error getting weekly menu: ${e.message}", e)
            Result.Error(e, "Lỗi khi lấy thực đơn tuần")
        }
    }

    override suspend fun getDailyMenuItem(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory
    ): Result<DailyMenuItem?> {
        return try {
            val entity = dailyMenuDao.getDailyMenuItem(
                userId,
                date.time,
                mealId,
                mealType.name
            )
            Result.Success(entity?.toDomain())
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lấy món ăn từ thực đơn")
        }
    }

    override suspend fun insertDailyMenus(items: List<DailyMenuItem>): Result<Unit> {
        return try {
            val entities = items.map { it.toEntity() }
            dailyMenuDao.insertMeals(entities)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi thêm món vào thực đơn")
        }
    }

    override suspend fun updateDailyMenu(item: DailyMenuItem): Result<Unit> {
        return try {
            val entity = item.toEntity()
            dailyMenuDao.updateMeal(entity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi cập nhật thực đơn")
        }
    }

    override suspend fun deleteSpecificMeal(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory
    ): Result<Unit> {
        return try {
            // Normalize date to start of day (00:00:00) to ensure exact match
            val calendar = java.util.Calendar.getInstance().apply {
                time = date
                set(java.util.Calendar.HOUR_OF_DAY, 0)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }
            val normalizedDate = calendar.timeInMillis
            
            android.util.Log.d("DailyMenuRepository", "Deleting meal: userId=$userId, originalDate=${date.time}, normalizedDate=$normalizedDate, mealId=$mealId, mealType=${mealType.name}")
            
            // First, get all meals with same mealId and mealType on this date (to handle date mismatches)
            val allMealsOnDate = dailyMenuDao.getMealsByType(userId, normalizedDate, mealType.name)
            val matchingMeals = allMealsOnDate.filter { it.mealId == mealId }
            android.util.Log.d("DailyMenuRepository", "Found ${matchingMeals.size} matching meals on this date (total meals on date: ${allMealsOnDate.size})")
            
            if (matchingMeals.isEmpty()) {
                android.util.Log.w("DailyMenuRepository", "No matching meals found to delete! Checking with original date...")
                // Try with original date in case normalization caused issues
                val allMealsOriginalDate = dailyMenuDao.getMealsByType(userId, date.time, mealType.name)
                val matchingMealsOriginal = allMealsOriginalDate.filter { it.mealId == mealId }
                android.util.Log.d("DailyMenuRepository", "Found ${matchingMealsOriginal.size} matching meals with original date")
                matchingMealsOriginal.forEach { mealEntity ->
                    android.util.Log.d("DailyMenuRepository", "Deleting meal entity (original date): date=${mealEntity.date}, mealId=${mealEntity.mealId}, mealType=${mealEntity.mealType}")
                    dailyMenuDao.deleteMeal(mealEntity)
                }
            } else {
                // Delete all matching meals (in case there are duplicates or date mismatches)
                matchingMeals.forEach { mealEntity ->
                    android.util.Log.d("DailyMenuRepository", "Deleting meal entity: date=${mealEntity.date}, mealId=${mealEntity.mealId}, mealType=${mealEntity.mealType}, userId=${mealEntity.userId}")
                    try {
                        dailyMenuDao.deleteMeal(mealEntity)
                        android.util.Log.d("DailyMenuRepository", "Successfully deleted entity using deleteMeal()")
                    } catch (e: Exception) {
                        android.util.Log.e("DailyMenuRepository", "Error deleting entity: ${e.message}", e)
                    }
                }
            }
            
            // Also try the direct delete query as backup (with both normalized and original date)
            val deletedCount1 = dailyMenuDao.deleteDailyMenuItem(userId, normalizedDate, mealId, mealType.name)
            val deletedCount2 = dailyMenuDao.deleteDailyMenuItem(userId, date.time, mealId, mealType.name)
            android.util.Log.d("DailyMenuRepository", "Direct delete queries: normalizedDate deleted $deletedCount1 rows, originalDate deleted $deletedCount2 rows")
            
            // Verify deletion
            val remainingMeals = dailyMenuDao.getMealsByType(userId, normalizedDate, mealType.name)
            val remainingMatching = remainingMeals.filter { it.mealId == mealId }
            android.util.Log.d("DailyMenuRepository", "After deletion: ${remainingMatching.size} matching meals remaining (should be 0), total meals on date: ${remainingMeals.size}")
            
            if (remainingMatching.isNotEmpty()) {
                android.util.Log.w("DailyMenuRepository", "Still have ${remainingMatching.size} matching meals! Trying to delete again...")
                remainingMatching.forEach { mealEntity ->
                    android.util.Log.d("DailyMenuRepository", "Force deleting: date=${mealEntity.date}, mealId=${mealEntity.mealId}, mealType=${mealEntity.mealType}")
                    dailyMenuDao.deleteMeal(mealEntity)
                }
                // Try one more time with direct query
                val retryCount1 = dailyMenuDao.deleteDailyMenuItem(userId, normalizedDate, mealId, mealType.name)
                val retryCount2 = dailyMenuDao.deleteDailyMenuItem(userId, date.time, mealId, mealType.name)
                android.util.Log.d("DailyMenuRepository", "Retry delete queries: normalizedDate deleted $retryCount1 rows, originalDate deleted $retryCount2 rows")
            }
            
            // Final verification
            val finalCheck = dailyMenuDao.getMealsByType(userId, normalizedDate, mealType.name)
            val finalMatching = finalCheck.filter { it.mealId == mealId }
            android.util.Log.d("DailyMenuRepository", "Final check: ${finalMatching.size} matching meals remaining, total meals on date: ${finalCheck.size}")
            
            if (finalMatching.isEmpty()) {
                android.util.Log.d("DailyMenuRepository", "Meal deleted successfully")
            } else {
                android.util.Log.w("DailyMenuRepository", "WARNING: Meal deletion may have failed! Still have ${finalMatching.size} matching meals")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("DailyMenuRepository", "Error deleting meal: ${e.message}", e)
            Result.Error(e, "Lỗi khi xóa món ăn cụ thể: ${e.message}")
        }
    }

    override suspend fun deleteMeals(
        userId: String,
        dateMealTypes: Map<Date, List<MealCategory>?>
    ): Result<Unit> {
        return try {
            for ((date, mealCategories) in dateMealTypes) {
                val mealTypes = mealCategories?.map { it.name }
                when {
                    mealTypes == null ->
                        dailyMenuDao.deleteAllByDate(userId, date.time)
                    mealTypes.size == 1 ->
                        dailyMenuDao.deleteMealTypeOfDate(userId, date.time, mealTypes[0])
                    else ->
                        dailyMenuDao.deleteMultipleMealTypesOfDate(userId, date.time, mealTypes)
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa món khỏi lịch ăn")
        }
    }

    override suspend fun getTotalCaloriesByDate(
        userId: String,
        date: Date
    ): Result<Double?> {
        return try {
            val total = dailyMenuDao.getTotalCaloriesByDate(userId, date.time)
            Result.Success(total)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi tính tổng calories")
        }
    }

    override suspend fun getTotalCaloriesByType(
        userId: String,
        date: Date,
        mealType: MealCategory
    ): Result<Double?> {
        return try {
            val total = dailyMenuDao.getTotalCaloriesByMealType(userId, date.time, mealType.name)
            Result.Success(total)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi tính tổng calories theo buổi")
        }
    }

    override suspend fun getMealCountByType(
        userId: String,
        date: Date,
        mealType: MealCategory
    ): Result<Int> {
        return try {
            val count = dailyMenuDao.getMealCountByType(userId, date.time, mealType.name)
            Result.Success(count)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi đếm số món ăn")
        }
    }

    override suspend fun updateDailyMenuMealInfo(
        mealId: String,
        newMealName: String,
        newCalories: Double
    ): Result<Unit> {
        return try {
            dailyMenuDao.updateMealInfo(mealId, newMealName, newCalories)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi cập nhật thông tin món ăn")
        }
    }

    override suspend fun mealExistsInMenu(
        userId: String,
        date: Date,
        mealId: String,
        mealType: MealCategory
    ): Result<Boolean> {
        return try {
            val exists = dailyMenuDao.mealExists(userId, date.time, mealId, mealType.name)
            Result.Success(exists)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi kiểm tra món ăn")
        }
    }

    override suspend fun deleteDailyMenuItemByMealId(mealId: String): Result<Unit> {
        return try {
            dailyMenuDao.deleteMealsByMealId(mealId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa món ăn theo mealId")
        }
    }

    override suspend fun deleteAllByUserId(userId: String): Result<Unit> {
        return try {
            dailyMenuDao.deleteAllByUserId(userId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa toàn bộ thực đơn")
        }
    }

    override suspend fun getAllUsedMealIds(userId: String): Result<List<String>> {
        return try {
            val list = dailyMenuDao.getAllUsedMealIds(userId)
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lấy danh sách mealId")
        }
    }
}