package com.team.eatcleanapp.domain.model.meal

import java.util.Date

data class Favorite(
    val userId: String,
    val mealId: String,
    val mealName: String,
    val calories: Double,
    val image: String?,
    val createdAt: Date
)
