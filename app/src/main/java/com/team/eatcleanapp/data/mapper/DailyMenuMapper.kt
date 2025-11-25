package com.team.eatcleanapp.data.mapper

import com.team.eatcleanapp.data.local.entities.DailyMenuEntity
import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.enums.MealCategory
import java.util.Date

object DailyMenuMapper {

    // Domain -> Entity
    fun DailyMenuItem.toEntity(): DailyMenuEntity {
        return DailyMenuEntity(
            userId = userId,
            date = date.time,
            mealId = mealId,
            mealName = mealName,
            calories = calories,
            mealType = mealType.name,
            portionSize = portionSize,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }

    // Entity -> Domain
    fun DailyMenuEntity.toDomain(): DailyMenuItem {
        return DailyMenuItem(
            id = "$userId-$date-$mealId-$mealType", // Tạo composite ID
            userId = userId,
            date = Date(date),
            mealId = mealId,
            mealName = mealName,
            calories = calories,
            mealType = MealCategory.valueOf(mealType),
            portionSize = portionSize
        )
    }

    // List Entity -> List Domain
    fun List<DailyMenuEntity>.toDomain(): List<DailyMenuItem> {
        return this.map { it.toDomain() }
    }
}