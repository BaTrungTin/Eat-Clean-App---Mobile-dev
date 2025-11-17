package com.team.eatcleanapp.di

import android.app.Application
import androidx.room.Room
import com.team.eatcleanapp.data.local.AppDatabase
import com.team.eatcleanapp.data.local.dao.DailyMenuDao
import com.team.eatcleanapp.data.local.dao.FavoriteDao
import com.team.eatcleanapp.data.local.dao.MealDao
import com.team.eatcleanapp.data.local.dao.MealIntakeDao
import com.team.eatcleanapp.data.local.dao.UserDao
import com.team.eatcleanapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            Constants.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideMealDao(db: AppDatabase): MealDao = db.mealDao()

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()

    @Provides
    fun provideDailyMenuDao(db: AppDatabase): DailyMenuDao = db.dailyMenuDao()

    @Provides
    fun provideMealIntakeDao(db: AppDatabase): MealIntakeDao = db.mealIntakeDao()
}
