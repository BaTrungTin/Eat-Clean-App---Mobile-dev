package com.team.eatcleanapp.di

import com.team.eatcleanapp.data.local.dao.MealDao
import com.team.eatcleanapp.data.repository.MealRepositoryImpl
import com.team.eatcleanapp.domain.repository.MealRepository

object RepositoryModule {

    fun provideMealRepository(mealDao: MealDao): MealRepository {
        return MealRepositoryImpl(
            mealDao = mealDao,
            realtimeDatabaseService = FirebaseModule.realtimeDatabaseService
        )
    }
}