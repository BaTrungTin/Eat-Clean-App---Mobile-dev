package com.team.eatcleanapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.eatcleanapp.data.local.dao.DailyMenuDao
import com.team.eatcleanapp.data.local.dao.FavoriteDao
import com.team.eatcleanapp.data.local.entities.DailyMenuEntity
import com.team.eatcleanapp.data.local.entities.FavoriteEntity
import com.team.eatcleanapp.util.Constants

@Database(
    entities = [
        FavoriteEntity::class,
        DailyMenuEntity::class
    ],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun dailyMenuDao(): DailyMenuDao
}
