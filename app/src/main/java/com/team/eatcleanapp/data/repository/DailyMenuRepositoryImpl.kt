package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.DailyMenuDao
import com.team.eatcleanapp.data.local.entities.DailyMenuEntity
import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.dailymenu.DateCategory
import com.team.eatcleanapp.domain.model.dailymenu.MealCategory
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.DateUtils
import com.team.eatcleanapp.util.Result
import java.util.Date

class DailyMenuRepositoryImpl(
    private val dailyMenuDao: DailyMenuDao
) : DailyMenuRepository {

    override suspend fun getDailyMenuItemById(id: String): Result<DailyMenuItem?> {
        return executeDatabaseOperation {
            dailyMenuDao.getDailyMenuItemById(id)?.toDomain()
        }
    }

    override suspend fun getDailyMenuByDate(userId: String, date: Date): List<DailyMenuItem> {
        return when (val result = executeDatabaseOperation {
            val dateString = DateUtils.formatDate(date)
            dailyMenuDao.getDailyMenuByDate(userId, dateString).map { it.toDomain() }
        }) {
            is Result.Success -> result.data
            is Result.Error -> emptyList()
            Result.Loading -> emptyList()
        }
    }

    override suspend fun getDailyMenuByWeek(userId: String, startDate: Date, endDate: Date): Result<List<DailyMenuItem>> {
        return executeDatabaseOperation {
            val (standardStartDate, standardEndDate) = DateUtils.getValidWeekForDate(startDate)
            if (!DateUtils.isValidWeek(standardStartDate, standardEndDate)) {
                throw IllegalArgumentException("Tuần không hợp lệ")
            }

            val startDateString = DateUtils.formatDate(standardStartDate)
            val endDateString = DateUtils.formatDate(standardEndDate)
            dailyMenuDao.getDailyMenusByDateRange(userId, startDateString, endDateString)
                .map { it.toDomain() }
        }
    }

    override suspend fun getMealsByCategory(userId: String, date: Date, category: String): List<DailyMenuItem> {
        return when (val result = executeDatabaseOperation {
            val dateString = DateUtils.formatDate(date)
            dailyMenuDao.getMealsByCategory(userId, dateString, category).map { it.toDomain() }
        }) {
            is Result.Success -> result.data
            is Result.Error -> emptyList()
            Result.Loading -> emptyList()
        }
    }

    override suspend fun getTotalCaloriesByDate(userId: String, date: Date): Result<Double?> {
        return executeDatabaseOperation {
            val dateString = DateUtils.formatDate(date)
            dailyMenuDao.getTotalCaloriesByDate(userId, dateString)
        }
    }

    override suspend fun getTotalCaloriesByCategory(userId: String, date: Date, category: String): Result<Double?> {
        return executeDatabaseOperation {
            val dateString = DateUtils.formatDate(date)
            dailyMenuDao.getTotalCaloriesByCategoryAndDate(userId, dateString, category)
        }
    }

    override suspend fun mealExists(userId: String, date: Date, mealId: String, category: String): Result<Boolean> {
        return executeDatabaseOperation {
            val dateString = DateUtils.formatDate(date)
            dailyMenuDao.mealExists(userId, dateString, mealId, category) > 0
        }
    }

    override suspend fun getMealCountByCategory(userId: String, date: Date, category: String): Result<Int> {
        return executeDatabaseOperation {
            val dateString = DateUtils.formatDate(date)
            dailyMenuDao.getMealCountByCategoryAndDate(userId, dateString, category)
        }
    }

    override suspend fun insertDailyMenu(dailyMenuItem: DailyMenuItem): Result<Unit> {
        return executeDatabaseOperation {
            val entity = dailyMenuItem.toEntity()
            dailyMenuDao.insertMeal(entity)
        }
    }

    override suspend fun insertMultipleDailyMenus(dailyMenuItems: List<DailyMenuItem>): Result<Unit> {
        return executeDatabaseOperation {
            val entities = dailyMenuItems.map { it.toEntity() }
            dailyMenuDao.insertMeals(entities)
        }
    }

    override suspend fun updateDailyMenu(dailyMenuItem: DailyMenuItem): Result<Unit> {
        return executeDatabaseOperation {
            val entity = dailyMenuItem.toEntity()
            dailyMenuDao.updateMeal(entity)
        }
    }

    override suspend fun updateQuantity(id: String, newQuantity: Double): Result<Unit> {
        return executeDatabaseOperation {
            if (newQuantity <= 0) {
                throw IllegalArgumentException("Phần ăn phải lớn hơn 0")
            }
            dailyMenuDao.updateQuantity(id, newQuantity)
        }
    }

    override suspend fun deleteDailyMenuItem(id: String): Result<Unit> {
        return executeDatabaseOperation {
            dailyMenuDao.deleteMealById(id)
        }
    }

    override suspend fun deleteAllByDate(userId: String, date: Date): Result<Unit> {
        return executeDatabaseOperation {
            val dateString = DateUtils.formatDate(date)
            dailyMenuDao.deleteAllByDate(userId, dateString)
        }
    }

    override suspend fun deleteCategory(userId: String, date: Date, category: String): Result<Unit> {
        return executeDatabaseOperation {
            val dateString = DateUtils.formatDate(date)
            dailyMenuDao.deleteCategoryOfDate(userId, dateString, category)
        }
    }

    override suspend fun deleteMultipleCategoriesOfDate(userId: String, date: Date, categories: List<String>): Result<Unit> {
        return executeDatabaseOperation {
            if (categories.isEmpty()) return@executeDatabaseOperation
            val dateString = DateUtils.formatDate(date)
            dailyMenuDao.deleteMultipleCategoriesOfDate(userId, dateString, categories)
        }
    }

    override suspend fun deleteSelectedCategories(userId: String, selectedDateCategories: List<DateCategory>): Result<Unit> {
        return executeDatabaseOperation {
            if (selectedDateCategories.isEmpty()) return@executeDatabaseOperation

            if (DateUtils.isFullValidWeek(selectedDateCategories)) {
                deleteFullWeekCategories(userId, selectedDateCategories)
            } else {
                deleteDateCategoriesOneByOne(userId, selectedDateCategories)
            }
        }
    }

    override suspend fun deleteSelectedDates(userId: String, selectedDates: List<Date>): Result<Unit> {
        return executeDatabaseOperation {
            if (selectedDates.isEmpty()) return@executeDatabaseOperation

            val dateStrings = selectedDates.map { DateUtils.formatDate(it) }
            dailyMenuDao.deleteSelectedDates(userId, dateStrings)
        }
    }

    override suspend fun deleteWeek(userId: String, weekStartDate: Date, weekEndDate: Date): Result<Unit> {
        return executeDatabaseOperation {
            val (standardStartDate, standardEndDate) = DateUtils.getValidWeekForDate(weekStartDate)
            if (!DateUtils.isValidWeek(standardStartDate, standardEndDate)) {
                throw IllegalArgumentException("Tuần không hợp lệ")
            }

            val weekDates = DateUtils.getWeekForDate(standardStartDate)
            val dateStrings = weekDates.map { DateUtils.formatDate(it) }
            dailyMenuDao.deleteSelectedDates(userId, dateStrings)
        }
    }

    override suspend fun updateDailyMenuMealInfo(mealId: String, newMealName: String, newCalories: Double): Result<Unit> {
        return executeDatabaseOperation {
            dailyMenuDao.updateDailyMenuMealInfo(mealId, newMealName, newCalories)
        }
    }

    override suspend fun deleteDailyMenuByMealId(mealId: String): Result<Unit> {
        return executeDatabaseOperation {
            dailyMenuDao.deleteDailyMenuByMealId(mealId)
        }
    }

    override suspend fun getAllUsedMealIds(): Result<List<String>> {
        return executeDatabaseOperation {
            dailyMenuDao.getAllUsedMealIds()
        }
    }

    override suspend fun deleteAllByUserId(userId: String): Result<Unit> {
        return executeDatabaseOperation {
            dailyMenuDao.deleteAllByUserId(userId)
        }
    }

    private suspend fun <T> executeDatabaseOperation(operation: suspend () -> T): Result<T> {
        return try {
            Result.Success(operation())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun deleteFullWeekCategories(userId: String, dateCategories: List<DateCategory>) {
        val sortedCategories = DateUtils.getSortedWeekCategories(dateCategories)

        // Sử dụng phương thức mới trong DAO để xóa hiệu quả hơn
        val dates = sortedCategories.map { DateUtils.formatDate(it.date) }
        val categories = sortedCategories.map { it.category.toString() } // Sử dụng toString() thay vì name

        dailyMenuDao.deleteCategoriesAcrossDates(userId, dates, categories)
    }

    private suspend fun deleteDateCategoriesOneByOne(userId: String, dateCategories: List<DateCategory>) {
        // Nhóm theo ngày để thực hiện ít query hơn
        val groupedByDate = dateCategories.groupBy { it.date }

        groupedByDate.forEach { (date, categories) ->
            val dateString = DateUtils.formatDate(date)
            val categoryNames = categories.map { it.category.toString() } // Sử dụng toString() thay vì name

            if (categoryNames.size == 1) {
                // Nếu chỉ có 1 category trong ngày, xóa trực tiếp
                dailyMenuDao.deleteCategoryOfDate(userId, dateString, categoryNames.first())
            } else {
                // Nếu có nhiều categories trong cùng ngày, xóa cùng lúc
                dailyMenuDao.deleteMultipleCategoriesOfDate(userId, dateString, categoryNames)
            }
        }
    }

    private fun DailyMenuEntity.toDomain(): DailyMenuItem {
        return DailyMenuItem(
            id = this.id,
            userId = this.userId,
            date = DateUtils.parseDate(this.date) ?: Date(),
            mealId = this.mealId,
            category = MealCategory.valueOf(this.category), // Chuyển từ String sang enum
            mealName = this.mealName,
            calories = this.calories,
            quantity = this.quantity,
            unit = this.unit
        )
    }

    private fun DailyMenuItem.toEntity(): DailyMenuEntity {
        return DailyMenuEntity(
            id = this.id,
            userId = this.userId,
            date = DateUtils.formatDate(this.date),
            mealId = this.mealId,
            category = this.category.toString(), // Chuyển từ enum sang String
            mealName = this.mealName,
            calories = this.calories,
            quantity = this.quantity,
            unit = this.unit
        )
    }
}