package com.team.eatcleanapp.data.repository

import com.team.eatcleanapp.data.local.dao.FavoriteDao
import com.team.eatcleanapp.data.mapper.FavoriteMapper.toDomain
import com.team.eatcleanapp.data.mapper.FavoriteMapper.toEntity
import com.team.eatcleanapp.domain.model.meal.Favorite
import com.team.eatcleanapp.domain.repository.FavoriteRepository
import com.team.eatcleanapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getFavorites(userId: String): Flow<Result<List<Favorite>>> {
        return favoriteDao.getAllFavoritesByUserId(userId)
            .map { entities ->
                Result.Success(entities.toDomain())
        }
    }

    override suspend fun isFavorite(
        userId: String,
        mealId: String
    ): Result<Boolean> {
        return try {
            val isFav = favoriteDao.isFavorite(userId, mealId)
            Result.Success(isFav)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi kiểm tra món yêu thích")
        }
    }

    override suspend fun addFavorite(favorite: Favorite): Result<Unit> {
        return try {
            val entity = favorite.toEntity()
            favoriteDao.addFavorite(entity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi lưu món yêu thích")
        }
    }

    override suspend fun removeFavorite(
        userId: String,
        mealId: String
    ): Result<Unit> {
        return try {
            favoriteDao.removeFavorite(userId, mealId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa món yêu thích")
        }
    }

    override suspend fun clearFavorites(userId: String): Result<Unit> {
        return try {
            favoriteDao.deleteAllFavoritesByUserId(userId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa tất cả món yêu thích")
        }
    }

    override suspend fun getFavoriteCount(userId: String): Result<Int> {
        return try {
            val count = favoriteDao.getFavoriteCount(userId)
            Result.Success(count)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi đếm số món yêu thích")
        }
    }

    override suspend fun updateFavoriteMealInfo(
        mealId: String,
        newMealName: String,
        newCalories: Double
    ): Result<Unit> {
        return try {
            favoriteDao.updateFavoriteMealInfo(mealId, newMealName, newCalories)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi cập nhật thông tin món yêu thích")
        }
    }

    override suspend fun removeFavoriteByMealId(mealId: String): Result<Unit> {
        return try {
            favoriteDao.deleteFavoritesByMealId(mealId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Lỗi khi xóa món yêu thích theo mealId")
        }
    }
}
