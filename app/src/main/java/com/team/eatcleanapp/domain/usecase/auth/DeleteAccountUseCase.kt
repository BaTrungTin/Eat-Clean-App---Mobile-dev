package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.util.Result

class DeleteAccountUseCase(
    private val authRepository: AuthRepository,
    private val dailyMenuRepository: DailyMenuRepository,
    private val favoriteRepository: FavoriteRepository
) {
    suspend fun execute(userId: String): Result<Unit> {
        return try {
            dailyMenuRepository.deleteAllByUserId(userId)

            val deleteFavoritesResult = favoriteRepository.deleteAllFavoritesByUserId(userId)
            if (deleteFavoritesResult is Result.Error) {
                return deleteFavoritesResult
            }

            authRepository.deleteAccount(userId)

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}