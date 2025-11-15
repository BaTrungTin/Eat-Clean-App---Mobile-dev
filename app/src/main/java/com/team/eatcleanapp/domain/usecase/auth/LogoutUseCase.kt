package com.team.eatcleanapp.domain.usecase.auth

import android.content.Context
import android.content.SharedPreferences
import com.team.eatcleanapp.util.Constants
import com.team.eatcleanapp.util.Result

class LogoutUseCase(
    private val context: Context
) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
    }
    
    suspend fun execute(): Result<Unit> {
        return try {
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

