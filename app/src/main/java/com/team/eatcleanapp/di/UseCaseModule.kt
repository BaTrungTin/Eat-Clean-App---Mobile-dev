package com.team.eatcleanapp.di

import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.domain.usecase.meal.GetAllMealsUseCase
import com.team.eatcleanapp.domain.usecase.meal.GetMealDetailUseCase
import com.team.eatcleanapp.domain.usecase.meal.SearchMealsUseCase

object UseCaseModule {

    fun provideGetAllMealsUseCase(mealRepository: MealRepository): GetAllMealsUseCase {
        return GetAllMealsUseCase(mealRepository)
    }

    fun provideGetMealDetailUseCase(mealRepository: MealRepository): GetMealDetailUseCase {
        return GetMealDetailUseCase(mealRepository)
    }

    fun provideSearchMealsUseCase(mealRepository: MealRepository): SearchMealsUseCase {
        return SearchMealsUseCase(mealRepository)
    }
}