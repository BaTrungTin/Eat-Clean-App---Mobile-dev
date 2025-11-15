package com.team.eatcleanapp.domain.usecase.auth

import android.content.Context
import android.content.SharedPreferences
import com.team.eatcleanapp.data.local.dao.DailyMenuDao
import com.team.eatcleanapp.data.local.dao.FavoriteDao
import com.team.eatcleanapp.util.Constants
import com.team.eatcleanapp.util.Result

class DeleteAccountUseCase(
    private val context: Context,
    private val dailyMenuDao: DailyMenuDao,
    private val favoriteDao: FavoriteDao
) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
    }
    
    suspend fun execute(userId: String): Result<Unit> {
        return try {
            // Delete all user data from database
            dailyMenuDao.deleteAllMealsByUserId(userId)
            favoriteDao.deleteAllFavoritesByUserId(userId)
            
            // Clear session data
            sharedPreferences.edit().apply {
                remove(Constants.PREF_USER_ID)
                remove(Constants.PREF_USER_EMAIL)
                putBoolean(Constants.PREF_IS_LOGGED_IN, false)
                apply()
            }
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

