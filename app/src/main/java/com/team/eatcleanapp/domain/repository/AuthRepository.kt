package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.User
import com.team.eatcleanapp.util.Result

interface AuthRepository {
    suspend fun registerUser(email: String, password: String): Result<String>

    suspend fun completeRegistration(user: User): Result<User>

    suspend fun loginUser(email: String, password: String): Result<User>

    suspend fun logout(): Result<Unit>

    suspend fun deleteAccount(userId: String): Result<Unit>

    suspend fun getCurrentUser(): Result<User?>
}