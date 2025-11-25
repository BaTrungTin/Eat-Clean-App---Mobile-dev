package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.util.Result

class LogoutUseCase {
    suspend fun execute(): Result<Unit> {
        return try {
            // Clear user session
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

