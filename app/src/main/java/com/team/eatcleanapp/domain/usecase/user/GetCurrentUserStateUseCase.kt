package com.team.eatcleanapp.domain.usecase.user

import com.google.firebase.auth.FirebaseUser
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User?> {
        val authResult = authRepository.getCurrentUser()
        if (authResult.isError) return Result.Error(message = authResult.errorMessage())

        val firebaseUser = authResult.getOrNull() ?: return Result.Success(null)

        val userResult = userRepository.getUserById(firebaseUser.uid)
        return when {
            userResult.isError -> createBasicUser(firebaseUser)
            else -> userResult
        }
    }

    private suspend fun createBasicUser(firebaseUser: FirebaseUser): Result<User?> {
        val user = User(
            id = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            name = firebaseUser.displayName ?: "",
            createdAt = System.currentTimeMillis()
        )

        val saveResult = userRepository.saveUser(user)
        return if (saveResult.isSuccess) {
            Result.Success(user)
        } else {
            Result.Error(message = saveResult.errorMessage())
        }
    }
}