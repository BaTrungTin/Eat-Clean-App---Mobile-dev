package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.model.User
import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.util.Result

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun execute(): Result<User?> {
        return authRepository.getCurrentUser()
    }
}