package com.team.eatcleanapp.data.remote

import com.team.eatcleanapp.data.remote.dto.MealApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API Service cho TheMealDB - API miễn phí về món ăn
 * Base URL: https://www.themealdb.com/api/json/v1/1/
 * Không cần API key, hoàn toàn miễn phí
 */
interface MealApiService {
    
    /**
     * Lấy danh sách món ăn theo category
     * @param category: "Beef", "Chicken", "Dessert", "Lamb", "Miscellaneous", "Pasta", "Pork", "Seafood", "Side", "Starter", "Vegan", "Vegetarian", "Breakfast", "Goat"
     */
    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ): MealApiResponse
    
    /**
     * Tìm kiếm món ăn theo tên
     */
    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") searchQuery: String
    ): MealApiResponse
    
    /**
     * Lấy chi tiết món ăn theo ID
     */
    @GET("lookup.php")
    suspend fun getMealDetail(
        @Query("i") mealId: String
    ): MealApiResponse
    
    /**
     * Lấy danh sách categories
     */
    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse
    
    /**
     * Lấy danh sách món ăn ngẫu nhiên
     */
    @GET("random.php")
    suspend fun getRandomMeal(): MealApiResponse
}

data class CategoriesResponse(
    val categories: List<Category>
)

data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

