package com.team.eatcleanapp.domain.usecase.health

import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.domain.utils.NutritionCalculator
import javax.inject.Inject

class CheckHealthMetricsNeedUpdateUseCase @Inject constructor() {
    operator fun invoke(user: User): Boolean {
        return user.healthMetrics?.let { healthMetrics ->
            NutritionCalculator.needsHealthUpdate(healthMetrics.lastUpdated)
        } ?: true
    }
}