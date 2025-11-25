package com.team.eatcleanapp.domain.usecase.user

import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<User> {
        val updatedUser = user.copy(updatedAt = System.currentTimeMillis())
        return userRepository.updateUser(updatedUser).let { updateResult ->
            if (updateResult.isSuccess) {
                Result.Success(updatedUser)
            } else {
                Result.Error(message = updateResult.errorMessage())
            }
        }
    }
}