package com.team.eatcleanapp.domain.usecase.meal

import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.repository.MealRepository

class GetAllMealsUseCase(
    private val mealRepository: MealRepository
) {

    suspend operator fun invoke(): List<Meal> {
        // Lấy list món từ repository
        val meals = mealRepository.getAllMeals()

        return meals.sortedBy { it.name.lowercase() }
    }
}