package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.MealIntakeDao
import com.team.eatcleanapp.data.local.entities.MealIntakeEntity
import com.team.eatcleanapp.domain.model.MealIntake
import com.team.eatcleanapp.domain.repository.MealIntakeRepository
import com.team.eatcleanapp.util.DateUtils
import com.team.eatcleanapp.util.Result
import java.util.*

class MealIntakeRepositoryImpl(
    private val mealIntakeDao: MealIntakeDao
) : MealIntakeRepository {

    override suspend fun getMealIntakeByDate(userId: String, date: Date): Result<List<MealIntake>> {
        return try {
            val dateString = DateUtils.formatDate(date)
            val entities = mealIntakeDao.getMealIntakeByDate(userId, dateString)
            val mealIntakes = entities.map { it.toDomain() }
            Result.Success(mealIntakes)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getMealIntakeByDateAndCategory(
        userId: String,
        date: Date,
        category: String
    ): Result<List<MealIntake>> {
        return try {
            val dateString = DateUtils.formatDate(date)
            val entities = mealIntakeDao.getMealIntakeByDateAndCategory(userId, dateString, category)
            val mealIntakes = entities.map { it.toDomain() }
            Result.Success(mealIntakes)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getMealIntakeByMealId(
        userId: String,
        date: Date,
        mealId: String,
        category: String
    ): Result<MealIntake?> {
        return try {
            val dateString = DateUtils.formatDate(date)
            val entity = mealIntakeDao.getMealIntakeByMealId(userId, dateString, mealId, category)
            val mealIntake = entity?.toDomain()
            Result.Success(mealIntake)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun upsertMealIntake(mealIntake: MealIntake): Result<Unit> {
        return try {
            val entity = mealIntake.toEntity()
            mealIntakeDao.upsert(entity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateCheckedStatus(id: String, isChecked: Boolean): Result<Unit> {
        return try {
            mealIntakeDao.updateCheckedStatus(id, isChecked)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateCheckedStatusByDailyMenuItemId(
        dailyMenuItemId: String,
        isChecked: Boolean
    ): Result<Unit> {
        return try {
            mealIntakeDao.updateCheckedStatusByDailyMenuItemId(dailyMenuItemId, isChecked)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun clearLocalData(): Result<Unit> {
        return try {
            mealIntakeDao.clearAllLocalData()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun clearLocalDataByUserId(userId: String): Result<Unit> {
        return try {
            mealIntakeDao.clearLocalDataByUserId(userId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun MealIntakeEntity.toDomain(): MealIntake {
        val parsedDate = DateUtils.parseDate(date) ?: Date()

        return MealIntake(
            id = id,
            userId = userId,
            dailyMenuItemId = dailyMenuItemId,
            date = parsedDate,
            mealId = mealId,
            mealName = mealName,
            category = com.team.eatcleanapp.domain.model.dailymenu.MealCategory.valueOf(category),
            calories = calories,
            quantity = quantity,
            unit = unit,
            isChecked = isChecked
        )
    }

    private fun MealIntake.toEntity(): MealIntakeEntity = MealIntakeEntity(
        id = id,
        userId = userId,
        dailyMenuItemId = dailyMenuItemId,
        date = DateUtils.formatDate(date),
        mealId = mealId,
        mealName = mealName,
        category = category.name,
        calories = calories,
        quantity = quantity,
        unit = unit,
        isChecked = isChecked
    )
}