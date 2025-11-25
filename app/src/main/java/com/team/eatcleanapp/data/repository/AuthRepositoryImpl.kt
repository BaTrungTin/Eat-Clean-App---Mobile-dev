package com.team.eatcleanapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.team.eatcleanapp.data.mapper.UserMapper
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(
                email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.Error(message = "Tạo tài khoản thất bại")

            Result.Success(firebaseUser)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi đăng ký: ${e.message}" )
        }
    }

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.Error(message = "Đăng nhập thất bại")

            Result.Success(firebaseUser)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi đăng nhập: ${e.message}")
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi đăng xuất")
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi gửi email reset password")
        }
    }

    override suspend fun getCurrentUser(): Result<FirebaseUser?> {
        return try {
            Result.Success(auth.currentUser)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lấy user hiện tại")
        }
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            val user = auth.currentUser
                ?: return Result.Error(message = "Không tìm thấy user đăng nhập")

            user.updatePassword(newPassword).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi cập nhật password")
        }
    }

    override suspend fun updateEmail(newEmail: String): Result<Unit> {
        return try {
            val user = auth.currentUser ?: return Result.Error(message = "Không tìm thấy user đăng nhập")
            user.updateEmail(newEmail).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi cập nhật email")
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val user = auth.currentUser ?: return Result.Error(message = "Không tìm thấy user đăng nhập")
            user.delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa tài khoản")
        }
    }

    override suspend fun isEmailAlreadyInUse(email: String): Result<Boolean> {
        return try {
            val methods = auth.fetchSignInMethodsForEmail(email).await()
            Result.Success(methods.signInMethods?.isNotEmpty() == true)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi kiểm tra email")
        }
    }
}