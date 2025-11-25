package com.team.eatcleanapp.di

import com.team.eatcleanapp.domain.repository.*
import com.team.eatcleanapp.domain.usecase.auth.*
import com.team.eatcleanapp.domain.usecase.dailymenu.*
import com.team.eatcleanapp.domain.usecase.favorite.*
import com.team.eatcleanapp.domain.usecase.health.*
import com.team.eatcleanapp.domain.usecase.meal.*
import com.team.eatcleanapp.domain.usecase.user.*
import com.team.eatcleanapp.domain.usecase.mealintake.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // Auth UseCases
    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): RegisterUseCase {
        return RegisterUseCase(authRepository, userRepository)
    }

    @Provides
    @Singleton
    fun provideCheckEmailAvailabilityUseCase(authRepository: AuthRepository): CheckEmailAvailabilityUseCase {
        return CheckEmailAvailabilityUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideCheckRegistrationProgressUseCase(userRepository: UserRepository): CheckRegistrationProgressUseCase {
        return CheckRegistrationProgressUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteAccountUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository,
        dailyMenuRepository: DailyMenuRepository,
        favoriteRepository: FavoriteRepository,
        mealIntakeRepository: MealIntakeRepository
    ): DeleteAccountUseCase {
        return DeleteAccountUseCase(
            authRepository,
            userRepository,
            dailyMenuRepository,
            favoriteRepository,
            mealIntakeRepository
        )
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSendPasswordResetEmailUseCase(authRepository: AuthRepository): SendPasswordResetEmailUseCase {
        return SendPasswordResetEmailUseCase(authRepository)
    }

    // Daily Menu UseCases
    @Provides
    @Singleton
    fun provideAddMealToDailyMenuUseCase(
        dailyMenuRepository: DailyMenuRepository,
        mealRepository: MealRepository
    ): AddMealToDailyMenuUseCase {
        return AddMealToDailyMenuUseCase(dailyMenuRepository, mealRepository)
    }

    @Provides
    @Singleton
    fun provideCalculateDailyNutritionUseCase(
        dailyMenuRepository: DailyMenuRepository,
        mealIntakeRepository: MealIntakeRepository
    ): CalculateDailyNutritionUseCase {
        return CalculateDailyNutritionUseCase(dailyMenuRepository, mealIntakeRepository)
    }

    @Provides
    @Singleton
    fun provideGetDailyMenuUseCase(dailyMenuRepository: DailyMenuRepository): GetDailyMenuUseCase {
        return GetDailyMenuUseCase(dailyMenuRepository)
    }

    @Provides
    @Singleton
    fun provideGetWeeklyMenuUseCase(dailyMenuRepository: DailyMenuRepository): GetWeeklyMenuUseCase {
        return GetWeeklyMenuUseCase(dailyMenuRepository)
    }

    @Provides
    @Singleton
    fun provideUpdatePortionSizeUseCase(dailyMenuRepository: DailyMenuRepository): UpdatePortionSizeUseCase {
        return UpdatePortionSizeUseCase(dailyMenuRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteSpecificMealUseCase(dailyMenuRepository: DailyMenuRepository): DeleteSpecificMealUseCase {
        return DeleteSpecificMealUseCase(dailyMenuRepository)
    }

    // Favorite UseCases
    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(
        favoriteRepository: FavoriteRepository,
        mealRepository: MealRepository,
        mealOverrideRepository: MealOverrideRepository
    ): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCase(favoriteRepository, mealRepository, mealOverrideRepository)
    }

    @Provides
    @Singleton
    fun provideGetFavoriteMealsUseCase(favoriteRepository: FavoriteRepository): GetFavoriteMealsUseCase {
        return GetFavoriteMealsUseCase(favoriteRepository)
    }

    @Provides
    @Singleton
    fun provideCheckIsFavoriteUseCase(favoriteRepository: FavoriteRepository): CheckIsFavoriteUseCase {
        return CheckIsFavoriteUseCase(favoriteRepository)
    }

    @Provides
    @Singleton
    fun provideGetMealWithOverrideUseCase(
        mealRepository: MealRepository,
        mealOverrideRepository: MealOverrideRepository
    ): GetMealWithOverrideUseCase {
        return GetMealWithOverrideUseCase(mealRepository, mealOverrideRepository)
    }

    @Provides
    @Singleton
    fun provideSaveMealOverrideUseCase(
        mealOverrideRepository: MealOverrideRepository,
        favoriteRepository: FavoriteRepository,
        mealRepository: MealRepository
    ): SaveMealOverrideUseCase {
        return SaveMealOverrideUseCase(mealOverrideRepository, favoriteRepository, mealRepository)
    }

    // Health UseCases
    @Provides
    @Singleton
    fun provideHealthCalculatorUseCase(): HealthCalculatorUseCase {
        return HealthCalculatorUseCase()
    }

    @Provides
    @Singleton
    fun provideUpdateHealthMetricsUseCase(userRepository: UserRepository): UpdateHealthMetricsUseCase {
        return UpdateHealthMetricsUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideCheckHealthMetricsNeedUpdateUseCase(): CheckHealthMetricsNeedUpdateUseCase {
        return CheckHealthMetricsNeedUpdateUseCase()
    }

    // Meal UseCases
    @Provides
    @Singleton
    fun provideGetMealsUseCase(mealRepository: MealRepository): GetMealsUseCase {
        return GetMealsUseCase(mealRepository)
    }

    @Provides
    @Singleton
    fun provideGetMealDetailUseCase(mealRepository: MealRepository): GetMealDetailUseCase {
        return GetMealDetailUseCase(mealRepository)
    }

    @Provides
    @Singleton
    fun provideManageMealsUseCase(mealRepository: MealRepository): ManageMealsUseCase {
        return ManageMealsUseCase(mealRepository)
    }

    // User UseCases
    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository, userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserProfileUseCase(userRepository: UserRepository): GetUserProfileUseCase {
        return GetUserProfileUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideCompleteUserProfileUseCase(userRepository: UserRepository): CompleteUserProfileUseCase {
        return CompleteUserProfileUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserProfileUseCase(userRepository: UserRepository): UpdateUserProfileUseCase {
        return UpdateUserProfileUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideSavePartialProfileUseCase(userRepository: UserRepository): SavePartialProfileUseCase {
        return SavePartialProfileUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserAvatarUseCase(userRepository: UserRepository): UpdateUserAvatarUseCase {
        return UpdateUserAvatarUseCase(userRepository)
    }

    // Meal Intake UseCases
    @Provides
    @Singleton
    fun provideGetMealIntakeByDateUseCase(mealIntakeRepository: MealIntakeRepository): GetMealIntakeByDateUseCase {
        return GetMealIntakeByDateUseCase(mealIntakeRepository)
    }

    @Provides
    @Singleton
    fun provideGetNutritionProgressUseCase(mealIntakeRepository: MealIntakeRepository): GetNutritionProgressUseCase {
        return GetNutritionProgressUseCase(mealIntakeRepository)
    }

    @Provides
    @Singleton
    fun provideSaveMealIntakeUseCase(mealIntakeRepository: MealIntakeRepository): SaveMealIntakeUseCase {
        return SaveMealIntakeUseCase(mealIntakeRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateConsumedStatusUseCase(mealIntakeRepository: MealIntakeRepository): UpdateConsumedStatusUseCase {
        return UpdateConsumedStatusUseCase(mealIntakeRepository)
    }
}