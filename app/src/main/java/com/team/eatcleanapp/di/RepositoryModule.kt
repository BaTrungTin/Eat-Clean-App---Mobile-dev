package com.team.eatcleanapp.di

import com.team.eatcleanapp.data.repository.*
import com.team.eatcleanapp.domain.repository.*
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

    @Binds
    @Singleton
    abstract fun bindMealIntakeRepository(
        mealIntakeRepositoryImpl: MealIntakeRepositoryImpl
    ): MealIntakeRepository

    @Binds
    @Singleton
    abstract fun bindMealOverrideRepository(
        mealOverrideRepositoryImpl: MealOverrideRepositoryImpl
    ): MealOverrideRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}