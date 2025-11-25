package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.model.enums.Goal
import com.team.eatcleanapp.domain.model.enums.RegistrationProgress
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class CheckRegistrationProgressUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<RegistrationProgress> {
        val userResult = userRepository.getUserById(userId)
        if (userResult.isError || userResult.getOrNull() == null) {
            return Result.Error(message = "Không tìm thấy thông tin người dùng")
        }

        return Result.Success(determineProgress(userResult.getOrThrow()))
    }

    private fun determineProgress(user: User): RegistrationProgress {
        return when {
            !hasBasicProfile(user) -> RegistrationProgress.NEEDS_BASIC_PROFILE
            !hasHealthProfile(user) -> RegistrationProgress.NEEDS_HEALTH_PROFILE
            !hasGoal(user) -> RegistrationProgress.NEEDS_GOAL
            else -> RegistrationProgress.COMPLETED
        }
    }

    private fun hasBasicProfile(user: User): Boolean {
        return user.name.isNotBlank() && user.email.isNotBlank()
    }

    private fun hasHealthProfile(user: User): Boolean {
        return user.weight > 0.0 && user.height > 0.0 && user.age > 0
    }

    private fun hasGoal(user: User): Boolean {
        return user.goal != Goal.MAINTAIN_WEIGHT
    }
}