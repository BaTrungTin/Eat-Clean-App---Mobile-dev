package com.team.eatcleanapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.eatcleanapp.data.local.dao.DailyMenuDao
import com.team.eatcleanapp.data.local.dao.FavoriteDao
import com.team.eatcleanapp.data.local.dao.MealDao
import com.team.eatcleanapp.data.local.dao.MealIntakeDao
import com.team.eatcleanapp.data.local.dao.UserDao
import com.team.eatcleanapp.data.local.entities.DailyMenuEntity
import com.team.eatcleanapp.data.local.entities.FavoriteEntity
import com.team.eatcleanapp.data.local.entities.MealEntity
import com.team.eatcleanapp.data.local.entities.MealIntakeEntity
import com.team.eatcleanapp.data.local.entities.UserEntity
import com.team.eatcleanapp.util.Constants

@Database(
    entities = [
        UserEntity::class,
        MealEntity::class,
        FavoriteEntity::class,
        DailyMenuEntity::class,
        MealIntakeEntity::class
    ],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun dailyMenuDao(): DailyMenuDao
    abstract fun mealIntakeDao(): MealIntakeDao
}

