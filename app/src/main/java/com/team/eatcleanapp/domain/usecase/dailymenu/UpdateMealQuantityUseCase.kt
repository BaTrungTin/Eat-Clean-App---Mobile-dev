package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.util.Result

/**
 * Use case de cap nhat so phan an (quantity) cua 1 mon trong daily menu
 */
class UpdateMealQuantityUseCase(
    private val dailyMenuRepository: DailyMenuRepository
) {
    data class Params(
        val id: String,
        val newQuantity: Double
    )

    suspend operator fun invoke(params: Params): Result<Unit> {
        // Kiem tra
        if (params.id.isBlank()) {
            return Result.Error(IllegalArgumentException("ID khong duoc de trong"))
        }
        if (params.newQuantity <= 0) {
            return Result.Error(IllegalArgumentException("So phan an phai lon hon 0"))
        }

        return dailyMenuRepository.updateQuantity(params.id, params.newQuantity)
    }
}

