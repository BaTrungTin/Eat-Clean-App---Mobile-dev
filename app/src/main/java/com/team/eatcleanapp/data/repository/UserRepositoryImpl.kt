package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.UserDao
import com.team.eatcleanapp.data.local.entities.UserEntity
import com.team.eatcleanapp.domain.model.*
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result

class UserRepositoryImpl(
    private val userDao: UserDao,
) : UserRepository {

    override suspend fun createUser(user: User): Result<User> {
        return try {
            userDao.insertUser(user.toEntity())
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi tạo người dùng: ${e.message}"))
        }
    }

    override suspend fun getUserById(userId: String): Result<User> {
        return try {
            val entity = userDao.getUserById(userId)
            if (entity != null) {
                Result.Success(entity.toDomain())
            } else {
                Result.Error(Exception("Không tìm thấy người dùng"))
            }
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi lấy thông tin người dùng: ${e.message}"))
        }
    }

    override suspend fun getUserByEmail(email: String): Result<User?> {
        return try {
            val entity = userDao.getUserByEmail(email)
            Result.Success(entity?.toDomain())
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi tìm người dùng theo email: ${e.message}"))
        }
    }

    override suspend fun updateUser(user: User): Result<User> {
        return try {
            userDao.updateUser(user.toEntity())
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi cập nhật người dùng: ${e.message}"))
        }
    }

    override suspend fun deleteUser(userId: String): Result<Boolean> {
        return try {
            userDao.deleteUserById(userId)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi xóa người dùng: ${e.message}"))
        }
    }

    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            userDao.insertUser(user.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi lưu người dùng: ${e.message}"))
        }
    }

    override suspend fun clearLocalUser(): Result<Unit> {
        return try {
            userDao.clearAllUsers()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi xóa dữ liệu local: ${e.message}"))
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            val users = userDao.getAllUsers()
            val currentUser = users.firstOrNull()?.toDomain()
            Result.Success(currentUser)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi lấy người dùng hiện tại: ${e.message}"))
        }
    }

    override suspend fun checkEmailExists(email: String): Result<Boolean> {
        return try {
            val count = userDao.checkEmailExists(email)
            Result.Success(count > 0)
        } catch (e: Exception) {
            Result.Error(Exception("Lỗi kiểm tra email: ${e.message}"))
        }
    }

    // UserEntity → User (Domain)
    private fun UserEntity.toDomain(): User = User(
        id = id,
        email = email,
        name = name,
        weight = weight,
        height = height,
        age = age,
        gender = enumValueOf(gender),
        activityMinutesPerDay = activityMinutesPerDay,
        activityDaysPerWeek = activityDaysPerWeek,
        activityLevel = enumValueOf(activityLevel),
        goal = enumValueOf(goal),
        avatarUrl = avatarUrl,
        healthMetrics = HealthMetrics(
            bmi = bmi,
            bmr = bmr,
            tdee = tdee
        )
    )

    // User (Domain) → UserEntity
    private fun User.toEntity(): UserEntity = UserEntity(
        id = id,
        email = email,
        name = name,
        weight = weight,
        height = height,
        age = age,
        gender = gender.name,
        activityLevel = activityLevel.name,
        goal = goal.name,
        activityMinutesPerDay = activityMinutesPerDay,
        activityDaysPerWeek = activityDaysPerWeek,
        avatarUrl = avatarUrl,
        bmi = healthMetrics?.bmi ?: 0.0,
        bmr = healthMetrics?.bmr ?: 0.0,
        tdee = healthMetrics?.tdee ?: 0.0
    )
}