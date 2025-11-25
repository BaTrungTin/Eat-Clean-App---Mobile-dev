package com.team.eatcleanapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.eatcleanapp.data.local.dao.*
import com.team.eatcleanapp.data.local.entities.*




import com.team.eatcleanapp.util.Constants

@Database(
    entities = [
        UserEntity::class,
        MealEntity::class,
        FavoriteEntity::class,
        DailyMenuEntity::class,
        MealIntakeEntity::class,
        MealOverrideEntity::class
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
    abstract fun mealOverrideDao(): MealOverrideDao
}
