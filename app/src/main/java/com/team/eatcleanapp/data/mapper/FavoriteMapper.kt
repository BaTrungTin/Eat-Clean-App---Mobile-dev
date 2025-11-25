package com.team.eatcleanapp.data.mapper

import com.team.eatcleanapp.data.local.entities.FavoriteEntity
import com.team.eatcleanapp.domain.model.meal.Favorite
import java.util.Date

object FavoriteMapper {

    // Entity -> Domain
    fun FavoriteEntity.toDomain(): Favorite {
        return Favorite(
            userId = userId,
            mealId = mealId,
            mealName = mealName,
            calories = calories,
            image = image,
            createdAt = Date(createdAt)
        )
    }

    // Domain -> Entity
    fun Favorite.toEntity(): FavoriteEntity {
        return FavoriteEntity(
            userId = userId,
            mealId = mealId,
            mealName = mealName,
            calories = calories,
            image = image ?: "",
            createdAt = createdAt.time
        )
    }

    // List Entity -> List Domain
    fun List<FavoriteEntity>.toDomain(): List<Favorite> {
        return this.map { it.toDomain() }
    }
}
