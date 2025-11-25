package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.domain.utils.EmailValidator
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class SendPasswordResetEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank()) return Result.Error(message = "Email không được để trống")

        if (!EmailValidator.isValid(email))
            return Result.Error(message = "Email không đúng định dạng")

        return authRepository.sendPasswordResetEmail(email)
    }
}