package com.team.eatcleanapp.domain.usecase.auth

import com.team.eatcleanapp.domain.repository.AuthRepository
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.domain.repository.MealIntakeRepository
import com.team.eatcleanapp.domain.repository.UserRepository
import com.team.eatcleanapp.util.Result
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val dailyMenuRepository: DailyMenuRepository,
    private val favoriteRepository: FavoriteRepository,
    private val mealIntakeRepository: MealIntakeRepository
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        val deleteResults = listOf(
            userRepository.deleteUser(userId),
            dailyMenuRepository.deleteAllByUserId(userId),
            favoriteRepository.clearFavorites(userId),
            mealIntakeRepository.deleteAllByUserId(userId)
        )

        val failedDeletion = deleteResults.find { it.isError }
        if (failedDeletion != null) {
            return Result.Error(message = "Lỗi khi xóa dữ liệu: ${failedDeletion.errorMessage()}")
        }

        return authRepository.deleteAccount()
    }
}