package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.User
import com.team.eatcleanapp.util.Result

interface UserRepository {
    suspend fun createUser(user: User): Result<User>

    suspend fun getUserById(userId: String): Result<User>

    // Lấy user bằng email (cho login)
    suspend fun getUserByEmail(email: String): Result<User?>

    suspend fun updateUser(user: User): Result<User>

    // Xóa user
    suspend fun deleteUser(userId: String): Result<Boolean>

    // Lưu user (sau khi login/register)
    suspend fun saveUser(user: User): Result<Unit>

    // Xóa user local (khi logout)
    suspend fun clearLocalUser(): Result<Unit>

    // Lấy user hiện tại (từ local)
    suspend fun getCurrentUser(): Result<User?>

    suspend fun checkEmailExists(email: String): Result<Boolean>
}