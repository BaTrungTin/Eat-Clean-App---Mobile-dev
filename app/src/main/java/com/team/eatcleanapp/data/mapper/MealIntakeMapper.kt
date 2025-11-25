package com.team.eatcleanapp.data.mapper

import com.team.eatcleanapp.data.local.entities.MealIntakeEntity
import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.domain.model.nutrition.MealIntake
import java.util.Date

object MealIntakeMapper {

    // Domain -> Entity
    fun MealIntake.toEntity(): MealIntakeEntity {
        return MealIntakeEntity(
            id = id,
            dailyMenuItemId = dailyMenuItemId,
            userId = userId,
            mealId = mealId,
            mealName = mealName,
            date = date.time,
            mealType = mealType.name,
            calories = calories,
            portionSize = portionSize,
            totalCalories = totalCalories,
            isConsumed = isConsumed
        )
    }

    // Entity -> Domain
    fun MealIntakeEntity.toDomain(): MealIntake {
        return MealIntake(
            id = id,
            dailyMenuItemId = dailyMenuItemId,
            userId = userId,
            mealId = mealId,
            mealName = mealName,
            date = Date(date),
            mealType = MealCategory.valueOf(mealType),
            calories = calories,
            portionSize = portionSize,
            isConsumed = isConsumed
        )
    }

    // List Entity -> List Domain
    fun List<MealIntakeEntity>.toDomain(): List<MealIntake> {
        return this.map { it.toDomain() }
    }
}