package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}