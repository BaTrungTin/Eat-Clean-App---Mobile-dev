package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.UserDao
import com.team.eatcleanapp.data.mapper.UserMapper.toDomain
import com.team.eatcleanapp.data.mapper.UserMapper.toEntity
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUserById(userId: String): Result<User> {
        return try {
            val entity = userDao.getUserById(userId)
                ?: return Result.Error(message = "Không tìm thấy user")
            Result.Success(entity.toDomain())
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lấy thông tin user theo ID")
        }
    }

    override suspend fun getUserByEmail(email: String): Result<User?> {
        return try {
            val entity = userDao.getUserByEmail(email)
            Result.Success(entity?.toDomain())
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lấy thông tin user theo email")
        }
    }

    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            userDao.insertUser(user.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lưu thông tin user")
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        return try {
            userDao.updateUser(user.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi cập nhật thông tin user")
        }
    }

    override suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            userDao.deleteUserById(userId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa user data")
        }
    }

    override suspend fun checkEmailExists(email: String): Result<Boolean> {
        return try {
            val count = userDao.checkEmailExists(email)
            Result.Success(count > 0)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi kiểm tra email tồn tại")
        }
    }
}
