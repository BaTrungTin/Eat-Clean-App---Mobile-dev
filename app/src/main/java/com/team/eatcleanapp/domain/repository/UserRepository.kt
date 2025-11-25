package com.team.eatcleanapp.domain.repository

import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.util.Result

interface UserRepository {

    // lay thong tin user theo ID
    suspend fun getUserById(userId: String): Result<User>

    // lay thong tin user theo email
    suspend fun getUserByEmail(email: String): Result<User?>

    // luu thong tin user vao db
    suspend fun saveUser(user: User): Result<Unit>

    // cap nhat thong tin user profile
    suspend fun updateUser(user: User): Result<Unit>

    // xoa user data
    suspend fun deleteUser(userId: String): Result<Unit>

    // kiem tra email da ton tai trong local db chua
    suspend fun checkEmailExists(email: String): Result<Boolean>
}