package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.data.local.dao.DailyMenuDao
import com.team.eatcleanapp.data.local.dao.FavoriteDao
import com.team.eatcleanapp.util.Result

class DeleteAccountUseCase(
    private val dailyMenuDao: DailyMenuDao,
    private val favoriteDao: FavoriteDao
) {
    suspend fun execute(userId: String): Result<Unit> {
        return try {
            // Delete all user data from database
            dailyMenuDao.deleteAllMealsByUserId(userId)
            favoriteDao.deleteAllFavoritesByUserId(userId)
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

