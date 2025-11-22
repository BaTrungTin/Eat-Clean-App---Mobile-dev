package com.team.eatcleanapp.data.local.dao

import androidx.room.*
import com.team.eatcleanapp.data.local.entities.FavoriteEntity

@Dao
interface FavoriteDao {
    // thêm 1 món vào favorite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    // lấy danh sách favorite của người dùng
    @Query("SELECT * FROM favorites WHERE userId = :userId")
    suspend fun getAllFavoritesByUserId(userId: String): List<FavoriteEntity>

    // lấy 1 món yêu thích của người dùng theo mealid
    @Query("SELECT * FROM favorites WHERE userId = :userId AND mealId = :mealId")
    suspend fun getFavoriteByMealId(userId: String, mealId: String): FavoriteEntity?

    // kiểm tra trong ds favorite có món đó không
    @Query("""
        SELECT EXISTS(
        SELECT 1 FROM favorites 
        WHERE userId = :userId 
        AND mealId = :mealId)
    """)
    suspend fun isFavorite(userId: String, mealId: String): Boolean

    // xóa 1 món cụ thể trong favorite
    @Query("DELETE FROM favorites WHERE userId = :userId AND mealId = :mealId")
    suspend fun deleteFavorite(userId: String, mealId: String)

    // xóa tất cả món yêu thích của người dùng
    @Query("DELETE FROM favorites WHERE userId = :userId")
    suspend fun deleteAllFavoritesByUserId(userId: String)

    // đếm số lượng món yêu thích của người dùng
    @Query("SELECT COUNT(*) FROM favorites WHERE userId = :userId")
    suspend fun getCountByUserId(userId: String): Int

    // cập nhật thông tin của món yêu thích
    @Update
    suspend fun update(favorite: FavoriteEntity)
}

