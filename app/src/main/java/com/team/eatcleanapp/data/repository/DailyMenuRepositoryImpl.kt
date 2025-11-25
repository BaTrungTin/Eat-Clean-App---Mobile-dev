package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.DailyMenuDao
import com.team.eatcleanapp.data.local.entities.DailyMenuEntity
import com.team.eatcleanapp.domain.model.DailyMenu
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.DateUtils
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.Date

class DailyMenuRepositoryImpl(
    private val dailyMenuDao: DailyMenuDao
) : DailyMenuRepository {
    
    override fun getDailyMenu(userId: String, date: Date): Flow<Result<List<DailyMenu>>> {
        val dateTimestamp = DateUtils.getStartOfDay(date).time
        return dailyMenuDao.getMenuByDate(userId, dateTimestamp)
            .map { entities ->
                Result.Success(entities.map { it.toDomainModel() }) as Result<List<DailyMenu>>
            }
            .catch { e ->
                emit(Result.Error(e))
            }
    }
    
    override fun getWeeklyMenu(userId: String, startDate: Date): Flow<Result<List<DailyMenu>>> {
        val startTimestamp = DateUtils.getStartOfDay(startDate).time
        val endDate = DateUtils.addDays(startDate, 7)
        val endTimestamp = DateUtils.getStartOfDay(endDate).time
        
        return dailyMenuDao.getWeeklyMenu(userId, startTimestamp, endTimestamp)
            .map { entities ->
                Result.Success(entities.map { it.toDomainModel() }) as Result<List<DailyMenu>>
            }
            .catch { e ->
                emit(Result.Error(e))
            }
    }
    
    override suspend fun addMealToDailyMenu(
        userId: String,
        date: Date,
        mealId: String,
        mealName: String,
        calories: Double,
        mealType: String,
        protein: Double?,
        carbs: Double?,
        fat: Double?
    ): Result<Long> {
        return try {
            val dateTimestamp = DateUtils.getStartOfDay(date).time
            val entity = DailyMenuEntity(
                userId = userId,
                date = dateTimestamp,
                mealId = mealId,
                mealName = mealName,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat,
                mealType = mealType,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            val id = dailyMenuDao.insertMeal(entity)
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun addMealsToDailyMenu(
        userId: String,
        date: Date,
        meals: List<DailyMenuRepository.MealToAdd>
    ): Result<List<Long>> {
        return try {
            val dateTimestamp = DateUtils.getStartOfDay(date).time
            val currentTime = System.currentTimeMillis()
            val entities = meals.map { meal ->
                DailyMenuEntity(
                    userId = userId,
                    date = dateTimestamp,
                    mealId = meal.mealId,
                    mealName = meal.mealName,
                    calories = meal.calories,
                    protein = meal.protein,
                    carbs = meal.carbs,
                    fat = meal.fat,
                    mealType = meal.mealType,
                    createdAt = currentTime,
                    updatedAt = currentTime
                )
            }
            val ids = dailyMenuDao.insertMeals(entities)
            Result.Success(ids)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateMealIntake(mealIntakeId: Long, isConsumed: Boolean): Result<Unit> {
        return try {
            dailyMenuDao.updateMealIntake(mealIntakeId, isConsumed)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun deleteMeal(mealId: Long): Result<Unit> {
        return try {
            dailyMenuDao.deleteMealById(mealId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun deleteAllMealsByDate(userId: String, date: Date): Result<Unit> {
        return try {
            val dateTimestamp = DateUtils.getStartOfDay(date).time
            dailyMenuDao.deleteAllMealsByDate(userId, dateTimestamp)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    private fun DailyMenuEntity.toDomainModel(): DailyMenu {
        return DailyMenu(
            id = id.toString(),
            userId = userId,
            date = Date(date),
            mealId = mealId,
            mealName = mealName,
            calories = calories,
            mealType = mealType,
            protein = protein,
            carbs = carbs,
            fat = fat,
            createdAt = Date(createdAt),
            updatedAt = Date(updatedAt)
        )
    }
}
