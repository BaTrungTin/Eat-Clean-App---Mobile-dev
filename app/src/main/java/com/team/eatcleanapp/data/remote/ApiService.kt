package com.team.eatcleanapp.data.remote

import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.domain.model.DailyMenu
import com.team.eatcleanapp.domain.model.User
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    
    // ==================== AUTHENTICATION ====================
    

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<User>
    

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<User>
    
 
    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String,
        @Body request: LogoutRequest
    ): Response<Unit>

    @DELETE("auth/account/{userId}")
    suspend fun deleteAccount(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<Unit>
    
    // ==================== MEALS ====================

    @GET("meals")
    suspend fun getAllMeals(): Response<List<Meal>>
    

    @GET("meals/search")
    suspend fun searchMeals(
        @Query("q") query: String,
        @Query("maxCalories") maxCalories: Double? = null
    ): Response<List<Meal>>
 
    @GET("meals/{mealId}")
    suspend fun getMealDetail(
        @Path("mealId") mealId: String
    ): Response<Meal>
    
    // ==================== FAVORITES ====================
    
  
    @GET("users/{userId}/favorites")
    suspend fun getFavorites(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<List<Meal>>
    

    @POST("users/{userId}/favorites")
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body request: AddFavoriteRequest
    ): Response<FavoriteResponse>
    
  
    @DELETE("users/{userId}/favorites/{mealId}")
    suspend fun removeFavorite(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Path("mealId") mealId: String
    ): Response<Unit>
    
   
    @GET("users/{userId}/favorites/{mealId}")
    suspend fun isFavorite(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Path("mealId") mealId: String
    ): Response<Boolean>
    
    // ==================== DAILY MENU ====================
    
   
    @GET("users/{userId}/daily-menu")
    suspend fun getDailyMenu(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Query("date") date: String
    ): Response<List<DailyMenu>>
    
  
    @GET("users/{userId}/weekly-menu")
    suspend fun getWeeklyMenu(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Query("startDate") startDate: String
    ): Response<List<DailyMenu>>
    
 
    @POST("users/{userId}/daily-menu")
    suspend fun addMealToDailyMenu(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body request: AddMealToDailyMenuRequest
    ): Response<DailyMenuResponse>
    

    @DELETE("users/{userId}/daily-menu/{menuId}")
    suspend fun deleteMealFromDailyMenu(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Path("menuId") menuId: String
    ): Response<Unit>
    
    // ==================== REQUEST/RESPONSE DATA CLASSES ====================
    
    data class LoginRequest(
        val email: String,
        val password: String
    )
    
    data class RegisterRequest(
        val email: String,
        val password: String,
        val name: String,
        val age: Int? = null,
        val gender: String? = null,
        val height: Double? = null,
        val weight: Double? = null
    )
    
    data class LogoutRequest(
        val userId: String
    )
    
    data class AddFavoriteRequest(
        val mealId: String
    )
    
    data class FavoriteResponse(
        val success: Boolean,
        val message: String? = null
    )
    
    data class AddMealToDailyMenuRequest(
        val date: String, // yyyy-MM-dd
        val mealId: String,
        val mealType: String, // BREAKFAST, LUNCH, DINNER, SNACK
        val quantity: Double = 1.0
    )
    
    data class DailyMenuResponse(
        val id: String,
        val userId: String,
        val date: String,
        val mealId: String,
        val mealName: String,
        val calories: Double,
        val mealType: String,
        val protein: Double? = null,
        val carbs: Double? = null,
        val fat: Double? = null
    )
}
