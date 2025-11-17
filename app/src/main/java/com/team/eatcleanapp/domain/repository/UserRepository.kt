package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.User
import com.team.eatcleanapp.util.Result

interface UserRepository {
    suspend fun registerUser(user: User): Result<User>
    suspend fun loginUser(email: String, password: String): Result<User>
    suspend fun getUserById(userId: String): Result<User>
    suspend fun updateUser(user: User): Result<User>
    suspend fun deleteUser(userId: String): Result<Boolean>
    suspend fun checkEmailExists(email: String): Result<Boolean>
    suspend fun logout(): Result<Unit>
}