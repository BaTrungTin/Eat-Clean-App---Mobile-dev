package com.team.eatcleanapp.di

import com.team.eatcleanapp.data.repository.DailyMenuRepositoryImpl
import com.team.eatcleanapp.data.repository.FavoriteRepositoryImpl
import com.team.eatcleanapp.data.repository.MealRepositoryImpl
import com.team.eatcleanapp.data.repository.UserRepositoryImpl
import com.team.eatcleanapp.domain.repository.DailyMenuRepository
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.domain.repository.MealRepository
import com.team.eatcleanapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindDailyMenuRepository(
        dailyMenuRepositoryImpl: DailyMenuRepositoryImpl
    ): DailyMenuRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindMealRepository(
        mealRepositoryImpl: MealRepositoryImpl
    ): MealRepository
}
