package com.team.eatcleanapp.domain.usecase.dailymenu

import com.team.eatcleanapp.domain.model.DailyMenu
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import kotlinx.coroutines.flow.first
import java.util.Date

class GetDailyMenuUseCase(
    private val repository: DailyMenuRepository
) {
    suspend operator fun invoke(
        userId: String,
        date: Date
    ): List<DailyMenu> {
        if (userId.isBlank()) {
            return emptyList()
        }

        return try {
            val result = repository.getDailyMenu(userId, date).first()
            when (result) {
                is com.team.eatcleanapp.util.Result.Success -> result.data
                else -> emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

