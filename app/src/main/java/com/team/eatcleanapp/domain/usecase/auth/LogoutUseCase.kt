package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.util.Result

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun execute(): Result<Unit> {
        return authRepository.logout()
    }
}