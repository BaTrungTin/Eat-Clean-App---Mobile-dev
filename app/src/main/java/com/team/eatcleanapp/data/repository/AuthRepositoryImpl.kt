package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.remote.firebase.FirebaseAuthDataSource
import com.team.eatcleanapp.domain.model.*
import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result

class AuthRepositoryImpl(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val userRepository: UserRepository
) : AuthRepository {

    override suspend fun registerUser(email: String, password: String): Result<String> {
        return try {
            val firebaseUser = firebaseAuthDataSource.registerWithEmail(email, password)

            // Tạo user tạm thời với thông tin cơ bản
            val tempUser = User(
                id = firebaseUser.uid,
                email = email,
                name = "",
                weight = 0.0,
                height = 0.0,
                age = 0,
                gender = Gender.MALE,
                activityMinutesPerDay = 0,
                activityDaysPerWeek = 0,
                activityLevel = ActivityLevel.SEDENTARY,
                goal = Goal.MAINTAIN_WEIGHT,
                healthMetrics = null
            )

            // Lưu user tạm vào local database
            userRepository.createUser(tempUser)
            Result.Success(firebaseUser.uid)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi đăng ký: ${e.message}"))
        }
    }

    override suspend fun completeRegistration(user: User): Result<User> {
        return try {
            val result = userRepository.updateUser(user)
            when (result) {
                is Result.Success -> Result.Success(user)
                is Result.Error -> result
                else -> Result.Error(Exception("Lỗi không xác định khi hoàn thành đăng ký"))
            }
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi hoàn thành đăng ký: ${e.message}"))
        }
    }

    override suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val firebaseUser = firebaseAuthDataSource.loginWithEmail(email, password)
            val userId = firebaseUser.uid

            // Xóa dữ liệu local cũ trước khi đăng nhập user mới
            userRepository.clearLocalUser()

            // Thử lấy user từ database
            val userResult = userRepository.getUserById(userId)

            return when (userResult) {
                is Result.Success -> {
                    // User tồn tại, lưu vào local và return
                    userRepository.saveUser(userResult.data)
                    Result.Success(userResult.data)
                }
                is Result.Error -> {
                    // User không tồn tại, tạo user mới
                    val newUser = User(
                        id = userId,
                        email = email,
                        name = firebaseUser.displayName ?: "Người dùng",
                        weight = 0.0,
                        height = 0.0,
                        age = 0,
                        gender = Gender.MALE,
                        activityMinutesPerDay = 0,
                        activityDaysPerWeek = 0,
                        activityLevel = ActivityLevel.SEDENTARY,
                        goal = Goal.MAINTAIN_WEIGHT,
                        healthMetrics = null
                    )

                    userRepository.createUser(newUser)
                    Result.Success(newUser)
                }
                else -> Result.Error(Exception("Kết quả không xác định khi đăng nhập"))
            }

        } catch (e: Exception) {
            Result.Error(Exception("Lỗi đăng nhập: ${e.message}"))
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            userRepository.clearLocalUser()
            firebaseAuthDataSource.logout()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi đăng xuất: ${e.message}"))
        }
    }

    override suspend fun deleteAccount(userId: String): Result<Unit> {
        return try {
            userRepository.deleteUser(userId)
            firebaseAuthDataSource.deleteAccount()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi xóa tài khoản: ${e.message}"))
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return userRepository.getCurrentUser()
    }
}