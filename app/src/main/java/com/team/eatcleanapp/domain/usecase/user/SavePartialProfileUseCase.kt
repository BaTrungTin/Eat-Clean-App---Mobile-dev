package com.team.eatcleanapp.domain.usecase.user

import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class SavePartialProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: String,
        name: String? = null,
        weight: Double? = null,
        height: Double? = null,
        age: Int? = null
    ): Result<User> {
        val userResult = userRepository.getUserById(userId)
        if (userResult.isError) return Result.Error(message = userResult.errorMessage())

        val existingUser = userResult.getOrThrow()
        validatePartialData(name, weight, height, age)?.let {
            return Result.Error(message = it)
        }

        val updatedUser = existingUser.copy(
            name = name ?: existingUser.name,
            weight = weight ?: existingUser.weight,
            height = height ?: existingUser.height,
            age = age ?: existingUser.age,
            updatedAt = System.currentTimeMillis()
        )

        val updateResult = userRepository.updateUser(updatedUser)
        return if (updateResult.isSuccess) {
            Result.Success(updatedUser)
        } else {
            Result.Error(message = updateResult.errorMessage())
        }
    }

    private fun validatePartialData(
        name: String?, weight: Double?, height: Double?, age: Int?
    ): String? {
        name?.let { if (it.isBlank()) return "Họ tên không được để trống" }
        weight?.let { if (it <= 0) return "Cân nặng phải lớn hơn 0" }
        height?.let { if (it <= 0) return "Chiều cao phải lớn hơn 0" }
        age?.let { if (it <= 0) return "Tuổi phải lớn hơn 0" }
        return null
    }
}