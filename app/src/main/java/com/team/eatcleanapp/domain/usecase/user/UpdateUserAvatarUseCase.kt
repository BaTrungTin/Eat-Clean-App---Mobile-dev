package com.team.eatcleanapp.domain.usecase.user

import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class UpdateUserAvatarUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, avatarUrl: String): Result<User> {
        val userResult = userRepository.getUserById(userId)
        if (userResult.isError) return userResult

        val user = userResult.getOrThrow()
        val updatedUser = user.copy(
            avatarUrl = avatarUrl,
            updatedAt = System.currentTimeMillis()
        )

        return userRepository.updateUser(updatedUser).let { updateResult ->
            if (updateResult.isSuccess) {
                Result.Success(updatedUser)
            } else {
                Result.Error(message = updateResult.errorMessage())
            }
        }
    }
}