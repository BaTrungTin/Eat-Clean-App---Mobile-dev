package com.team.eatcleanapp.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.util.Result

interface AuthRepository {

    // dang ky user moi voi email va password
    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser>

    // dang nhap voi email va password
    suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser>

    // dang xuat
    suspend fun logout(): Result<Unit>

    // gui email reset password
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>

    // kiem tra user da dang nhap chua
    suspend fun getCurrentUser(): Result<FirebaseUser?>

    // cap nhat password
    suspend fun updatePassword(newPassword: String): Result<Unit>

    // cap nhat email
    suspend fun updateEmail(newEmail: String): Result<Unit>

    // xoa tai khoan
    suspend fun deleteAccount(): Result<Unit>

    // kiem tra email da duoc su dung chua
    suspend fun isEmailAlreadyInUse(email: String): Result<Boolean>
}