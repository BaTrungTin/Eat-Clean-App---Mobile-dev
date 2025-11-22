package com.team.eatcleanapp.data.local.database

import android.content.Context
import androidx.room.Room
import com.team.eatcleanapp.data.local.AppDatabase
import com.team.eatcleanapp.data.local.dao.DailyMenuDao
import com.team.eatcleanapp.data.local.dao.FavoriteDao
import com.team.eatcleanapp.data.local.dao.MealDao
import com.team.eatcleanapp.data.local.dao.MealIntakeDao
import com.team.eatcleanapp.data.local.dao.UserDao
import com.team.eatcleanapp.util.Constants

object DatabaseModule {

    private var database: AppDatabase? = null

    fun initialize(context: Context) {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                Constants.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    fun getDatabase(): AppDatabase {
        return database ?: throw IllegalStateException("Database must be initialized first")
    }

    fun getUserDao(): UserDao = getDatabase().userDao()

    fun getMealDao(): MealDao = getDatabase().mealDao()

    fun getFavoriteDao(): FavoriteDao = getDatabase().favoriteDao()

    fun getDailyMenuDao(): DailyMenuDao = getDatabase().dailyMenuDao()

    fun getMealIntakeDao(): MealIntakeDao = getDatabase().mealIntakeDao()

    fun closeDatabase() {
        database?.close()
        database = null
    }
}