package com.team.eatcleanapp.data.local.dao

import androidx.room.*
import com.team.eatcleanapp.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    
    @Query("SELECT * FROM favorites WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllFavoritesByUserId(userId: String): Flow<List<FavoriteEntity>>
    
    @Query("SELECT * FROM favorites WHERE userId = :userId AND mealId = :mealId LIMIT 1")
    suspend fun getFavoriteByMealId(userId: String, mealId: String): FavoriteEntity?
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND mealId = :mealId)")
    suspend fun isFavorite(userId: String, mealId: String): Boolean
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorites(favorites: List<FavoriteEntity>)
    
    @Query("DELETE FROM favorites WHERE userId = :userId AND mealId = :mealId")
    suspend fun removeFavorite(userId: String, mealId: String)
    
    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)
    
    @Query("DELETE FROM favorites WHERE userId = :userId")
    suspend fun deleteAllFavoritesByUserId(userId: String)
    
    @Query("SELECT COUNT(*) FROM favorites WHERE userId = :userId")
    suspend fun getFavoriteCount(userId: String): Int
}

